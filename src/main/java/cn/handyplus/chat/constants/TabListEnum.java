package cn.handyplus.chat.constants;

import cn.handyplus.chat.core.HornUtil;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.HandyConfigUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    FIRST(Arrays.asList("reload", "send", "give", "take", "set", "look", "channel", "tell", "clearBossBar", "ignore", "ignoreList", "removeIgnore"), 0, null, 1),

    LOOK_TWO(null, 1, "look", 2),

    CHANNEL_TWO(null, 1, "channel", 2),

    GIVE_TWO(null, 1, "give", 2),
    GIVE_THREE(null, 1, "give", 3),
    GIVE_FOUR(Collections.singletonList(BaseUtil.getLangMsg("tabHelp.number")), 1, "give", 4),

    TAKE_TWO(null, 1, "take", 2),
    TAKE_THREE(null, 1, "take", 3),
    TAKE_FOUR(Collections.singletonList(BaseUtil.getLangMsg("tabHelp.number")), 1, "take", 4),

    SET_TWO(null, 1, "set", 2),
    SET_THREE(null, 1, "set", 3),
    SET_FOUR(Collections.singletonList(BaseUtil.getLangMsg("tabHelp.number")), 1, "set", 4),

    TELL_TWO(null, 1, "tell", 2),
    TELL_THREE(Collections.singletonList(BaseUtil.getLangMsg("tabHelp.message")), 1, "tell", 3),

    IGNORE_TWO(null, 1, "ignore", 2),
    REMOVE_IGNORE_TWO(null, 1, "removeIgnore", 2),
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
        for (TabListEnum tabListEnum : TabListEnum.values()) {
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
                return getChannel(null);
            }
            // 喇叭特殊处理
            if (GIVE_TWO.equals(tabListEnum) || TAKE_TWO.equals(tabListEnum) || SET_TWO.equals(tabListEnum)) {
                return HornUtil.getTabTitle();
            }
            return tabListEnum.getList();
        }
        return new ArrayList<>();
    }

    public static List<String> getChannel(CommandSender sender) {
        Set<String> chatChannelKeySet = HandyConfigUtil.getKey(ConfigUtil.CHAT_CONFIG, "chat");
        List<String> chatChannelList = new ArrayList<>(chatChannelKeySet);
        // 过滤私信频道
        chatChannelList = chatChannelList.stream().filter(s -> !"tell".equals(s)).collect(Collectors.toList());
        // 只显示有权限的频道
        if (sender != null) {
            chatChannelList = chatChannelList.stream().filter(s -> sender.hasPermission(ChatConstants.PLAYER_CHAT_USE + s)).collect(Collectors.toList());
        }
        // 过滤插件频道
        List<String> pluginChannelList = ChatConstants.PLUGIN_CHANNEL.values().stream().distinct().collect(Collectors.toList());
        return chatChannelList.stream().filter(s -> !pluginChannelList.contains(s)).collect(Collectors.toList());
    }

}
