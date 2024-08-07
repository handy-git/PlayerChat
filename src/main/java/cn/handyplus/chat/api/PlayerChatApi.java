package cn.handyplus.chat.api;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.enter.ChatPlayerChannelEnter;
import cn.handyplus.chat.service.ChatPlayerChannelService;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public void regChannel(Plugin plugin, String channel) {
        ChatConstants.PLUGIN_CHANNEL.put(this.getPluginChannelName(plugin, channel), plugin.getName());
    }

    /**
     * 取消注册频道
     *
     * @param plugin  插件
     * @param channel 频道名
     */
    public void unRegChannel(Plugin plugin, String channel) {
        // 移除频道
        String pluginChannelName = this.getPluginChannelName(plugin, channel);
        ChatConstants.PLUGIN_CHANNEL.remove(pluginChannelName);
        // 查询是否有使用该频道的
        List<ChatPlayerChannelEnter> channelEnterList = ChatPlayerChannelService.getInstance().findByChannel(pluginChannelName);
        if (CollUtil.isEmpty(channelEnterList)) {
            return;
        }
        // 重新设置频道
        ChatPlayerChannelService.getInstance().setChannel(pluginChannelName, ChatConstants.DEFAULT, true);
        // 缓存频道
        for (ChatPlayerChannelEnter channelEnter : channelEnterList) {
            Optional<Player> playerOptional = BaseUtil.getOnlinePlayer(UUID.fromString(channelEnter.getPlayerUuid()));
            playerOptional.ifPresent(player -> ChatConstants.PLAYER_CHAT_CHANNEL.put(player.getUniqueId(), ChatConstants.DEFAULT));
        }
    }

    /**
     * 注册频道
     *
     * @param plugin      插件
     * @param channelList 频道名集合
     */
    public void regChannel(Plugin plugin, List<String> channelList) {
        for (String channel : channelList) {
            regChannel(plugin, channel);
        }
    }

    /**
     * 取消注册频道
     *
     * @param plugin      插件
     * @param channelList 频道名集合
     */
    public void unRegChannel(Plugin plugin, List<String> channelList) {
        for (String channel : channelList) {
            unRegChannel(plugin, channel);
        }
    }

    /**
     * 取消注册频道
     *
     * @param plugin 插件
     */
    public void unRegChannel(Plugin plugin) {
        ChatConstants.PLUGIN_CHANNEL.entrySet().removeIf(entry -> entry.getValue().equals(plugin.getName()));
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
        String channelName = getPluginChannelName(plugin, channel);
        if (StrUtil.isEmpty(ChatConstants.PLUGIN_CHANNEL.get(channelName))) {
            return false;
        }
        // 设置玩家拥有的插件频道
        List<String> channelNameList = ChatConstants.PLAYER_PLUGIN_CHANNEL.getOrDefault(player.getUniqueId(), new ArrayList<>());
        if (!channelNameList.contains(channelName)) {
            channelNameList.add(channelName);
        }
        ChatConstants.PLAYER_PLUGIN_CHANNEL.put(player.getUniqueId(), channelNameList);
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
        String channelName = getPluginChannelName(plugin, channel);
        if (StrUtil.isEmpty(ChatConstants.PLUGIN_CHANNEL.get(channelName))) {
            return false;
        }
        // 取消玩家频道
        List<String> channelNameList = ChatConstants.PLAYER_PLUGIN_CHANNEL.getOrDefault(player.getUniqueId(), new ArrayList<>());
        channelNameList.remove(channelName);
        ChatConstants.PLAYER_PLUGIN_CHANNEL.put(player.getUniqueId(), channelNameList);
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
        String channelName = getPluginChannelName(plugin, channel);
        if (StrUtil.isEmpty(ChatConstants.PLUGIN_CHANNEL.get(channelName))) {
            return false;
        }
        return ChatPlayerChannelService.getInstance().setChannel(player.getUniqueId(), channelName);
    }

    /**
     * 设置玩家正在使用的频道为默认
     *
     * @param player 玩家
     * @return true成功
     */
    public boolean setPlayerChannelToDefault(Player player) {
        return ChatPlayerChannelService.getInstance().setChannel(player.getUniqueId(), ChatConstants.DEFAULT);
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