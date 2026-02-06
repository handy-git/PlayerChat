package cn.handyplus.chat.core;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.core.Pair;
import cn.handyplus.lib.core.StrUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 频道处理
 *
 * @author handy
 * @since 1.0.6
 */
public class ChannelUtil {

    /**
     * 获取开启的频道
     *
     * @param channel 频道
     * @return 开启的频道
     */
    public static String isChannelEnable(String channel) {
        // 默认频道直接返回
        if (ChatConstants.DEFAULT.equals(channel)) {
            return channel;
        }
        // chat自带频道
        boolean chatEnable = ConfigUtil.CHAT_CONFIG.getBoolean("chat." + channel + ".enable");
        if (chatEnable) {
            return channel;
        }
        // 第三方插件频道
        String pluginChannel = ChatConstants.PLUGIN_CHANNEL.get(channel);
        if (StrUtil.isEmpty(pluginChannel)) {
            return null;
        }
        // 第三方插件频道是否启用
        boolean pluginChannelEnable = ConfigUtil.CHAT_CONFIG.getBoolean("chat." + pluginChannel + ".enable");
        if (!pluginChannelEnable) {
            return null;
        }
        return pluginChannel;
    }

    /**
     * 获取频道名
     *
     * @param channel 频道
     * @return channel名称
     */
    public static String getChannelName(String channel) {
        String channelEnable = isChannelEnable(channel);
        return ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".name", channelEnable);
    }

    /**
     * 获取该频道玩家
     *
     * @param channel 频道
     * @return 在频道的玩家
     */
    public static List<Player> getChannelPlayer(String channel) {
        // 默认频道返回全部
        if (ChatConstants.DEFAULT.equals(channel)) {
            return new ArrayList<>(Bukkit.getOnlinePlayers());
        }
        List<Player> playerList = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            // 判断插件自定义频道
            List<String> channelNameList = ChatConstants.PLAYER_PLUGIN_CHANNEL.getOrDefault(onlinePlayer.getUniqueId(), new ArrayList<>());
            if (channelNameList.contains(channel)) {
                playerList.add(onlinePlayer);
                continue;
            }
            // 判断是否存在对应频道权限
            String channelEnable = isChannelEnable(channel);
            if (StrUtil.isNotEmpty(channelEnable) && onlinePlayer.hasPermission(ChatConstants.PLAYER_CHAT_CHAT + channelEnable)) {
                playerList.add(onlinePlayer);
            }
        }
        return playerList;
    }

    /**
     * 获取附近玩家
     *
     * @param channel 频道
     * @param player  玩家
     * @return 玩家列表
     * @since 2.1.0
     */
    public static Pair<Boolean, List<UUID>> getNearbyPlayers(String channel, Player player) {
        // 渠道查找范围
        String range = ConfigUtil.CHAT_CONFIG.getString("chat." + channel + ".range", "");
        if (StrUtil.isEmpty(range)) {
            return Pair.of(false, new ArrayList<>());
        }
        // 附近玩家处理
        List<String> rangeList = StrUtil.strToStrList(range, ",");
        List<Entity> entityList = player.getNearbyEntities(Double.parseDouble(rangeList.get(0)),
                Double.parseDouble(rangeList.get(1)),
                Double.parseDouble(rangeList.get(2)));
        // 过滤玩家列表
        List<UUID> playerList = entityList.stream().filter(e -> EntityType.PLAYER.equals(e.getType())).map(Entity::getUniqueId).collect(Collectors.toList());
        return Pair.of(true, playerList);
    }

}