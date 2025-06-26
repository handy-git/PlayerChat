package cn.handyplus.chat.listener;

import cn.handyplus.chat.core.ChatUtil;
import cn.handyplus.chat.event.PlayerChannelChatEvent;
import cn.handyplus.chat.event.PlayerChannelTellEvent;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.util.BcUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * 玩家频道聊天
 *
 * @author handy
 */
@HandyListener
public class PlayerChannelChatEventListener implements Listener {

    /**
     * 频道聊天信息处理.
     *
     * @param event 事件
     */
    @EventHandler
    public void onChat(PlayerChannelChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        sendMsg(event.getBcMessageParam(), event.getPlayer());
    }

    /**
     * 频道私信信息处理.
     *
     * @param event 事件
     */
    @EventHandler
    public void onChat(PlayerChannelTellEvent event) {
        if (event.isCancelled()) {
            return;
        }
        sendMsg(event.getBcMessageParam(), event.getPlayer());
    }

    /**
     * 发消息
     *
     * @param bcMessageParam 消息参数
     * @param player         玩家
     */
    private static void sendMsg(BcUtil.BcMessageParam bcMessageParam, Player player) {
        // 发送本服消息
        ChatUtil.sendTextMsg(bcMessageParam, true);
        // 发送BC消息
        BcUtil.sendParamForward(player, bcMessageParam);
    }

}