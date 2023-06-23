package cn.handyplus.horn.listener;

import cn.handyplus.horn.RiceHorn;
import cn.handyplus.horn.constants.RiceHornConstants;
import cn.handyplus.horn.util.ChatUtil;
import cn.handyplus.horn.util.ConfigUtil;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.param.BcMessageParam;
import cn.handyplus.lib.util.BcUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * 当玩家聊天时触发这个事件
 * 称号聊天显示
 *
 * @author handy
 */
@HandyListener
public class AsyncPlayerChatEventListener implements Listener {

    /**
     * 当玩家聊天时触发这个事件.
     *
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        boolean chatEnable = ConfigUtil.CONFIG.getBoolean("chat.enable");
        if (!chatEnable) {
            return;
        }
        event.setCancelled(true);
        // 发送本服消息
        ChatUtil.sendMsg(event.getPlayer(), event.getMessage());
        // 发送BC消息
        BcMessageParam param = new BcMessageParam();
        param.setPluginName(RiceHorn.getInstance().getName());
        param.setType(RiceHornConstants.CHAT_TYPE);
        param.setMessage(event.getMessage());
        BcUtil.sendParamForward(event.getPlayer(), param);
    }

}