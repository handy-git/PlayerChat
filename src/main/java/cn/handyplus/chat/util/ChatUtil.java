package cn.handyplus.chat.util;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.hook.PlaceholderApiUtil;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.param.BcMessageParam;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.TextUtil;
import net.md_5.bungee.api.chat.TextComponent;
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
     * @param player       玩家
     * @param msg          消息内容
     * @param isConsoleMsg 打印消息
     */
    public static void sendMsg(Player player, BcMessageParam msg, boolean isConsoleMsg) {
        new BukkitRunnable() {
            @Override
            public void run() {
                sendTextMsg(player, msg, isConsoleMsg);
            }
        }.runTaskAsynchronously(PlayerChat.getInstance());
    }

    /**
     * 解析并发送消息
     *
     * @param player       玩家
     * @param msg          消息内容
     * @param isConsoleMsg 打印消息
     */
    private synchronized static void sendTextMsg(Player player, BcMessageParam msg, boolean isConsoleMsg) {
        String channel = msg.getChannel();
        // 功能是否开启
        boolean chatEnable = ConfigUtil.CHAT_CONFIG.getBoolean("chat." + channel + ".enable");
        if (!chatEnable) {
            return;
        }
        // 前缀
        String prefixText = ConfigUtil.CHAT_CONFIG.getString("chat." + channel + ".format.prefix.text");
        List<String> prefixHover = ConfigUtil.CHAT_CONFIG.getStringList("chat." + channel + ".format.prefix.hover");
        // 玩家信息
        String playerText = ConfigUtil.CHAT_CONFIG.getString("chat." + channel + ".format.player.text");
        List<String> playerHover = ConfigUtil.CHAT_CONFIG.getStringList("chat." + channel + ".format.player.hover");
        // 消息
        String msgText = ConfigUtil.CHAT_CONFIG.getString("chat." + channel + ".format.msg.text");
        List<String> msgHover = ConfigUtil.CHAT_CONFIG.getStringList("chat." + channel + ".format.msg.hover");

        // 解析变量
        prefixText = PlaceholderApiUtil.set(player, prefixText);
        prefixHover = PlaceholderApiUtil.set(player, prefixHover);
        playerText = PlaceholderApiUtil.set(player, playerText);
        playerHover = PlaceholderApiUtil.set(player, playerHover);
        msgText = PlaceholderApiUtil.set(player, msgText);
        msgHover = PlaceholderApiUtil.set(player, msgHover);

        // 加载rgb颜色
        prefixText = BaseUtil.replaceChatColor(prefixText);
        prefixHover = BaseUtil.replaceChatColor(prefixHover);
        playerText = BaseUtil.replaceChatColor(playerText);
        playerHover = BaseUtil.replaceChatColor(playerHover);
        msgText = BaseUtil.replaceChatColor(msgText);
        msgHover = BaseUtil.replaceChatColor(msgHover);

        // 前缀
        TextUtil prefixTextComponent = TextUtil.getInstance().init(prefixText);
        prefixTextComponent.addHoverText(CollUtil.listToStr(prefixHover, "\n"));
        // 玩家
        TextUtil playerTextComponent = TextUtil.getInstance().init(playerText);
        playerTextComponent.addHoverText(CollUtil.listToStr(playerHover, "\n"));
        // 消息
        TextUtil msgTextComponent = TextUtil.getInstance().init(msgText + msg.getMessage(), false);
        // 聊天处理
        if (ChatConstants.CHAT_TYPE.equals(msg.getType())) {
            msgTextComponent.addHoverText(CollUtil.listToStr(msgHover, "\n"));
        }
        // 物品展示处理
        if (ChatConstants.ITEM_TYPE.equals(msg.getType())) {
            String legacyText = TextUtil.getInstance().init(msg.getMessage()).build().toLegacyText();
            msgTextComponent = TextUtil.getInstance().init(msgText + legacyText);
            msgTextComponent.addHoverText(msg.getHover());
        }
        // 发送消息
        TextComponent textComponent = prefixTextComponent
                .addExtra(playerTextComponent.build())
                .addExtra(msgTextComponent.build()).build();
        // 根据渠道发送消息
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            String onlinePlayerChannel = ChatConstants.CHANNEL_MAP.getOrDefault(onlinePlayer.getUniqueId(), "default");
            if (!onlinePlayerChannel.equals(channel)) {
                continue;
            }
            MessageApi.sendMessage(onlinePlayer, textComponent);
        }
        if (isConsoleMsg) {
            getServer().getConsoleSender().sendMessage(textComponent.toLegacyText());
        }
    }

}