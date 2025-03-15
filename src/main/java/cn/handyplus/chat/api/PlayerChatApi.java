package cn.handyplus.chat.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * API
 *
 * @author handy
 * @since 1.0.6
 */
public class PlayerChatApi {
    private PlayerChatApi() {
    }

    private static class SingletonHolder {
        private static final PlayerChatApi INSTANCE = new PlayerChatApi();
    }

    public static PlayerChatApi getInstance() {
        return PlayerChatApi.SingletonHolder.INSTANCE;
    }

    /**
     * 注册频道
     *
     * @param plugin  插件
     * @param channel 频道名
     */
    public void regChannel(Plugin plugin, String channel) {
    }

    /**
     * 取消注册频道
     *
     * @param plugin  插件
     * @param channel 频道名
     */
    public void unRegChannel(Plugin plugin, String channel) {
    }

    /**
     * 注册频道
     *
     * @param plugin      插件
     * @param channelList 频道名集合
     */
    public void regChannel(Plugin plugin, List<String> channelList) {
    }

    /**
     * 取消注册频道
     *
     * @param plugin      插件
     * @param channelList 频道名集合
     */
    public void unRegChannel(Plugin plugin, List<String> channelList) {
    }

    /**
     * 取消注册频道
     *
     * @param plugin 插件
     */
    public void unRegChannel(Plugin plugin) {
    }

    /**
     * 注册玩家监听的插件自定义的频道
     *
     * @param plugin  插件
     * @param player  玩家
     * @param channel 频道
     * @return true成功
     */
    public boolean regPlayerChannel(Plugin plugin, String channel, Player player) {
        return true;
    }

    /**
     * 取消注册玩家监听的插件自定义的频道
     *
     * @param plugin  插件
     * @param player  玩家
     * @param channel 频道
     * @return true成功
     */
    public boolean unRegPlayerChannel(Plugin plugin, String channel, Player player) {
        return true;
    }

    /**
     * 设置玩家正在使用的频道
     * 只能设置本插件注册的频道
     *
     * @param plugin  插件
     * @param player  玩家
     * @param channel 频道
     * @return true成功
     */
    public boolean setPlayerChannel(Plugin plugin, String channel, Player player) {
        return true;
    }

    /**
     * 设置玩家正在使用的频道为默认
     *
     * @param player 玩家
     * @return true成功
     */
    public boolean setPlayerChannelToDefault(Player player) {
        return true;
    }

    /**
     * 处理频道名称
     *
     * @param plugin  插件
     * @param channel 频道名
     * @return 频道名称
     */
    private String getPluginChannelName(Plugin plugin, String channel) {
        return "";
    }

    /**
     * 发送消息
     *
     * @param player  发送人
     * @param channel 渠道
     * @param message 消息内容
     * @param source  来源
     * @return true成功
     * @since 1.2.4
     */
    public boolean sendMessage(@NotNull Player player, @NotNull String channel, @NotNull String message, @NotNull String source) {
        // @处理
        List<String> mentionedPlayers = new ArrayList<>();
        message = ChatUtil.at(mentionedPlayers, message);
        // 参数构建
        BcUtil.BcMessageParam param = new BcUtil.BcMessageParam();
        param.setPluginName(PlayerChat.INSTANCE.getName());
        param.setPlayerName(player.getName());
        param.setTimestamp(System.currentTimeMillis());
        // 构建消息参数
        ChatParam chatParam = ChatParam.builder().msgContent(message).build();
        // 原消息内容
        chatParam.setMessage(message);
        // @玩家处理
        chatParam.setMentionedPlayers(mentionedPlayers);
        chatParam.setHasColor(true);
        chatParam.setChannel(channel);
        chatParam.setSource(source);
        param.setType(ChatConstants.CHAT_TYPE);
        param.setMessage(JsonUtil.toJson(chatParam));
        // 发送事件
        Bukkit.getServer().getPluginManager().callEvent(new PlayerChannelChatEvent(player, param));
        return true;
    }

    /**
     * 发送喇叭消息
     * @param player 玩家
     * @param type 类型
     * @param message 消息
     * @since 1.2.7
     */
    public void sendLb(@NotNull Player player, @NotNull String type, @NotNull String message) {
        BcUtil.BcMessageParam param = new BcUtil.BcMessageParam();
        param.setPluginName(PlayerChat.INSTANCE.getName());
        param.setType(type);
        param.setMessage(message);
        param.setTimestamp(System.currentTimeMillis());
        param.setPlayerName(player.getName());
        BcUtil.sendParamForward(player, param);
        // 发送消息
        HornUtil.sendMsg(player, param);
    }

}