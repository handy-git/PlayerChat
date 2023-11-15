package cn.handyplus.chat.constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 常量
 *
 * @author handy
 */
public abstract class ChatConstants {

    /**
     * 检查更新网址
     */
    public final static String PLUGIN_VERSION_URL = "https://api.github.com/repos/handy-git/PlayerChat/releases/latest";

    /**
     * 默认渠道
     */
    public final static String DEFAULT = "default";

    /**
     * 消息类型
     */
    public final static String CHAT_TYPE = "RICE_CHAT";

    /**
     * 物品类型
     */
    public final static String ITEM_TYPE = "RICE_ITEM";

    /**
     * 玩家当前渠道
     */
    public static Map<UUID, String> PLAYER_CHAT_CHANNEL = new HashMap<>();

    /**
     * 插件渠道
     * key 渠道 value 插件名
     *
     * @since 1.0.6
     */
    public static Map<String, String> PLUGIN_CHANNEL = new HashMap<>();

    /**
     * 玩家注册的插件渠道
     *
     * @since 1.0.6
     */
    public static Map<UUID, List<String>> PLAYER_PLUGIN_CHANNEL = new HashMap<>();

    /**
     * 玩家聊天冷却
     *
     * @since 1.0.7
     */
    public static Map<UUID, Long> PLAYER_CHAT_TIME = new HashMap<>();

}