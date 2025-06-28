package cn.handyplus.chat.constants;

import cn.handyplus.lib.util.BaseUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * tab提醒
 *
 * @author handy
 */
@Getter
@AllArgsConstructor
public enum PlayerTabListEnum {
    /**
     * 第一层提醒
     */
    FIRST(Arrays.asList("channel", "tell"), 0, null, 1),

    CHANNEL_TWO(null, 1, "channel", 2),

    TELL_TWO(null, 1, "tell", 2),
    TELL_THREE(Collections.singletonList(BaseUtil.getLangMsg("tabHelp.message")), 1, "tell", 3),
    ;

    /**
     * 返回的List
     */
    private final List<String> list;
    /**
     * 识别的上个参数的位置
     */
    private final int befPos;
    /**
     * 上个参数的内容
     */
    private final String bef;
    /**
     * 这个参数可以出现的位置
     */
    private final int num;

    /**
     * 获取提醒
     *
     * @param args       参数
     * @param argsLength 参数长度
     * @return 提醒
     */
    public static List<String> returnList(CommandSender sender, String[] args, int argsLength) {
        for (PlayerTabListEnum tabListEnum : PlayerTabListEnum.values()) {
            // 过滤掉参数长度不满足要求的情况
            if (tabListEnum.getBefPos() - 1 >= args.length) {
                continue;
            }
            // 过滤掉前置参数不匹配的情况
            if (tabListEnum.getBef() != null && !tabListEnum.getBef().equalsIgnoreCase(args[tabListEnum.getBefPos() - 1])) {
                continue;
            }
            // 过滤掉参数长度不匹配的情况
            if (tabListEnum.getNum() != argsLength) {
                continue;
            }
            // 频道特殊处理
            if (CHANNEL_TWO.equals(tabListEnum)) {
                return TabListEnum.getChannel(sender);
            }
            return tabListEnum.getList();
        }
        return new ArrayList<>();
    }

}
