package cn.handyplus.chat.listener;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.util.ChatUtil;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.chat.util.HornUtil;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.DateUtil;
import cn.handyplus.lib.param.BcMessageParam;
import cn.handyplus.lib.util.BcUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * 消息处理
 *
 * @author handy
 */
public class HornPluginMessageListener implements PluginMessageListener {

    public HornPluginMessageListener() {
        Bukkit.getMessenger().registerIncomingPluginChannel(PlayerChat.getInstance(), ChatConstants.RICE_HORN_CHANNEL, this);
    }

    /**
     * 处理消息
     *
     * @param channel 渠道
     * @param player  玩家
     * @param message 消息
     */
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        // 自定义消息处理
        String server = ConfigUtil.CONFIG.getString("server");
        MessageApi.sendConsoleDebugMessage("子服:" + server + "收到消息");
        BcMessageParam lbMessage = BcUtil.getParamByForward(message);
        if (lbMessage == null) {
            return;
        }
        // 判断时间太久的不发送
        long between = DateUtil.between(lbMessage.getSendTime(), new Date(), ChronoUnit.MINUTES);
        if (between > 1) {
            return;
        }
        // 群组聊天消息
        if (ChatConstants.CHAT_TYPE.equals(lbMessage.getType()) || ChatConstants.ITEM_TYPE.equals(lbMessage.getType())) {
            ChatUtil.sendMsg(player, lbMessage, false);
            return;
        }
        // 获取喇叭配置
        List<String> serverList = ConfigUtil.LB_CONFIG.getStringList("lb." + lbMessage.getType() + ".server");
        if (CollUtil.isEmpty(serverList)) {
            MessageApi.sendConsoleDebugMessage(lbMessage.getType() + "的server配置错误");
            return;
        }
        // 判断是否包含该子服
        if (!serverList.contains(server)) {
            MessageApi.sendConsoleDebugMessage(server + "子服不发消息");
            return;
        }
        // 发送消息
        HornUtil.sendMsg(player, lbMessage.getType(), lbMessage.getMessage());
    }

}