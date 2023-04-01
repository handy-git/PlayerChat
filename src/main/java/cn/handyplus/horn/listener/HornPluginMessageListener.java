package cn.handyplus.horn.listener;

import cn.handyplus.horn.RiceHorn;
import cn.handyplus.horn.constants.RiceHornConstants;
import cn.handyplus.horn.param.LbMessage;
import cn.handyplus.horn.util.ConfigUtil;
import cn.handyplus.horn.util.HornUtil;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.util.BcUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.List;

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
        String server = ConfigUtil.CONFIG.getString("server");
        MessageApi.sendConsoleMessage("子服:" + server + "收到消息");
        String json = BcUtil.getContentByForward(message);
        MessageApi.sendConsoleMessage("消息内容为:" + json);
        LbMessage lbMessage = JsonUtil.toBean(json, LbMessage.class);
        // 获取喇叭配置
        List<String> serverList = ConfigUtil.CONFIG.getStringList("lb." + lbMessage.getType() + ".server");
        if (CollUtil.isEmpty(serverList)) {
            MessageApi.sendConsoleMessage(lbMessage.getType() + "的server配置错误");
        }
        // 判断是否包含该子服
        if (!serverList.contains(server)) {
            MessageApi.sendConsoleMessage(server + "子服不发消息");
            return;
        }
        // 发送消息
        HornUtil.sendMsg(lbMessage.getType(), lbMessage.getMessage());
    }

}
