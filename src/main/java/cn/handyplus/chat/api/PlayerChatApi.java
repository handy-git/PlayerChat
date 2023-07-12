package cn.handyplus.chat.api;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.service.ChatPlayerChannelService;
import cn.handyplus.lib.core.StrUtil;
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
        ChatConstants.PLUGIN_CHANNEL.put(this.getPluginChannelName(plugin, channel), plugin.getName());
    }

    /**
     * 取消注册渠道
     *
     * @param plugin  插件
     * @param channel 渠道名
     */
    public void unRegChannel(Plugin plugin, String channel) {
        ChatConstants.PLUGIN_CHANNEL.remove(this.getPluginChannelName(plugin, channel));
    }

    /**
     * 注册渠道
     *
     * @param plugin      插件
     * @param channelList 渠道名集合
     */
    public void regChannel(Plugin plugin, List<String> channelList) {
        for (String channel : channelList) {
            regChannel(plugin, channel);
        }
    }

    /**
     * 取消注册渠道
     *
     * @param plugin      插件
     * @param channelList 渠道名集合
     */
    public void unRegChannel(Plugin plugin, List<String> channelList) {
        for (String channel : channelList) {
            unRegChannel(plugin, channel);
        }
    }

    /**
     * 取消注册渠道
     *
     * @param plugin 插件
     */
    public void unRegChannel(Plugin plugin) {
        ChatConstants.PLUGIN_CHANNEL.entrySet().removeIf(entry -> entry.getValue().equals(plugin.getName()));
    }

    /**
     * 设置玩家正在使用的渠道
     * 只能设置本插件注册的渠道
     *
     * @param plugin  插件
     * @param player  玩家
     * @param channel 渠道
     * @return true成功
     */
    public boolean setPlayerChannel(Plugin plugin, String channel, Player player) {
        String channelName = getPluginChannelName(plugin, channel);
        if (StrUtil.isEmpty(ChatConstants.PLUGIN_CHANNEL.get(channelName))) {
            return false;
        }
        return ChatPlayerChannelService.getInstance().setChannel(player.getUniqueId(), channelName);
    }

    /**
     * 处理渠道名称
     *
     * @param plugin  插件
     * @param channel 渠道名
     * @return 渠道名称
     */
    private String getPluginChannelName(Plugin plugin, String channel) {
        return plugin.getName() + "_" + channel;
    }

}