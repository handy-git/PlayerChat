package cn.handyplus.horn.listener;

import cn.handyplus.horn.RiceHorn;
import cn.handyplus.horn.constants.RiceHornConstants;
import cn.handyplus.horn.util.ChatUtil;
import cn.handyplus.horn.util.CheckUtil;
import cn.handyplus.horn.util.ConfigUtil;
import cn.handyplus.horn.util.HornUtil;
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
import java.util.Map;

/**
 * 消息处理
 *
 * @author handy
 */
public class HornPluginMessageListener implements PluginMessageListener {

    public HornPluginMessageListener() {
        Bukkit.getMessenger().registerIncomingPluginChannel(RiceHorn.getInstance(), RiceHornConstants.RICE_HORN_CHANNEL, this);
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
        // 获取服务器人数
        this.getPlayerCount(message);
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
        if (RiceHornConstants.CHAT_TYPE.equals(lbMessage.getType()) || RiceHornConstants.ITEM_TYPE.equals(lbMessage.getType())) {
            ChatUtil.sendMsg(player, lbMessage);
            return;
        }
        // 获取喇叭配置
        List<String> serverList = ConfigUtil.CONFIG.getStringList("lb." + lbMessage.getType() + ".server");
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

    /**
     * 获取服务器人数
     *
     * @param message 消息
     */
    private void getPlayerCount(byte[] message) {
        Map<String, Integer> playerCountMap = BcUtil.getPlayerCount(message);
        Integer playerCount = playerCountMap.get(BcUtil.ALL);
        if (playerCount == null) {
            return;
        }
        CheckUtil.check(playerCount);
    }

}