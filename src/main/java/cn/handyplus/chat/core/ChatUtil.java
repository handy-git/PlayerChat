package cn.handyplus.chat.core;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.hook.PlaceholderApiUtil;
import cn.handyplus.chat.param.ChatChildParam;
import cn.handyplus.chat.param.ChatParam;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.core.Pair;
import cn.handyplus.lib.core.PatternUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.internal.HandySchedulerUtil;
import cn.handyplus.lib.internal.PlayerSchedulerUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.HandyConfigUtil;
import cn.handyplus.lib.util.HandyPermissionUtil;
import cn.handyplus.lib.util.MessageUtil;
import cn.handyplus.lib.util.RgbTextUtil;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * 聊天解析工具
 *
 * @author handy
 */
public class ChatUtil {

    /**
     * 异步发送消息
     *
     * @param msg          消息内容
     * @param isConsoleMsg 打印消息
     */
    public static void asyncSendMsg(BcUtil.BcMessageParam msg, boolean isConsoleMsg) {
        HandySchedulerUtil.runTaskAsynchronously(() -> sendTextMsg(msg, isConsoleMsg));
    }

    /**
     * 发送消息
     *
     * @param param        消息内容
     * @param isConsoleMsg 打印消息
     */
    public synchronized static void sendTextMsg(BcUtil.BcMessageParam param, boolean isConsoleMsg) {
        String chatParamJson = param.getMessage();
        ChatParam chatParam = JsonUtil.toBean(chatParamJson, ChatParam.class);
        RgbTextUtil rgbTextUtil = buildMsg(chatParam);
        String channel = chatParam.getChannel();
        // 频道是否开启
        if (StrUtil.isEmpty(ChannelUtil.isChannelEnable(channel))) {
            return;
        }
        // 根据频道发送消息
        for (Player onlinePlayer : ChannelUtil.getChannelPlayer(channel)) {
            // 判断是否开启私信
            if (StrUtil.isNotEmpty(chatParam.getTellPlayerName()) && !onlinePlayer.getName().equals(chatParam.getTellPlayerName())) {
                continue;
            }
            // 判断是否开启附近的人
            Pair<Boolean, List<UUID>> nearbyPlayersPair = chatParam.getNearbyPlayers();
            if (nearbyPlayersPair != null && nearbyPlayersPair.getKey()) {
                if (!nearbyPlayersPair.getValue().contains(onlinePlayer.getUniqueId())) {
                    continue;
                }
            }
            // 判断是否开启屏蔽
            List<String> ignoreList = ChatConstants.PLAYER_IGNORE_MAP.get(onlinePlayer.getUniqueId());
            if (CollUtil.isNotEmpty(ignoreList) && ignoreList.contains(param.getPlayerName())) {
                continue;
            }
            rgbTextUtil.send(onlinePlayer);
            // 如果开启艾特，发送消息
            if (ChatConstants.CHAT_TYPE.equals(param.getType()) && ConfigUtil.CHAT_CONFIG.getBoolean("at.enable")) {
                // 获取艾特玩家
                if (CollUtil.isNotEmpty(chatParam.getMentionedPlayers()) && chatParam.getMentionedPlayers().contains(onlinePlayer.getName())) {
                    String sound = ConfigUtil.CHAT_CONFIG.getString("at.sound");
                    playSound(onlinePlayer, sound);
                }
            }
            // 播放频道发言音效
            String channelSound = ConfigUtil.CHAT_CONFIG.getString("chat." + channel + ".sound");
            playSound(onlinePlayer, channelSound);
        }
        // 控制台消息
        if (isConsoleMsg) {
            rgbTextUtil.sendConsole();
        }
    }

