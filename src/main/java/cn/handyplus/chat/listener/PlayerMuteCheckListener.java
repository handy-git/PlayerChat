package cn.handyplus.chat.listener;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.enter.ChatPlayerMuteEnter;
import cn.handyplus.chat.service.ChatPlayerMuteService;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * 禁言检查监听器
 *
 * @author handy
 */
@HandyListener
public class PlayerMuteCheckListener implements Listener {

    /**
     * 禁言检查
     *
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onMuteChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();

        // 从缓存获取，如果没有则查询数据库并缓存
        Optional<ChatPlayerMuteEnter> muteOpt = ChatConstants.PLAYER_MUTE_CACHE.computeIfAbsent(playerUuid,
                uuid -> ChatPlayerMuteService.getInstance().findActiveMute(uuid));

        if (!muteOpt.isPresent()) {
            return;
        }

        ChatPlayerMuteEnter mute = muteOpt.get();
        // 检查是否已过期
        long remainingTime = (mute.getExpireTime().getTime() - System.currentTimeMillis()) / 1000;
        if (remainingTime <= 0) {
            // 已过期，移除缓存
            ChatConstants.PLAYER_MUTE_CACHE.remove(playerUuid);
            return;
        }

        event.setCancelled(true);
        HashMap<String, String> replaceMap = MapUtil.of("${time}", String.valueOf(remainingTime), "${reason}", mute.getReason());
        String mutedMsg = BaseUtil.getLangMsg("mutedMsg", replaceMap);
        MessageUtil.sendMessage(player, mutedMsg);
    }

}
