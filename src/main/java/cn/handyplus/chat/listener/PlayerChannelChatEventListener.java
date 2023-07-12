package cn.handyplus.chat.listener;

import cn.handyplus.chat.event.PlayerChannelChatEvent;
import cn.handyplus.chat.core.ChatUtil;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.param.BcMessageParam;
import cn.handyplus.lib.util.BcUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(PlayerChannelChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        BcMessageParam bcMessageParam = event.getBcMessageParam();
        // 发送本服消息
        ChatUtil.sendMsg(bcMessageParam, true);
        // 发送BC消息
        BcUtil.sendParamForward(event.getPlayer(), bcMessageParam);
    }

}