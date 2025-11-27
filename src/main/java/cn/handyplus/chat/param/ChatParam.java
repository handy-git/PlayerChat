package cn.handyplus.chat.param;

import lombok.Builder;
import lombok.Data;

import java.util.List;

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
     * 来源
     */
    private String source = "PlayerChat";

}
