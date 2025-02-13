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
     * 前缀消息
     */
    private String prefixText;
    /**
     * 前缀hover
     */
    private List<String> prefixHover;
    /**
     * 前缀click
     */
    private String prefixClick;
    /**
     * 玩家消息
     */
    private String playerText;
    /**
     * 玩家hover
     */
    private List<String> playerHover;
    /**
     * 玩家click
     */
    private String playerClick;
    /**
     * 消息前缀
     */
    private String msgText;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 消息hover
     */
    private List<String> msgHover;
    /**
     * 消息click
     */
    private String msgClick;

    /**
     * 聊天消息
     */
    private String message;

    /**
     * 聊天内容颜色权限
     */
    private boolean hasColor;

    /**
     * 物品名称
     */
    private String itemText;

    /**
     * 物品hover
     */
    private List<String> itemHover;

    /**
     * 物品ID
     */
    private Integer itemId;

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
