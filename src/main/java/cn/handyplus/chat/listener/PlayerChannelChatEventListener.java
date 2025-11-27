package cn.handyplus.chat.listener;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.ChatUtil;
import cn.handyplus.chat.event.PlayerAiChatEvent;
import cn.handyplus.chat.event.PlayerChannelChatEvent;
import cn.handyplus.chat.event.PlayerChannelTellEvent;
import cn.handyplus.deep.seek.api.DeepSeekApi;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.expand.adapter.HandySchedulerUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.MessageUtil;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * 玩家频道聊天
 *
 * @author handy
 */
@HandyListener
public class PlayerChannelChatEventListener implements Listener {

    /**
     * 频道聊天信息处理.
     *
     * @param event 事件
     */
    @EventHandler
    public void onChat(PlayerChannelChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        // 发送Plc消息
        sendMsg(event.getBcMessageParam(), event.getPlayer());
        // 发送Discord消息
        this.sendDiscord(event);
        // AI审核
        this.aiReview(event);
    }

    /**
     * 频道私信信息处理.
     *
     * @param event 事件
     */
    @EventHandler
    public void onChat(PlayerChannelTellEvent event) {
        if (event.isCancelled()) {
            return;
        }
        sendMsg(event.getBcMessageParam(), event.getPlayer());
    }

    /**
     * 发消息
     *
     * @param bcMessageParam 消息参数
     * @param player         玩家
     */
    private static void sendMsg(BcUtil.BcMessageParam bcMessageParam, Player player) {
        // 发送本服消息
        ChatUtil.sendTextMsg(bcMessageParam, true);
        // 发送BC消息
        BcUtil.sendParamForward(player, bcMessageParam);
    }

    /**
     * 发送Discord消息
     *
     * @param event 事件
     */
    private void sendDiscord(PlayerChannelChatEvent event) {
        if (!PlayerChat.USE_DISCORD_SRV) {
            return;
        }
        // 不转发非本插件消息
        if (!PlayerChat.INSTANCE.getName().equals(event.getSource())) {
            return;
        }
        // 根据游戏频道名获取对应的Discord频道
        TextChannel channel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(event.getChannel());
        if (channel != null) {
            DiscordSRV.getPlugin().processChatMessage(event.getPlayer(), event.getOriginalMessage(), event.getChannel(), false, event);
        }
    }

    /**
     * AI审核
     */
    private void aiReview(PlayerChannelChatEvent event) {
        // 是否开启
        if (!PlayerChat.USE_AI || !BaseConstants.CONFIG.getBoolean(ChatConstants.AI_ENABLE)) {
            return;
        }
        // 不处理非本插件消息
        if (!PlayerChat.INSTANCE.getName().equals(event.getSource())) {
            return;
        }
        // 异步 AI 审核并提醒玩家
        HandySchedulerUtil.runTaskAsynchronously(() -> {
            String chat = DeepSeekApi.chat(PlayerChat.INSTANCE.getName(), event.getOriginalMessage());
            if (!chat.contains(ChatConstants.ILLEGAL)) {
                return;
            }
            MessageUtil.sendMessage(event.getPlayer(), chat);
            Bukkit.getServer().getPluginManager().callEvent(new PlayerAiChatEvent(event.getPlayer(), event.getOriginalMessage(), chat));
        });
    }

}