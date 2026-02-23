package cn.handyplus.chat.listener;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.ChannelUtil;
import cn.handyplus.chat.core.ChatUtil;
import cn.handyplus.chat.event.PlayerChannelChatEvent;
import cn.handyplus.chat.event.PlayerChannelTellEvent;
import cn.handyplus.chat.param.ChatParam;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BcUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家聊天监听器
 *
 * @author handy
 */
@HandyListener
public class PlayerChatListener implements Listener {

    /**
     * 聊天信息处理
     *
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        // 取消事件
        event.setCancelled(true);
        // 发送消息
        sendMsg(player, event.getMessage(), ChatUtil.getChannel(player), null);
    }

    /**
     * 发送消息
     *
     * @param player         玩家
     * @param message        消息
     * @param channel        渠道
     * @param tellPlayerName 接收人
     * @since 1.1.5
     */
    public static void sendMsg(Player player, String message, String channel, String tellPlayerName) {
        // 聊天校验处理
        if (ChatUtil.chatCheck(player, message)) {
            return;
        }
        // @处理
        List<String> mentionedPlayers = new ArrayList<>();
        message = ChatUtil.at(mentionedPlayers, message);
        // 参数构建
        BcUtil.BcMessageParam param = new BcUtil.BcMessageParam();
        param.setPluginName(PlayerChat.INSTANCE.getName());
        param.setPlayerName(player.getName());
        param.setTimestamp(System.currentTimeMillis());
        // 构建消息参数
        ChatParam chatParam = ChatUtil.buildChatParam(player, channel);
        if (chatParam == null) {
            return;
        }
        // 添加私信接收人 1.1.5
        chatParam.setTellPlayerName(tellPlayerName);
        // 添加附近的人 2.1.0
        chatParam.setNearbyPlayers(ChannelUtil.getNearbyPlayers(channel, player));
        // 原消息内容
        chatParam.setMessage(message);
        // @玩家处理
        chatParam.setMentionedPlayers(mentionedPlayers);
        // 有权限进行颜色代码处理
        chatParam.setHasColor(player.hasPermission(ChatConstants.CHAT_COLOR));
        chatParam.setSource(param.getPluginName());
        param.setType(ChatConstants.CHAT_TYPE);
        param.setMessage(JsonUtil.toJson(chatParam));
        // 发送事件
        if (StrUtil.isEmpty(tellPlayerName)) {
            Bukkit.getServer().getPluginManager().callEvent(new PlayerChannelChatEvent(player, param));
        } else {
            Bukkit.getServer().getPluginManager().callEvent(new PlayerChannelTellEvent(player, param));
        }
    }

}
