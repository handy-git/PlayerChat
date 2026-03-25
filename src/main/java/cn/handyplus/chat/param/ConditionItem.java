package cn.handyplus.chat.param;

import lombok.Builder;
import lombok.Data;

/**
 * 条件项（用于条件前缀节点）
 *
 * @author handy
 * @since 2.x
 */
@Data
@Builder
public class ConditionItem {
    /**
     * 条件类型：perm（权限检查）
     */
    private String type;

    /**
     * 条件值：
     * - perm类型：权限节点，如 "vip6"
     */
    private String value;

    /**
     * 该条件满足时显示的文本
     */
    private String text;

    /**
     * hover（可选）
     */
    private String hover;

    /**
     * click（可选）
     */
    private String click;

    /**
     * clickSuggest（可选）
     */
    private String clickSuggest;
}
