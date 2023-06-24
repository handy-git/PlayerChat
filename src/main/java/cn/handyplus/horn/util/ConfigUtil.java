package cn.handyplus.horn.util;

import cn.handyplus.horn.constants.RiceHornConstants;
import cn.handyplus.lib.util.HandyConfigUtil;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * 配置
 *
 * @author handy
 */
public class ConfigUtil {
    public static FileConfiguration CONFIG, LANG_CONFIG;

    /**
     * 加载全部配置
     */
    public static void init() {
        CONFIG = HandyConfigUtil.loadConfig();
        LANG_CONFIG = HandyConfigUtil.loadLangConfig(CONFIG.getString("language"), true);
        HandyConfigUtil.loadKey(RiceHornConstants.SECRET_KEY);
    }

}