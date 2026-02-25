package cn.handyplus.chat.core;

import cn.handyplus.chat.param.ChatChildParam;
import cn.handyplus.chat.param.ChatParam;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.PatternUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 快捷触发工具
 *
 * @author handy
 * @since 3.3.0
 */
public final class ShortcutUtil {

    private ShortcutUtil() {
    }

    /**
     * 快捷键处理
     *
     * @param player    玩家
     * @param channel   频道
     * @param chatParam 参数
     */
    public static void convert(@NotNull Player player, @NotNull String channel, ChatParam chatParam) {
        if (!ConfigUtil.SHORTCUT_CONFIG.getBoolean("enable", false)) {
            return;
        }
        if (chatParam == null || CollUtil.isEmpty(chatParam.getChildList())) {
            return;
        }
        String message = chatParam.getMessage();
        String channelName = ChannelUtil.getChannelName(channel);
        String stripColorMessage = BaseUtil.stripColor(message);
        Set<String> shortcutKeySet = ConfigUtil.SHORTCUT_CONFIG.getKeys(false);
        for (String key : shortcutKeySet) {
            if ("enable".equalsIgnoreCase(key)) {
                continue;
            }
            // 正则匹配
            String pattern = ConfigUtil.SHORTCUT_CONFIG.getString(key + ".pattern", "");
            if (!PatternUtil.isMatch(pattern, stripColorMessage)) {
                continue;
            }
            // 文本过滤匹配
            String textFilter = ConfigUtil.SHORTCUT_CONFIG.getString(key + ".text-filter");
            List<String> textFilterVars = CollUtil.of();
            if (StrUtil.isNotEmpty(textFilter)) {
                textFilterVars = extractRegexVars(stripColorMessage, textFilter, key + ".text-filter");
                if (textFilterVars == null) {
                    continue;
                }
            }
            String text = ConfigUtil.SHORTCUT_CONFIG.getString(key + ".display.text");
            List<String> hover = ConfigUtil.SHORTCUT_CONFIG.getStringList(key + ".display.hover");
            String click = ConfigUtil.SHORTCUT_CONFIG.getString(key + ".display.click");
            String clickSuggest = ConfigUtil.SHORTCUT_CONFIG.getString(key + ".display.clickSuggest");
            if (StrUtil.isEmpty(text)) {
                continue;
            }
            // 处理替换
            ChatChildParam msgNode = chatParam.getChildList().get(chatParam.getChildList().size() - 1);
            text = replaceTextFilterVar(text, textFilterVars);
            hover = replaceTextFilterVar(hover, textFilterVars);
            click = replaceTextFilterVar(click, textFilterVars);
            clickSuggest = replaceTextFilterVar(clickSuggest, textFilterVars);
            msgNode.setText(ChatUtil.replaceStr(player, channelName, text));
            msgNode.setHover(ChatUtil.replaceStr(player, channelName, hover));
            msgNode.setClick(ChatUtil.replaceStr(player, channelName, click));
            msgNode.setClickSuggest(ChatUtil.replaceStr(player, channelName, clickSuggest));
            msgNode.setHoverItem(null);
            return;
        }
    }

    /**
     * 文本过滤变量处理
     *
     * @param str  内容
     * @param vars 变量
     * @return 新内容
     */
    private static String replaceTextFilterVar(String str, List<String> vars) {
        if (StrUtil.isEmpty(str) || CollUtil.isEmpty(vars)) {
            return str;
        }
        String result = str;
        for (int i = 0; i < vars.size(); i++) {
            result = result.replace("{" + i + "}", vars.get(i));
        }
        return result;
    }

    /**
     * 文本过滤变量处理
     *
     * @param strList 内容
     * @param vars    变量
     * @return 新内容
     */
    private static List<String> replaceTextFilterVar(List<String> strList, List<String> vars) {
        if (CollUtil.isEmpty(strList) || CollUtil.isEmpty(vars)) {
            return strList;
        }
        List<String> newStrList = new ArrayList<>();
        for (String str : strList) {
            newStrList.add(replaceTextFilterVar(str, vars));
        }
        return newStrList;
    }

    /**
     * 正则提取变量
     *
     * @param message 消息
     * @param regex   正则
     * @param key     节点
     * @return 变量集合, null表示未匹配
     */
    private static List<String> extractRegexVars(String message, String regex, String key) {
        if (StrUtil.isEmpty(regex)) {
            return Collections.emptyList();
        }
        try {
            Matcher matcher = Pattern.compile(regex).matcher(message);
            if (!matcher.matches()) {
                return null;
            }
            List<String> vars = new ArrayList<>();
            for (int i = 0; i <= matcher.groupCount(); i++) {
                vars.add(matcher.group(i));
            }
            return vars;
        } catch (PatternSyntaxException ex) {
            MessageUtil.sendConsoleDebugMessage("shortcut." + key + " 正则配置错误: " + regex);
            return null;
        }
    }

}