    /**
     * 构建消息
     *
     * @param player  玩家
     * @param channel 频道
     * @return 参数
     */
    public static @Nullable ChatParam buildChatParam(@NotNull Player player, @NotNull String channel) {
        // 频道是否开启
        String channelEnable = ChannelUtil.isChannelEnable(channel);
        if (StrUtil.isEmpty(channelEnable)) {
            return null;
        }
        String channelName = ChannelUtil.getChannelName(channel);
        Set<String> keySet = HandyConfigUtil.getKey(ConfigUtil.CHAT_CONFIG, "chat." + channelEnable + ".format");
        List<ChatChildParam> childList = new ArrayList<>();
        for (String key : keySet) {
            String text = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".format." + key + ".text");
            List<String> hover = ConfigUtil.CHAT_CONFIG.getStringList("chat." + channelEnable + ".format." + key + ".hover");
            String click = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".format." + key + ".click");
            String clickSuggest = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".format." + key + ".clickSuggest");
            // 替换变量
            text = replaceStr(player, channelName, text);
            hover = replaceStr(player, channelName, hover);
            click = replaceStr(player, channelName, click);
            clickSuggest = replaceStr(player, channelName, clickSuggest);
            ChatChildParam chatChildParam = ChatChildParam.builder().text(text).hover(hover).click(click).clickSuggest(clickSuggest).build();
            childList.add(chatChildParam);
        }
        // 构建参数
        return ChatParam.builder().channel(channel).childList(childList).build();
    }

    /**
     * 构建消息
     *
     * @param chatParam 入参
     */
    public static @NotNull RgbTextUtil buildMsg(@NotNull ChatParam chatParam) {
        // 加载颜色
        chatParam.setMessage(chatParam.isHasColor() ? chatParam.getMessage() : BaseUtil.stripColor(chatParam.getMessage()));
        for (ChatChildParam chatChildParam : chatParam.getChildList()) {
            String text = StrUtil.replace(chatChildParam.getText(), "message", chatParam.getMessage());
            chatChildParam.setText(text);
            chatChildParam.setHover(chatChildParam.getHover());
        }
        // 构建消息
        List<RgbTextUtil> rgbTextUtilList = new ArrayList<>();
        for (ChatChildParam chatChildParam : chatParam.getChildList()) {
            RgbTextUtil textComponent = RgbTextUtil.getInstance().init(chatChildParam.getText());
            textComponent.addHoverText(CollUtil.listToStr(chatChildParam.getHover(), "\n"));
            textComponent.addClickSuggestCommand(chatChildParam.getClickSuggest());
            textComponent.addClickCommand(chatChildParam.getClick());
            rgbTextUtilList.add(textComponent);
        }
        // 构建消息
        RgbTextUtil first = rgbTextUtilList.get(0);
        for (int i = 1; i < rgbTextUtilList.size(); i++) {
            first.addExtra(rgbTextUtilList.get(i));
        }
        return first;
    }

    /**
     * 解析内部变量
     *
     * @param player      玩家
     * @param channelName 频道名称
     * @param str         内容
     * @return 新内容
     */
    private static String replaceStr(Player player, String channelName, String str) {
        if (StrUtil.isEmpty(str)) {
            return str;
        }
        str = StrUtil.replace(str, "channel", channelName);
        str = StrUtil.replace(str, "player", player.getName());
        str = StrUtil.replace(str, "nickName", ChatConstants.PLAYER_NICK_CACHE.getOrDefault(player.getUniqueId(), player.getName()));
        str = StrUtil.replace(str, "serverName", BaseUtil.replaceChatColor(BaseConstants.CONFIG.getString("serverName")));
        // head组件解析
        str = BaseUtil.headComponent(str, player.getName());
        // 解析 papi 变量
        str = PlaceholderApiUtil.set(player, str);
        return str;
    }

    /**
     * 解析内部变量
     *
     * @param player      玩家
     * @param channelName 频道名称
     * @param strList     内容集合
     * @return 新内容
     */
    private static List<String> replaceStr(Player player, String channelName, List<String> strList) {
        if (CollUtil.isEmpty(strList)) {
            return strList;
        }
        List<String> newStrList = new ArrayList<>();
        for (String str : strList) {
            newStrList.add(replaceStr(player, channelName, str));
        }
        return newStrList;
    }

    /**
     * 处理@人
     *
     * @param mentionedPlayers 被@的人
     * @param message          消息
     * @return 新消息
     * @since 1.0.9
     */
    public static String at(List<String> mentionedPlayers, String message) {
        boolean enable = ConfigUtil.CHAT_CONFIG.getBoolean("at.enable");
        if (!enable) {
            return message;
        }
        List<String> messageList = StrUtil.strToStrList(message, " ");
        for (String name : messageList) {
            if (CollUtil.contains(ChatConstants.PLAYER_LIST, name)) {
                message = message.replaceFirst(name, "@" + name);
            }
        }
        // 提取@的玩家名
        mentionedPlayers.addAll(PatternUtil.extractAtTags(message));
        if (CollUtil.isEmpty(mentionedPlayers)) {
            return message;
        }
        // 将 @玩家名 替换为高亮显示
        boolean keepAt = ConfigUtil.CHAT_CONFIG.getBoolean("at.keepAt", false);
        String atColor = ConfigUtil.CHAT_CONFIG.getString("at.atColor", "&9");
        for (String playerName : mentionedPlayers) {
            message = message.replaceAll("@" + playerName, BaseUtil.replaceChatColor(atColor + (keepAt ? "@" : "") + playerName) + ChatColor.RESET);
        }
        return message;
    }

    /**
     * 播放声音
     *
     * @param player   玩家
     * @param soundStr 声音
     * @since 1.0.9
     */
    private static void playSound(Player player, String soundStr) {
        if (StrUtil.isEmpty(soundStr)) {
            return;
        }
        Optional<Sound> soundOptional = BaseUtil.getSound(soundStr);
        if (!soundOptional.isPresent()) {
            MessageUtil.sendMessage(player, "没有 " + soundStr + " 音效");
            return;
        }
        PlayerSchedulerUtil.playSound(player, soundOptional.get(), 1, 1);
    }

    /**
     * 聊天校验处理
     *
     * @param player  玩家
     * @param message 消息
     * @return true 不满足条件
     */
    public static boolean chatCheck(Player player, String message) {
        // 内容黑名单处理
        if (blackListCheck(message)) {
            MessageUtil.sendMessage(player, BaseUtil.getMsgNotColor("blacklistMsg"));
            return true;
        }
        // 聊天间隔处理
        int chatTime = HandyPermissionUtil.getReverseIntNumber(player, BaseConstants.CONFIG, "chatTime");
        if (ChatConstants.PLAYER_CHAT_TIME.containsKey(player.getUniqueId())) {
            long keepAlive = (System.currentTimeMillis() - ChatConstants.PLAYER_CHAT_TIME.get(player.getUniqueId())) / 1000L;
            if (keepAlive < chatTime) {
                String waitTimeMsg = BaseUtil.getLangMsg("chatTime").replace("${chatTime}", (chatTime - keepAlive) + "");
                MessageUtil.sendMessage(player, waitTimeMsg);
                return true;
            }
        }
        ChatConstants.PLAYER_CHAT_TIME.put(player.getUniqueId(), System.currentTimeMillis());
        return false;
    }

    /**
     * 黑名单控制
     *
     * @param message 消息
     * @return true 存在黑名单语言
     */
    public static boolean blackListCheck(String message) {
        List<String> blacklist = BaseConstants.CONFIG.getStringList("blacklist");
        String stripColorMessage = BaseUtil.stripColor(message);
        if (CollUtil.isNotEmpty(blacklist)) {
            for (String blackMsg : blacklist) {
                if (StrUtil.isEmpty(blackMsg)) {
                    continue;
                }
                if (stripColorMessage.contains(blackMsg)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取所在频道
     *
     * @param player 玩家
     * @return 频道
     */
    public static String getChannel(Player player) {
        String channel = ChatConstants.PLAYER_CHAT_CHANNEL.getOrDefault(player.getUniqueId(), ChatConstants.DEFAULT);
        // 插件频道直接返回
        if (ChatConstants.PLUGIN_CHANNEL.containsKey(channel)) {
            return channel;
        }
        // 是否有对应频道权限 如果没有权限回到默认频道
        if (!ChatConstants.DEFAULT.equals(channel) && !player.hasPermission(ChatConstants.PLAYER_CHAT_USE + channel)) {
            channel = ChatConstants.DEFAULT;
            // 缓存为频道
            ChatConstants.PLAYER_CHAT_CHANNEL.put(player.getUniqueId(), channel);
        }
        // 频道是否开启
        if (StrUtil.isEmpty(ChannelUtil.isChannelEnable(channel))) {
            return ChatConstants.DEFAULT;
        }
        return channel;
    }

}