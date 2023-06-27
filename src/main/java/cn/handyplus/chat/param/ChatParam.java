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
     * 玩家消息
     */
    private String playerText;
    /**
     * 玩家hover
     */
    private List<String> playerHover;
    /**
     * 消息
     */
    private String msgText;
    /**
     * 消息hover
     */
    private List<String> msgHover;

    /**
     * 聊天消息
     */
    private String message;

    /**
     * 物品名称
     */
    private String itemText;

    /**
     * 物品hover
     */
    private List<String> itemHover;

}
