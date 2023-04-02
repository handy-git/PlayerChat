package cn.handyplus.horn.param;

import lombok.Data;

/**
 * @author handy
 */
@Data
public class LbMessage {

    /**
     * 类型
     */
    private String type;

    /**
     * 消息
     */
    private String message;
}
