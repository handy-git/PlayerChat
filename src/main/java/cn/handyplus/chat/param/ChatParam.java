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
     * 聊天内容颜色权限
     */
    private boolean hasColor;

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
     * 来源
     */
    private String source = "PlayerChat";

}
