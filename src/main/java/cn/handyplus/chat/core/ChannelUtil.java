package cn.handyplus.chat.core;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * 渠道处理
 *
 * @author handy
 * @since 1.0.6
 */
public class ChannelUtil {

    /**
     * 获取渠道
     *
     * @param channel 渠道
     * @return true开启
     */
    public static String isChannelEnable(String channel) {
        // chat自带渠道
        boolean chatEnable = ConfigUtil.CHAT_CONFIG.getBoolean("chat." + channel + ".enable");
        if (chatEnable) {
            return channel;
        }
        // 第三方插件渠道
        String pluginChannel = ChatConstants.PLUGIN_CHANNEL.get(channel);
        if (StrUtil.isEmpty(pluginChannel)) {
            return null;
        }
        // 第三方插件渠道是否启用
        boolean pluginChannelEnable = ConfigUtil.CHAT_CONFIG.getBoolean("chat." + pluginChannel + ".enable");
        if (!pluginChannelEnable) {
            return null;
        }
        return pluginChannel;
    }

    /**
     * 获取渠道名
     *
     * @param channel 渠道
     * @return channel名称
     */
    public static String getChannelName(String channel) {
        String channelEnable = isChannelEnable(channel);
        String pluginChannel = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".name", channelEnable);
        return BaseUtil.replaceChatColor(pluginChannel);
    }

    /**
     * 获取该渠道玩家
     *
     * @param channel 渠道
     * @return 在渠道的玩家
     */
    public static List<Player> getChannelPlayer(String channel) {
        // 默认渠道返回全部
        if (ChatConstants.DEFAULT.equals(channel)) {
            return new ArrayList<>(Bukkit.getOnlinePlayers());
        }
        List<Player> playerList = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            // 判断插件自定义渠道
            List<String> channelNameList = ChatConstants.PLAYER_PLUGIN_CHANNEL.getOrDefault(onlinePlayer.getUniqueId(), new ArrayList<>());
            if (channelNameList.contains(channel)) {
                playerList.add(onlinePlayer);
                continue;
            }
            // 判断是否存在对应渠道权限
            if (onlinePlayer.hasPermission("playerChat.chat." + isChannelEnable(channel))) {
                playerList.add(onlinePlayer);
            }
        }
        return playerList;
    }

}