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
            String pattern = ConfigUtil.SHORTCUT_CONFIG.getString(key + ".pattern", "");
            String textFilter = ConfigUtil.SHORTCUT_CONFIG.getString(key + ".text-filter");
            String text = ConfigUtil.SHORTCUT_CONFIG.getString(key + ".display.text");
            List<String> hover = ConfigUtil.SHORTCUT_CONFIG.getStringList(key + ".display.hover");
            String click = ConfigUtil.SHORTCUT_CONFIG.getString(key + ".display.click");
            String clickSuggest = ConfigUtil.SHORTCUT_CONFIG.getString(key + ".display.clickSuggest");
            String url = ConfigUtil.SHORTCUT_CONFIG.getString(key + ".display.url");
            ChatChildParam shortcutDisplay = buildShortcutDisplay(stripColorMessage, pattern, textFilter, key, text, hover, click, clickSuggest, url);
            if (shortcutDisplay == null) {
                continue;
            }
            ChatChildParam msgNode = chatParam.getChildList().get(chatParam.getChildList().size() - 1);
            msgNode.setText(ChatUtil.replaceStr(player, channelName, shortcutDisplay.getText()));
            msgNode.setHover(ChatUtil.replaceStr(player, channelName, shortcutDisplay.getHover()));
            msgNode.setClick(ChatUtil.replaceStr(player, channelName, shortcutDisplay.getClick()));
            msgNode.setClickSuggest(ChatUtil.replaceStr(player, channelName, shortcutDisplay.getClickSuggest()));
            msgNode.setUrl(ChatUtil.replaceStr(player, channelName, shortcutDisplay.getUrl()));
            msgNode.setHoverItem(null);
            return;
        }
    }

    /**
     * 构建快捷展示节点
     *
     * @param message      消息
     * @param pattern      整句匹配正则
     * @param textFilter   文本提取正则
     * @param key          节点
     * @param text         展示内容
     * @param hover        悬浮内容
     * @param click        点击执行
     * @param clickSuggest 点击补全
     * @param url          跳转链接
     * @return 快捷展示节点, null 表示未命中
     */
    static ChatChildParam buildShortcutDisplay(String message, String pattern, String textFilter, String key, String text, List<String> hover, String click, String clickSuggest, String url) {
        if (!PatternUtil.isMatch(pattern, message) || StrUtil.isEmpty(text)) {
            return null;
        }
        List<String> textFilterVars = CollUtil.of();
        if (StrUtil.isNotEmpty(textFilter)) {
            textFilterVars = extractRegexVars(message, textFilter, key + ".text-filter");
            if (textFilterVars == null) {
                return null;
            }
        }
        return ChatChildParam.builder()
                .text(replaceTextFilterVar(text, textFilterVars))
                .hover(replaceTextFilterVar(hover, textFilterVars))
                .click(replaceTextFilterVar(click, textFilterVars))
                .clickSuggest(replaceTextFilterVar(clickSuggest, textFilterVars))
                .url(replaceTextFilterVar(url, textFilterVars))
                .build();
    }

    /**
     * 文本过滤变量处理
     *
     * @param str  内容
     * @param vars 变量
     * @return 新内容
     */
    static String replaceTextFilterVar(String str, List<String> vars) {
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
    static List<String> replaceTextFilterVar(List<String> strList, List<String> vars) {
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
     * 在消息中提取第一个命中的正则变量
     *
     * @param message 消息
     * @param regex   正则
     * @param key     节点
     * @return 变量集合, null表示未匹配
     */
    static List<String> extractRegexVars(String message, String regex, String key) {
        if (StrUtil.isEmpty(regex)) {
            return Collections.emptyList();
        }
        try {
            Matcher matcher = Pattern.compile(regex).matcher(message);
            if (!matcher.find()) {
                return null;
            }
            List<String> vars = new ArrayList<>();
            // group(0) 是整体匹配
            vars.add(matcher.group());
            // 从 group(1) 开始是捕获组
            for (int i = 1; i <= matcher.groupCount(); i++) {
                vars.add(matcher.group(i));
            }
            return vars;
        } catch (PatternSyntaxException ex) {
            MessageUtil.sendConsoleDebugMessage("shortcut." + key + " 正则配置错误: " + regex);
            return null;
        }
    }

}
