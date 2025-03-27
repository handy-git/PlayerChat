package cn.handyplus.chat.listener;

import cn.handyplus.chat.core.ChatUtil;
import cn.handyplus.chat.event.PlayerChannelChatEvent;
import cn.handyplus.chat.event.PlayerChannelTellEvent;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

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
        // 内容黑名单处理
        if (blackListCheck(event.getOriginalMessage())) {
            MessageUtil.sendMessage(event.getPlayer(), BaseUtil.getMsgNotColor("blacklistMsg"));
            event.setCancelled(true);
            return;
        }
        BcUtil.BcMessageParam bcMessageParam = event.getBcMessageParam();
        // 发送本服消息
        ChatUtil.sendTextMsg(bcMessageParam, true);
        // 发送BC消息
        BcUtil.sendParamForward(event.getPlayer(), bcMessageParam);
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
        // 内容黑名单处理
        if (blackListCheck(event.getOriginalMessage())) {
            MessageUtil.sendMessage(event.getPlayer(), BaseUtil.getMsgNotColor("blacklistMsg"));
            event.setCancelled(true);
            return;
        }
        BcUtil.BcMessageParam bcMessageParam = event.getBcMessageParam();
        // 发送本服消息
        ChatUtil.sendTextMsg(bcMessageParam, true);
        // 发送BC消息
        BcUtil.sendParamForward(event.getPlayer(), bcMessageParam);
    }

    /**
     * 黑名单控制
     *
     * @param message 消息
     * @return true 存在黑名单语言
     */
    private static boolean blackListCheck(String message) {
        List<String> blacklist = BaseConstants.CONFIG.getStringList("blacklist");
        String stripColorMessage = BaseUtil.stripColor(message);
        if (CollUtil.isNotEmpty(blacklist)) {
            for (String blackMsg : blacklist) {
                if (StrUtil.isEmpty(blackMsg)) {
                    continue;
                }
                if (stripColorMessage.contains(blackMsg)) {
                    return true;
                }
            }
        }
        return false;
    }

}