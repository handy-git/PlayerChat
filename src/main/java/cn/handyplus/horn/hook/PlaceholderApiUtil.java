package cn.handyplus.horn.hook;

import cn.handyplus.horn.RiceHorn;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

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
            str = PlaceholderAPI.setPlaceholders(player, str);
        }
        // 双重解析,处理变量嵌套变量
        if (PlaceholderAPI.containsPlaceholders(str)) {
            str = PlaceholderAPI.setPlaceholders(player, str);
        }
        return str;
    }

}