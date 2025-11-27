package cn.handyplus.chat.param;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 跨服消息节点内容
 *
 * @author handy
 * @since 2.0.0
 */
@Data
@Builder
public class ChatChildParam {
    /**
     * 消息
     */
    private String text;
    /**
     * hover
     */
    private List<String> hover;
    /**
     * click补全
     */
    private String clickSuggest;
    /**
     * click
     */
    private String click;
    /**
     * 内容
     */
    private String content;
}
