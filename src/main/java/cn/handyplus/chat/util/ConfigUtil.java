package cn.handyplus.chat.util;

import cn.handyplus.lib.util.HandyConfigUtil;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * 配置
 *
 * @author handy
 */
public class ConfigUtil {
    public static FileConfiguration CONFIG, LANG_CONFIG;
    public static FileConfiguration CHAT_CONFIG, LB_CONFIG;

    /**
     * 加载全部配置
     */
    public static void init() {
        CONFIG = HandyConfigUtil.loadConfig();
        LANG_CONFIG = HandyConfigUtil.loadLangConfig(CONFIG.getString("language"), true);
        CHAT_CONFIG = HandyConfigUtil.load("chat.yml");
        LB_CONFIG = HandyConfigUtil.load("lb.yml");
    }

}