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
     * 渠道
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
     * 消息
     */
    private String msgText;
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

}
