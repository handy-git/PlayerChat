package cn.handyplus.chat.core;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.core.Pair;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 频道处理
 *
 * @author handy
 * @since 1.0.6
 */
public class ChannelUtil {
    private static final Set<String> INVALID_RANGE_WARNING_CACHE = ConcurrentHashMap.newKeySet();

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
        // 频道是否启用
        String channelEnable = isChannelEnable(channel);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            // 判断插件自定义频道
            List<String> channelNameList = ChatConstants.PLAYER_PLUGIN_CHANNEL.getOrDefault(onlinePlayer.getUniqueId(), Collections.emptyList());
            if (channelNameList.contains(channel)) {
                playerList.add(onlinePlayer);
                continue;
            }
            // 判断是否存在对应频道权限
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
        String channelEnable = isChannelEnable(channel);
        if (StrUtil.isEmpty(channelEnable)) {
            return Pair.of(false, new ArrayList<>());
        }
        String range = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".range", "");
        if (StrUtil.isEmpty(range)) {
            return Pair.of(false, new ArrayList<>());
        }
        // 附近玩家处理
        List<String> rangeList = StrUtil.strToStrList(range, ",");
        if (rangeList.size() != 3) {
            return invalidRange(channel, range);
        }
        double x;
        double y;
        double z;
        try {
            x = Double.parseDouble(rangeList.get(0));
            y = Double.parseDouble(rangeList.get(1));
            z = Double.parseDouble(rangeList.get(2));
        } catch (NumberFormatException e) {
            return invalidRange(channel, range);
        }
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z) || x < 0 || y < 0 || z < 0) {
            return invalidRange(channel, range);
        }
        List<Entity> entityList = player.getNearbyEntities(x, y, z);
        // 过滤玩家列表
        List<UUID> playerList = entityList.stream().filter(e -> EntityType.PLAYER.equals(e.getType())).map(Entity::getUniqueId).collect(Collectors.toList());
        playerList.add(player.getUniqueId());
        return Pair.of(true, playerList);
    }

    /**
     * 处理错误的附近频道范围配置
     *
     * @param channel 频道
     * @param range   范围配置
     * @return 未启用附近范围的结果
     */
    private static Pair<Boolean, List<UUID>> invalidRange(String channel, String range) {
        String warningKey = channel + ":" + range;
        if (INVALID_RANGE_WARNING_CACHE.add(warningKey)) {
            MessageUtil.sendConsoleMessage("频道 " + channel + " 的 range 配置错误: " + range + "，格式应为三个非负数字，例如 6,6,6；已按未配置范围处理");
        }
        return Pair.of(false, new ArrayList<>());
    }

}
