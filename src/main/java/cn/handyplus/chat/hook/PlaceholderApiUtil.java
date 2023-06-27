package cn.handyplus.chat.hook;

import cn.handyplus.chat.PlayerChat;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.List;

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
        if (!PlayerChat.USE_PAPI || player == null) {
            return str;
        }
        // 是否包含变量
        if (PlaceholderAPI.containsPlaceholders(str)) {
            str = PlaceholderAPI.setPlaceholders(player, str);
        }
        // 双重解析,处理变量嵌套变量
        if (PlaceholderAPI.containsPlaceholders(str)) {
            str = PlaceholderAPI.setPlaceholders(player, str);
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
        if (!PlayerChat.USE_PAPI || player == null) {
            return strList;
        }
        // 解析变量
        strList = PlaceholderAPI.setPlaceholders(player, strList);
        // 双重解析,处理变量嵌套变量
        return PlaceholderAPI.setPlaceholders(player, strList);
    }

}