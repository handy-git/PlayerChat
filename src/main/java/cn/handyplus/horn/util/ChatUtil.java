package cn.handyplus.horn.util;

import cn.handyplus.horn.RiceHorn;
import cn.handyplus.horn.hook.PlaceholderApiUtil;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.TextUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * 聊天解析工具
 *
 * @author handy
 */
public class ChatUtil {

    /**
     * 解析并发送消息
     *
     * @param player 玩家
     * @param msg    消息内容
     */
    public static void sendMsg(Player player, String msg) {
        new BukkitRunnable() {
            @Override
            public void run() {
                sendTextMsg(player, msg);
            }
        }.runTaskAsynchronously(RiceHorn.getInstance());
    }

    /**
     * 解析并发送消息
     *
     * @param player 玩家
     * @param msg    消息内容
     */
    private synchronized static void sendTextMsg(Player player, String msg) {
        // 功能是否开启
        boolean chatEnable = ConfigUtil.CONFIG.getBoolean("chat.enable");
        if (!chatEnable) {
            return;
        }
        // 前缀
        String prefixText = ConfigUtil.CONFIG.getString("chat.format.prefix.text");
        List<String> prefixHover = ConfigUtil.CONFIG.getStringList("chat.format.prefix.hover");
        // 玩家信息
        String playerText = ConfigUtil.CONFIG.getString("chat.format.player.text");
        List<String> playerHover = ConfigUtil.CONFIG.getStringList("chat.format.player.hover");
        // 消息
        String msgText = ConfigUtil.CONFIG.getString("chat.format.msg.text");
        List<String> msgHover = ConfigUtil.CONFIG.getStringList("chat.format.msg.hover");

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
        TextUtil msgTextComponent = TextUtil.getInstance().init(msgText + msg);
        msgTextComponent.addHoverText(CollUtil.listToStr(msgHover, "\n"));
        // 发送消息
        TextComponent textComponent = prefixTextComponent
                .addExtra(playerTextComponent.build())
                .addExtra(msgTextComponent.build()).build();
        MessageApi.sendAllMessage(textComponent);
    }

}