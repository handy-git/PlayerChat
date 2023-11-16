package cn.handyplus.chat.util;

import cn.handyplus.lib.util.HandyConfigUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;

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
        upConfig();
    }

    /**
     * 升级节点处理
     *
     * @since 1.0.7
     */
    public static void upConfig() {
        // 1.0.7 添加聊天频率提醒
        HandyConfigUtil.setPathIsNotContains(LANG_CONFIG, "chatTime", "&7你必须等待 &a${chatTime} &7秒后 才可以继续发言.", null, "languages/" + CONFIG.getString("language") + ".yml");
        LANG_CONFIG = HandyConfigUtil.loadLangConfig(CONFIG.getString("language"), true);

        // 1.0.7 添加聊天频率配置和黑名单配置
        HandyConfigUtil.setPathIsNotContains(CONFIG, "blacklist", Arrays.asList("操", "草", "cao"), Collections.singletonList("黑名单,关键字替换成*"), "config.yml");
        HandyConfigUtil.setPathIsNotContains(CONFIG, "chatTime.default", 0, Collections.singletonList("聊天冷却时间(单位秒)(可无限扩展和修改子节点，权限格式: playerChat.chatTime.vip1)"), "config.yml");
        HandyConfigUtil.setPathIsNotContains(CONFIG, "chatTime.vip1", 0, null, "config.yml");
        HandyConfigUtil.setPathIsNotContains(CONFIG, "chatTime.vip2", 0, null, "config.yml");
        HandyConfigUtil.setPathIsNotContains(CONFIG, "chatTime.vip3", 0, null, "config.yml");
        CONFIG = HandyConfigUtil.load("config.yml");
    }

}