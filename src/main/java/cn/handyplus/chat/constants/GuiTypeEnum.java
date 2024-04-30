package cn.handyplus.chat.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * gui类型
 *
 * @author handy
 */
@Getter
@AllArgsConstructor
public enum GuiTypeEnum {
    /**
     * gui类型
     */
    ITEM("item"),

    ;

    private final String type;

}