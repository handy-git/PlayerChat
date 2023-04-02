package cn.handyplus.horn.hook;

import cn.handyplus.horn.RiceHorn;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * 变量工具类
 *
 * @author handy
 */
public class PlaceholderApiUtil {

    /**
     * 替换变量
     *
     * @param player 玩家
     * @param str    字符串
     * @return 新字符串
     */
    public static String set(Player player, String str) {
        if (!RiceHorn.USE_PAPI || player == null) {
            return str;
        }
        // 是否包含变量
        if (PlaceholderAPI.containsPlaceholders(str)) {
            return PlaceholderAPI.setPlaceholders(player, str);
        }
        return str;
    }

    /**
     * 替换变量
     *
     * @param offlinePlayer 玩家
     * @param str           字符串
     * @return 新字符串
     */
    public static String set(OfflinePlayer offlinePlayer, String str) {
        if (!RiceHorn.USE_PAPI || offlinePlayer == null) {
            return str;
        }
        // 是否包含变量
        if (PlaceholderAPI.containsPlaceholders(str)) {
            return PlaceholderAPI.setPlaceholders(offlinePlayer, str);
        }
        return str;
    }

    /**
     * 替换变量
     *
     * @param offlinePlayer 玩家
     * @param strList       字符串
     * @return 新字符串
     */
    public static List<String> set(OfflinePlayer offlinePlayer, List<String> strList) {
        if (!RiceHorn.USE_PAPI || offlinePlayer == null) {
            return strList;
        }
        return PlaceholderAPI.setPlaceholders(offlinePlayer, strList);
    }

    /**
     * 替换变量
     *
     * @param playerUuid 玩家UUid
     * @param str        字符串
     * @return 新字符串
     */
    public static String set(UUID playerUuid, String str) {
        if (!RiceHorn.USE_PAPI || playerUuid == null) {
            return str;
        }
        // 是否包含变量
        if (PlaceholderAPI.containsPlaceholders(str)) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUuid);
            return PlaceholderAPI.setPlaceholders(offlinePlayer, str);
        }
        return str;
    }

    /**
     * 替换变量
     *
     * @param player  玩家
     * @param strList 字符串集合
     * @return 新字符串集合
     */
    public static List<String> set(Player player, List<String> strList) {
        if (!RiceHorn.USE_PAPI || player == null) {
            return strList;
        }
        return PlaceholderAPI.setPlaceholders(player, strList);
    }

}