package cn.handyplus.chat.constants;

import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.chat.util.HornUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.HandyPermissionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * tab提醒
 *
 * @author handy
 */
@Getter
@AllArgsConstructor
public enum TabListEnum {
    /**
     * 第一层提醒
     */
    FIRST(Arrays.asList("reload", "send", "give", "take", "set", "look", "channel"), 0, null, 1),

    LOOK_TWO(null, 1, "look", 2),

    GIVE_THREE(null, 1, "give", 3),
    GIVE_FOUR(Collections.singletonList(BaseUtil.getLangMsg("tabHelp.number")), 1, "give", 4),

    TAKE_THREE(null, 1, "take", 3),
    TAKE_FOUR(Collections.singletonList(BaseUtil.getLangMsg("tabHelp.number")), 1, "take", 4),

    SET_THREE(null, 1, "set", 3),
    SET_FOUR(Collections.singletonList(BaseUtil.getLangMsg("tabHelp.number")), 1, "set", 4),
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
    public static List<String> returnList(String[] args, int argsLength) {
        List<String> completions = new ArrayList<>();
        // 渠道特殊处理
        if (argsLength == 2 && ("channel".equalsIgnoreCase(args[0]))) {
            Map<String, Object> chatChannel = HandyPermissionUtil.getChildMap(ConfigUtil.CHAT_CONFIG, "chat");
            return new ArrayList<>(chatChannel.keySet());
        }

        if (argsLength == 2 && ("give".equalsIgnoreCase(args[0]) || "take".equalsIgnoreCase(args[0]) || "set".equalsIgnoreCase(args[0]))) {
            return HornUtil.getTabTitle();
        }
        for (TabListEnum tabListEnum : TabListEnum.values()) {
            if (tabListEnum.getBefPos() - 1 >= args.length) {
                continue;
            }
            if ((tabListEnum.getBef() == null || tabListEnum.getBef().equalsIgnoreCase(args[tabListEnum.getBefPos() - 1])) && tabListEnum.getNum() == argsLength) {
                completions = tabListEnum.getList();
            }
        }
        return completions;
    }

}
