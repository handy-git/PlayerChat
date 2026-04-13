package cn.handyplus.chat.core;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.enter.ChatPlayerMuteEnter;
import cn.handyplus.chat.service.ChatPlayerMuteService;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * 禁言工具类
 *
 * @author handy
 */
public class MuteUtil {

    /**
     * 检查玩家是否被禁言
     *
     * @param player 玩家
     * @return true-被禁言，false-未被禁言
     */
    public static boolean checkMute(Player player) {
        UUID playerUuid = player.getUniqueId();

        // 从缓存获取，如果没有则查询数据库并缓存（包括空值）
        Optional<ChatPlayerMuteEnter> muteOpt = ChatConstants.PLAYER_MUTE_CACHE.get(playerUuid);
        
        // 缓存未命中，查询数据库
        if (muteOpt == null) {
            muteOpt = ChatPlayerMuteService.getInstance().findActiveMute(playerUuid);
            // 缓存查询结果（包括 Optional.empty()）
            ChatConstants.PLAYER_MUTE_CACHE.put(playerUuid, muteOpt);
        }

        if (!muteOpt.isPresent()) {
            return false;
        }

        ChatPlayerMuteEnter mute = muteOpt.get();
        // 检查是否已过期
        long remainingTime = (mute.getExpireTime().getTime() - System.currentTimeMillis()) / 1000;
        if (remainingTime <= 0) {
            // 已过期，更新缓存为空值而不是移除
            ChatConstants.PLAYER_MUTE_CACHE.put(playerUuid, Optional.empty());
            return false;
        }

        // 发送禁言提示消息
        HashMap<String, String> replaceMap = MapUtil.of("${time}", String.valueOf(remainingTime), "${reason}", mute.getReason());
        String mutedMsg = BaseUtil.getLangMsg("mutedMsg", replaceMap);
        MessageUtil.sendMessage(player, mutedMsg);
        return true;
    }

}
