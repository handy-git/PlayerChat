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
     * hover
     */
    private String hoverItem;
    /**
     * click补全
     */
    private String clickSuggest;
    /**
     * click
     */
    private String click;
    /**
     * 打开链接
     */
    private String url;
    /**
     * 复制到聊天栏（与 click 互斥，有 click 时优先执行命令）
     */
    private String copy;
}
