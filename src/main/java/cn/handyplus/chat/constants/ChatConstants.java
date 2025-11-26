package cn.handyplus.chat.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 常量
 *
 * @author handy
 */
public class ChatConstants {

    /**
     * 默认频道
     */
    public final static String DEFAULT = "default";

    /**
     * 私信频道
     */
    public static final String TELL = "tell";

    /**
     * 消息类型
     */
    public final static String CHAT_TYPE = "RICE_CHAT";

    /**
     * 物品类型
     */
    public final static String ITEM_TYPE = "RICE_ITEM";

    /**
     * 玩家当前频道
     */
    public static final Map<UUID, String> PLAYER_CHAT_CHANNEL = new HashMap<>();

    /**
     * 插件频道
     * key 频道 value 插件名
     *
     * @since 1.0.6
     */
    public static final Map<String, String> PLUGIN_CHANNEL = new HashMap<>();

    /**
     * 玩家注册的插件频道
     *
     * @since 1.0.6
     */
    public static final Map<UUID, List<String>> PLAYER_PLUGIN_CHANNEL = new HashMap<>();

    /**
     * 玩家聊天冷却
     *
     * @since 1.0.7
     */
    public static final Map<UUID, Long> PLAYER_CHAT_TIME = new HashMap<>();

    /**
     * 频道使用权限
     *
     * @since 1.1.4
     */
    public static final String PLAYER_CHAT_USE = "playerChat.use.";

    /**
     * 频道查看权限
     *
     * @since 1.1.4
     */
    public static final String PLAYER_CHAT_CHAT = "playerChat.chat.";

    /**
     * 玩家列表
     *
     * @since 1.1.5
     */
    public static List<String> PLAYER_LIST = new ArrayList<>();

    /**
     * 玩家忽略列表
     *
     * @since 1.4.3
     */
    public static Map<UUID, List<String>> PLAYER_IGNORE_MAP = new HashMap<>();

}