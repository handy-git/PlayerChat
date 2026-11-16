package cn.handyplus.chat.listener;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.ChatUtil;
import cn.handyplus.chat.core.HornUtil;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.chat.util.PlayerListUtil;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.DateUtil;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.HandyConfigUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * BC消息处理
 *
 * @author handy
 */
public class ChatPluginMessageListener implements PluginMessageListener {

    private static final ChatPluginMessageListener INSTANCE = new ChatPluginMessageListener();

    public static ChatPluginMessageListener getInstance() {
        return INSTANCE;
    }

    public void register() {
        Bukkit.getMessenger().registerIncomingPluginChannel(PlayerChat.INSTANCE, BcUtil.BUNGEE_CORD_CHANNEL, INSTANCE);
    }

    public void unregister() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(PlayerChat.INSTANCE, BcUtil.BUNGEE_CORD_CHANNEL, INSTANCE);
    }

    /**
     * 处理消息
     *
     * @param channel 频道
     * @param player  玩家
     * @param message 消息
     */
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        // DEBUG消息: 打印收到的通道与报文长度
        MessageUtil.sendConsoleDebugMessage("&e[PlayerChat] 收到BC消息 channel=" + channel + " len=" + message.length);
        // 解析并写回子服标识
        this.saveServerName(message);
        // 自定义消息处理
        String server = BaseConstants.CONFIG.getString("server", ChatConstants.DEFAULT_SERVER);
        // 设置玩家列表
        List<String> playerList = BcUtil.getPlayerList(message);
        if (CollUtil.isNotEmpty(playerList)) {
            PlayerListUtil.replaceOnlinePlayers(playerList);
            MessageUtil.sendConsoleDebugMessage("&e[PlayerChat] 当前BC全服在线玩家列表:" + ChatConstants.PLAYER_LIST);
        }
        Optional<BcUtil.BcMessageParam> paramOptional = BcUtil.getParamByForward(message);
        if (!paramOptional.isPresent()) {
            return;
        }
        BcUtil.BcMessageParam bcMessageParam = paramOptional.get();
        // 判断时间太久的不发送
        long between = DateUtil.between(new Date(bcMessageParam.getTimestamp()), new Date(), ChronoUnit.MINUTES);
        if (between > 1) {
            return;
        }
        // 群组聊天消息
        if (ChatConstants.CHAT_TYPE.equals(bcMessageParam.getType()) || ChatConstants.ITEM_TYPE.equals(bcMessageParam.getType())) {
            ChatUtil.asyncSendMsg(bcMessageParam, false);
            return;
        }
        // 获取喇叭配置
        List<String> serverList = ConfigUtil.LB_CONFIG.getStringList("lb." + bcMessageParam.getType() + ".server");
        if (CollUtil.isEmpty(serverList)) {
            MessageUtil.sendConsoleDebugMessage(bcMessageParam.getType() + "的server配置错误");
            return;
        }
        // 判断是否包含该子服
        if (!serverList.contains(server)) {
            MessageUtil.sendConsoleDebugMessage("&e[PlayerChat] " + server + "子服不发该喇叭消息");
            return;
        }
        // 发送消息
        HornUtil.sendMsg(null, bcMessageParam);
    }

    /**
     * 解析子服标识并写回配置
     *
     * @param message 消息
     * @since 3.10.6
     */
    private void saveServerName(byte[] message) {
        Optional<String> serverNameOptional = BcUtil.getServerName(message);
        if (!serverNameOptional.isPresent()) {
            return;
        }
        String serverName = serverNameOptional.get();
        // 写回配置文件, 之后各处正常读取该配置即可
        HandyConfigUtil.setPath(BaseConstants.CONFIG, "server", serverName, "config.yml");
        MessageUtil.sendConsoleMessage(BaseUtil.getLangMsg("getServerNameMsg", MapUtil.of("${serverName}", serverName)));
    }

}
