package cn.handyplus.chat.util;

import org.bukkit.Bukkit;

/**
 * 版本检测工具类
 * 用于检测 Minecraft 服务器版本并提供兼容性判断
 *
 * @author handy
 */
public class VersionUtil {

    /**
     * 服务器版本号
     * 格式：主版本 * 10000 + 次版本 * 100 + 修订版本
     * 例如：1.12.2 -> 11202, 1.16.5 -> 11605, 1.21.11 -> 12111
     */
    private static int version;

    /**
     * 是否已初始化
     */
    private static boolean initialized = false;

    /**
     * 初始化版本信息
     * 应在插件启用时调用
     */
    public static void init() {
        if (initialized) {
            return;
        }
        String serverVersion = Bukkit.getVersion();
        version = parseVersion(serverVersion);
        initialized = true;
    }

    /**
     * 解析版本号
     *
     * @param version 版本字符串
     * @return 数字版本号
     */
    private static int parseVersion(String version) {
        if (version == null || version.isEmpty()) {
            return 0;
        }
        // 提取版本号，如 "git-Paper-123 (MC: 1.21.11)" -> "1.21.11"
        int start = version.indexOf("(MC: ");
        if (start != -1) {
            start += 5;
            int end = version.indexOf(")", start);
            if (end != -1) {
                version = version.substring(start, end);
            }
        }
        // 解析版本号
        try {
            String[] parts = version.split("\\.");
            if (parts.length >= 2) {
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                int patch = parts.length >= 3 ? Integer.parseInt(parts[2].split("-")[0]) : 0;
                return major * 10000 + minor * 100 + patch;
            }
        } catch (NumberFormatException e) {
            // 忽略解析错误
        }
        return 0;
    }

    /**
     * 是否为 1.12.x 及以下版本（旧版）
     *
     * @return true 如果是 1.12.x 及以下
     */
    public static boolean isLegacy() {
        return version < 11300;
    }

    /**
     * 是否为 1.13+ 版本（新版）
     *
     * @return true 如果是 1.13+
     */
    public static boolean isModern() {
        return version >= 11300;
    }

    /**
     * 是否支持 RGB 颜色代码
     * RGB 颜色在 1.16+ 支持
     *
     * @return true 如果支持 RGB
     */
    public static boolean supportsRGB() {
        return version >= 11600;
    }

    /**
     * 是否支持 Adventure API
     * Adventure API 在 1.16.5+ 的 Paper 中开始支持
     *
     * @return true 如果支持 Adventure
     */
    public static boolean supportsAdventure() {
        return version >= 11605;
    }

    /**
     * 获取版本字符串
     *
     * @return 版本字符串
     */
    public static String getVersionString() {
        return Bukkit.getVersion();
    }

    /**
     * 获取数字版本号
     *
     * @return 数字版本号
     */
    public static int getVersion() {
        return version;
    }
}
