package cn.handyplus.chat.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
        return plugin.getName() + "_" + channel;
    }

}