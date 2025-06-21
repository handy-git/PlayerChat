package cn.handyplus.chat.core;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.hook.PlaceholderApiUtil;
import cn.handyplus.chat.param.ChatParam;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.core.PatternUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.expand.adapter.HandySchedulerUtil;
import cn.handyplus.lib.expand.adapter.PlayerSchedulerUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.MessageUtil;
import cn.handyplus.lib.util.RgbTextUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getServer;

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
        BaseComponent[] textComponent = buildMsg(chatParam, param.getType());
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
            MessageUtil.sendMessage(onlinePlayer, textComponent);
            // 如果开启艾特，发送消息
            if (ChatConstants.CHAT_TYPE.equals(param.getType()) && ConfigUtil.CHAT_CONFIG.getBoolean("at.enable")) {
                // 获取艾特玩家
                if (CollUtil.isNotEmpty(chatParam.getMentionedPlayers()) && chatParam.getMentionedPlayers().contains(onlinePlayer.getName())) {
                    String sound = ConfigUtil.CHAT_CONFIG.getString("at.sound");
                    playSound(onlinePlayer, sound);
                }
            }
        }
        // 控制台消息
        if (isConsoleMsg) {
            StringBuilder str = new StringBuilder();
            for (BaseComponent baseComponent : textComponent) {
                str.append(baseComponent.toLegacyText());
            }
            getServer().getConsoleSender().sendMessage(str.toString());
        }
    }

    /**
     * 构建消息
     *
     * @param player  玩家
     * @param channel 频道
     * @param message 消息
     * @return 参数
     */
    public static ChatParam buildChatParam(Player player, String channel, String message) {
        // 频道是否开启
        String channelEnable = ChannelUtil.isChannelEnable(channel);
        if (StrUtil.isEmpty(channelEnable)) {
            return null;
        }
        // 前缀
        String prefixText = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".format.prefix.text");
        List<String> prefixHover = ConfigUtil.CHAT_CONFIG.getStringList("chat." + channelEnable + ".format.prefix.hover");
        String prefixClick = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".format.prefix.click");
        // 玩家信息
        String playerText = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".format.player.text");
        List<String> playerHover = ConfigUtil.CHAT_CONFIG.getStringList("chat." + channelEnable + ".format.player.hover");
        String playerClick = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".format.player.click");
        // 消息
        String msgText = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".format.msg.text");
        List<String> msgHover = ConfigUtil.CHAT_CONFIG.getStringList("chat." + channelEnable + ".format.msg.hover");
        String msgClick = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".format.msg.click");
        String msgContent = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".format.msg.content");

        // 解析内部变量
        String channelName = ChannelUtil.getChannelName(channel);
        prefixText = replaceStr(player, channelName, prefixText);
        prefixHover = replaceStr(player, channelName, prefixHover);
        prefixClick = replaceStr(player, channelName, prefixClick);
        playerText = replaceStr(player, channelName, playerText);
        playerHover = replaceStr(player, channelName, playerHover);
        playerClick = replaceStr(player, channelName, playerClick);
        msgText = replaceStr(player, channelName, msgText);
        msgHover = replaceStr(player, channelName, msgHover);
        msgClick = replaceStr(player, channelName, msgClick);
        msgContent = replaceStr(player, channelName, msgContent, message);

        // 解析PAPI变量
        prefixText = PlaceholderApiUtil.set(player, prefixText);
        prefixHover = PlaceholderApiUtil.set(player, prefixHover);
        prefixClick = PlaceholderApiUtil.set(player, prefixClick);
        playerText = PlaceholderApiUtil.set(player, playerText);
        playerHover = PlaceholderApiUtil.set(player, playerHover);
        playerClick = PlaceholderApiUtil.set(player, playerClick);
        msgText = PlaceholderApiUtil.set(player, msgText);
        msgHover = PlaceholderApiUtil.set(player, msgHover);
        msgClick = PlaceholderApiUtil.set(player, msgClick);
        msgContent = PlaceholderApiUtil.set(player, msgContent);

        // 构建参数
        return ChatParam.builder().prefixText(prefixText).prefixHover(prefixHover).prefixClick(prefixClick)
                .playerText(playerText).playerHover(playerHover).playerClick(playerClick)
                .msgText(msgText).msgHover(msgHover).msgClick(msgClick).msgContent(msgContent).build();
    }

    /**
     * 构建消息
     *
     * @param chatParam 入参
     * @param type      类型
     */
    public static BaseComponent[] buildMsg(ChatParam chatParam, String type) {
        // 加载rgb颜色
        chatParam.setPrefixText(BaseUtil.replaceChatColor(chatParam.getPrefixText()));
        chatParam.setPrefixHover(BaseUtil.replaceChatColor(chatParam.getPrefixHover()));
        chatParam.setPlayerText(BaseUtil.replaceChatColor(chatParam.getPlayerText()));
        chatParam.setPlayerHover(BaseUtil.replaceChatColor(chatParam.getPlayerHover()));
        chatParam.setMsgText(BaseUtil.replaceChatColor(chatParam.getMsgText()));
        chatParam.setMsgHover(BaseUtil.replaceChatColor(chatParam.getMsgHover()));
        chatParam.setMessage(chatParam.isHasColor() ? BaseUtil.replaceChatColor(chatParam.getMessage()) : chatParam.getMessage());
        chatParam.setMsgContent(chatParam.isHasColor() ? BaseUtil.replaceChatColor(chatParam.getMsgContent()) : chatParam.getMsgContent());
        chatParam.setMessage(StrUtil.isNotEmpty(chatParam.getMsgContent()) ? chatParam.getMsgContent() : chatParam.getMessage());

        // 前缀
        RgbTextUtil prefixTextComponent = RgbTextUtil.getInstance().init(chatParam.getPrefixText());
        prefixTextComponent.addHoverText(CollUtil.listToStr(chatParam.getPrefixHover(), "\n"));
        prefixTextComponent.addClickSuggestCommand(chatParam.getPrefixClick());
        // 玩家
        RgbTextUtil playerTextComponent = RgbTextUtil.getInstance().init(chatParam.getPlayerText());
        playerTextComponent.addHoverText(CollUtil.listToStr(chatParam.getPlayerHover(), "\n"));
        playerTextComponent.addClickSuggestCommand(chatParam.getPlayerClick());
        // 消息
        RgbTextUtil msgTextComponent = RgbTextUtil.getInstance().init(chatParam.getMsgText() + chatParam.getMessage(), false);
        // 聊天处理
        if (ChatConstants.CHAT_TYPE.equals(type)) {
            msgTextComponent.addHoverText(CollUtil.listToStr(chatParam.getMsgHover(), "\n"));
            msgTextComponent.addClickSuggestCommand(chatParam.getMsgClick());
        }
        // 物品展示处理
        if (ChatConstants.ITEM_TYPE.equals(type)) {
            msgTextComponent = RgbTextUtil.getInstance().init(chatParam.getMsgText() + chatParam.getItemText());
            String itemHover = CollUtil.listToStr(chatParam.getItemHover(), "\n");
            msgTextComponent.addHoverText(itemHover);
            msgTextComponent.addClickCommand("/plc item " + chatParam.getItemId());
        }
        // 构建消息
        return prefixTextComponent.addExtra(playerTextComponent.build()).addExtra(msgTextComponent.build()).build();
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
        str = StrUtil.replace(str, "serverName", BaseUtil.replaceChatColor(BaseConstants.CONFIG.getString("serverName")));
        return str;
    }

    /**
     * 解析内部变量
     *
     * @param player      玩家
     * @param channelName 频道名称
     * @param str         内容
     * @param message     原消息
     * @return 新内容
     * @since 1.1.9
     */
    private static String replaceStr(Player player, String channelName, String str, String message) {
        if (StrUtil.isEmpty(str)) {
            return str;
        }
        str = StrUtil.replace(str, "channel", channelName);
        str = StrUtil.replace(str, "player", player.getName());
        str = StrUtil.replace(str, "message", message);
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
        for (String playerName : mentionedPlayers) {
            message = message.replaceAll("@" + playerName, ChatColor.BLUE + playerName + ChatColor.RESET);
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

}