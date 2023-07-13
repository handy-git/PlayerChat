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
     * 注册渠道
     *
     * @param plugin  插件
     * @param channel 渠道名
     */
    public void regChannel(Plugin plugin, String channel) {
    }

    /**
     * 取消注册渠道
     *
     * @param plugin  插件
     * @param channel 渠道名
     */
    public void unRegChannel(Plugin plugin, String channel) {
    }

    /**
     * 注册渠道
     *
     * @param plugin      插件
     * @param channelList 渠道名集合
     */
    public void regChannel(Plugin plugin, List<String> channelList) {
    }

    /**
     * 取消注册渠道
     *
     * @param plugin      插件
     * @param channelList 渠道名集合
     */
    public void unRegChannel(Plugin plugin, List<String> channelList) {
    }

    /**
     * 取消注册渠道
     *
     * @param plugin 插件
     */
    public void unRegChannel(Plugin plugin) {
    }

    /**
     * 设置玩家监听的插件自定义的渠道
     * 只能设置本插件注册的渠道
     *
     * @param plugin  插件
     * @param player  玩家
     * @param channel 渠道
     * @return true成功
     */
    public boolean setPlayerChannel(Plugin plugin, String channel, Player player) {
        return true;
    }

    /**
     * 删除玩家监听的插件自定义的渠道
     * 只能删除本插件注册的渠道
     *
     * @param plugin  插件
     * @param player  玩家
     * @param channel 渠道
     * @return true成功
     */
    public boolean delPlayerChannel(Plugin plugin, String channel, Player player) {
        return true;
    }

    /**
     * 设置玩家正在使用的渠道为默认
     *
     * @param player 玩家
     * @return true成功
     */
    public boolean setPlayerChannelToDefault(Player player) {
        return true;
    }

}