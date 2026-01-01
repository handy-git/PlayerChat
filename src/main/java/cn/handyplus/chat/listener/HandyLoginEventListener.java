package cn.handyplus.chat.listener;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.ChannelUtil;
import cn.handyplus.chat.enter.ChatPlayerChannelEnter;
import cn.handyplus.chat.enter.ChatPlayerNickEnter;
import cn.handyplus.chat.service.ChatPlayerChannelService;
import cn.handyplus.chat.service.ChatPlayerIgnoreService;
import cn.handyplus.chat.service.ChatPlayerNickService;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.internal.HandyLoginEvent;
import cn.handyplus.lib.internal.HandySchedulerUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.HandyHttpUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Optional;

/**
 * 登录事件
 *
 * @author handy
 */
@HandyListener
public class HandyLoginEventListener implements Listener {

    /**
     * 设置频道
     *
     * @param event 事件
     */
    @EventHandler
    public void onEvent(HandyLoginEvent event) {
        Player player = event.getPlayer();
        HandySchedulerUtil.runTaskAsynchronously(() -> {
            Optional<ChatPlayerChannelEnter> enterOptional = ChatPlayerChannelService.getInstance().findByUid(player.getUniqueId());
            String defaultChannel = BaseConstants.CONFIG.getString("firstLoginChatDefault", ChatConstants.DEFAULT);
            String channel = defaultChannel;
            if (!enterOptional.isPresent()) {
                ChatPlayerChannelEnter enter = new ChatPlayerChannelEnter();
                enter.setPlayerName(player.getName());
                enter.setPlayerUuid(player.getUniqueId());
                enter.setChannel(ChatConstants.DEFAULT);
                enter.setIsApi(false);
                ChatPlayerChannelService.getInstance().add(enter);
            } else {
                channel = enterOptional.get().getChannel();
                // 判断是否有权限
                if (!enterOptional.get().getIsApi() && !player.hasPermission(ChatConstants.PLAYER_CHAT_USE + channel)) {
                    ChatPlayerChannelService.getInstance().setChannel(player.getUniqueId(), defaultChannel);
                    return;
                }
            }
            // 缓存频道
            ChatConstants.PLAYER_CHAT_CHANNEL.put(player.getUniqueId(), channel);
            // 判断频道是否存在
            if (StrUtil.isEmpty(ChannelUtil.isChannelEnable(channel))) {
                ChatPlayerChannelService.getInstance().setChannel(player.getUniqueId(), ChatConstants.DEFAULT);
            }
            // 缓存屏蔽列表
            ChatConstants.PLAYER_IGNORE_MAP.put(player.getUniqueId(), ChatPlayerIgnoreService.getInstance().findIgnoreByUid(player.getUniqueId()));
        });
    }

    /**
     * 进入服务器发送更新提醒
     *
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpPlayerJoin(HandyLoginEvent event) {
        HandyHttpUtil.checkVersion(event.getPlayer());
    }

    /**
     * 缓存昵称
     *
     * @param event 事件
     * @since 2.0.5
     */
    @EventHandler
    public void onCacheNickEvent(HandyLoginEvent event) {
        Player player = event.getPlayer();
        HandySchedulerUtil.runTaskAsynchronously(() -> {
            Optional<ChatPlayerNickEnter> nickOptional = ChatPlayerNickService.getInstance().findByPlayerUuid(player.getUniqueId());
            String nickName = nickOptional.map(ChatPlayerNickEnter::getNickName).orElse(player.getName());
            ChatConstants.PLAYER_CHAT_NICK.put(player.getUniqueId(), nickName);
        });
    }

    /**
     * 获取玩家列表
     *
     * @param event 事件
     */
    @EventHandler
    public void getPlayerList(HandyLoginEvent event) {
        BcUtil.sendPlayerList();
        String name = event.getPlayer().getName();
        if (!ChatConstants.PLAYER_LIST.contains(name)) {
            ChatConstants.PLAYER_LIST.add(name);
        }
    }

}