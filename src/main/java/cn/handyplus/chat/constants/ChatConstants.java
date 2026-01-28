package cn.handyplus.chat.constants;

import cn.handyplus.chat.enter.ChatPlayerMuteEnter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * 常量
 *
 * @author handy
 */
public final class ChatConstants {

    private ChatConstants() {
    }

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
     * 玩家投票记录
     */
    public static final Map<UUID, Integer> PLAYER_VOTE_MAP = new HashMap<>();

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
     * 玩家昵称缓存
     *
     * @since 2.0.5
     */
    public static final Map<UUID, String> PLAYER_CHAT_NICK = new HashMap<>();

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

    /**
     * 玩家昵称缓存
     * key: 玩家UUID, value: 昵称
     *
     * @since 2.0.6
     */
    public static Map<UUID, String> PLAYER_NICK_CACHE = new HashMap<>();
    /**
     * 违规
     *
     * @since 2.0.0
     */
    public static final String ILLEGAL = "违规";

    /**
     * AI审核
     *
     * @since 2.0.0
     */
    public static final String AI_ENABLE = "ai.enable";
    /**
     * AI审核忽略
     *
     * @since 2.0.0
     */
    public static final String AI_IGNORE = "playerChat.ai.ignore";

    /**
     * 昵称权限
     *
     * @since 2.0.5
     */
    public static final String NICK_OTHER = "playerChat.nick.other";

    /**
     * 全部
     *
     * @since 2.1.0
     */
    public static final String ALL = "[ALL]";

    /**
     * 玩家禁言缓存
     * key: 玩家UUID, value: 禁言记录
     */
    public static final Map<UUID, Optional<ChatPlayerMuteEnter>> PLAYER_MUTE_CACHE = new HashMap<>();

    /**
     * 命令别名映射
     * key: 别名, value: 实际命令
     *
     * @since 2.0.6
     */
    public static final Map<String, String> COMMAND_ALIAS_MAP = new HashMap<>();

}