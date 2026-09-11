package cn.handyplus.chat.param;

import cn.handyplus.lib.core.Pair;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * 跨服消息
 *
 * @author handy
 */
@Data
@Builder
public class ChatParam {

    /**
     * 频道
     */
    private String channel;

    /**
     * 子消息
     */
    private List<ChatChildParam> childList;

    /**
     * 聊天消息
     */
    private String message;

    /**
     * 接收人
     *
     * @since 1.1.5
     */
    private String tellPlayerName;

    /**
     * 被@的人
     *
     * @since 1.2.2
     */
    private List<String> mentionedPlayers;

    /**
     * 附近的人
     *
     * @since 2.1.0
     */
    private Pair<Boolean, List<UUID>> nearbyPlayers;

    /**
     * 世界隔离键 格式: 子服标识:世界名
     * 为空时不启用世界隔离, 开启后仅同服同世界的玩家可见
     *
     * @since 3.10.0
     */
    private String worldKey;

    /**
     * 来源
     */
    private String source = "PlayerChat";

}
