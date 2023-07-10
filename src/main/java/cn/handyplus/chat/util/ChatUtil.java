package cn.handyplus.chat.util;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.hook.PlaceholderApiUtil;
import cn.handyplus.chat.param.ChatParam;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.param.BcMessageParam;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import cn.handyplus.lib.util.RgbTextUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

/**
 * 聊天解析工具
 *
 * @author handy
 */
public class ChatUtil {

    /**
     * 解析并发送消息
     *
     * @param msg          消息内容
     * @param isConsoleMsg 打印消息
     */
    public static void sendMsg(BcMessageParam msg, boolean isConsoleMsg) {
        new BukkitRunnable() {
            @Override
            public void run() {
                sendTextMsg(msg, isConsoleMsg);
            }
        }.runTaskAsynchronously(PlayerChat.getInstance());
    }

    /**
     * 解析并发送消息
     *
     * @param param        消息内容
     * @param isConsoleMsg 打印消息
     */
    private synchronized static void sendTextMsg(BcMessageParam param, boolean isConsoleMsg) {
        String chatParamJson = param.getMessage();
        ChatParam chatParam = JsonUtil.toBean(chatParamJson, ChatParam.class);
        BaseComponent[] textComponent = buildMsg(chatParam, param.getType());
        String channel = chatParam.getChannel();
        // 功能是否开启
        boolean chatEnable = ConfigUtil.CHAT_CONFIG.getBoolean("chat." + channel + ".enable");
        if (!chatEnable) {
            return;
        }
        // 根据渠道发送消息
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            String onlinePlayerChannel = ChatConstants.CHANNEL_MAP.getOrDefault(onlinePlayer.getUniqueId(), "default");
            if (!onlinePlayerChannel.equals(channel)) {
                continue;
            }
            MessageUtil.sendMessage(onlinePlayer, textComponent);
        }
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
     * @param channel 渠道
     * @return 参数
     */
    public static ChatParam buildChatParam(Player player, String channel) {
        // 功能是否开启
        boolean chatEnable = ConfigUtil.CHAT_CONFIG.getBoolean("chat." + channel + ".enable");
        if (!chatEnable) {
            return null;
        }
        // 前缀
        String prefixText = ConfigUtil.CHAT_CONFIG.getString("chat." + channel + ".format.prefix.text");
        List<String> prefixHover = ConfigUtil.CHAT_CONFIG.getStringList("chat." + channel + ".format.prefix.hover");
        String prefixClick = ConfigUtil.CHAT_CONFIG.getString("chat." + channel + ".format.prefix.click");
        // 玩家信息
        String playerText = ConfigUtil.CHAT_CONFIG.getString("chat." + channel + ".format.player.text");
        List<String> playerHover = ConfigUtil.CHAT_CONFIG.getStringList("chat." + channel + ".format.player.hover");
        String playerClick = ConfigUtil.CHAT_CONFIG.getString("chat." + channel + ".format.player.click");
        // 消息
        String msgText = ConfigUtil.CHAT_CONFIG.getString("chat." + channel + ".format.msg.text");
        List<String> msgHover = ConfigUtil.CHAT_CONFIG.getStringList("chat." + channel + ".format.msg.hover");
        String msgClick = ConfigUtil.CHAT_CONFIG.getString("chat." + channel + ".format.msg.click");

        // 解析变量
        prefixText = PlaceholderApiUtil.set(player, prefixText);
        prefixHover = PlaceholderApiUtil.set(player, prefixHover);
        prefixClick = PlaceholderApiUtil.set(player, prefixClick);
        playerText = PlaceholderApiUtil.set(player, playerText);
        playerHover = PlaceholderApiUtil.set(player, playerHover);
        playerClick = PlaceholderApiUtil.set(player, playerClick);
        msgText = PlaceholderApiUtil.set(player, msgText);
        msgHover = PlaceholderApiUtil.set(player, msgHover);
        msgClick = PlaceholderApiUtil.set(player, msgClick);

        // 构建参数
        return ChatParam.builder().prefixText(prefixText).prefixHover(prefixHover).prefixClick(prefixClick)
                .playerText(playerText).playerHover(playerHover).playerClick(playerClick)
                .msgText(msgText).msgHover(msgHover).msgClick(msgClick).build();
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
        chatParam.setMessage(chatParam.getHasColor() ? BaseUtil.replaceChatColor(chatParam.getMessage()) : chatParam.getMessage());

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
        }
        // 构建消息
        return prefixTextComponent.addExtra(playerTextComponent.build()).addExtra(msgTextComponent.build()).build();
    }

}