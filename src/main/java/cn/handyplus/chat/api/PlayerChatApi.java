package cn.handyplus.chat.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

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
    public native void regChannel(Plugin plugin, String channel);

    /**
     * 取消注册频道
     *
     * @param plugin  插件
     * @param channel 频道名
     */
    public native void unRegChannel(Plugin plugin, String channel);

    /**
     * 注册频道
     *
     * @param plugin      插件
     * @param channelList 频道名集合
     */
    public native void regChannel(Plugin plugin, List<String> channelList);

    /**
     * 取消注册频道
     *
     * @param plugin      插件
     * @param channelList 频道名集合
     */
    public native void unRegChannel(Plugin plugin, List<String> channelList);

    /**
     * 取消注册频道
     *
     * @param plugin 插件
     */
    public native void unRegChannel(Plugin plugin);

    /**
     * 注册玩家监听的插件自定义的频道
     *
     * @param plugin  插件
     * @param player  玩家
     * @param channel 频道
     * @return true成功
     */
    public native boolean regPlayerChannel(Plugin plugin, String channel, Player player);

    /**
     * 取消注册玩家监听的插件自定义的频道
     *
     * @param plugin  插件
     * @param player  玩家
     * @param channel 频道
     * @return true成功
     */
    public native boolean unRegPlayerChannel(Plugin plugin, String channel, Player player);

    /**
     * 设置玩家正在使用的频道
     * 只能设置本插件注册的频道
     *
     * @param plugin  插件
     * @param player  玩家
     * @param channel 频道
     * @return true成功
     */
    public native boolean setPlayerChannel(Plugin plugin, String channel, Player player);

    /**
     * 设置玩家正在使用的频道为默认
     *
     * @param player 玩家
     * @return true成功
     */
    public native boolean setPlayerChannelToDefault(Player player);

    /**
     * 处理频道名称
     *
     * @param plugin  插件
     * @param channel 频道名
     * @return 频道名称
     */
    private native String getPluginChannelName(Plugin plugin, String channel);

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
    public native boolean sendMessage(@NotNull Player player, @NotNull String channel, @NotNull String message, @Nullable String source);

    /**
     * 发送喇叭消息
     *
     * @param player  玩家
     * @param type    类型
     * @param message 消息
     * @since 1.2.7
     */
    public native void sendLb(@NotNull Player player, @NotNull String type, @NotNull String message);

    /**
     * 禁言玩家
     *
     * @param player       玩家
     * @param muteTime     禁言时长(秒)
     * @param reason       禁言原因
     * @param operatorName 操作者名称
     * @return true成功
     * @since 3.3.5
     */
    public native boolean mutePlayer(@NotNull OfflinePlayer player, int muteTime, @Nullable String reason, @Nullable String operatorName);

    /**
     * 禁言玩家
     *
     * @param playerUuid   玩家UUID
     * @param playerName   玩家名称
     * @param muteTime     禁言时长(秒)
     * @param reason       禁言原因
     * @param operatorName 操作者名称
     * @return true成功
     * @since 3.3.5
     */
    public native boolean mutePlayer(@NotNull UUID playerUuid, @Nullable String playerName, int muteTime, @Nullable String reason, @Nullable String operatorName);

    /**
     * 解除禁言
     *
     * @param player 玩家
     * @return true成功
     * @since 3.3.5
     */
    public native boolean unmutePlayer(@NotNull OfflinePlayer player);

    /**
     * 解除禁言
     *
     * @param playerUuid 玩家UUID
     * @return true成功
     * @since 3.3.5
     */
    public native boolean unmutePlayer(@NotNull UUID playerUuid);

}
