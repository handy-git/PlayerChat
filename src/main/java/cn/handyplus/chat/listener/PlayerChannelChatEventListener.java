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
import cn.handyplus.lib.constants.HookPluginEnum;
import cn.handyplus.lib.internal.HandySchedulerUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.HookPluginUtil;
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
        if (!BaseConstants.CONFIG.getBoolean(ChatConstants.AI_ENABLE) || !HookPluginUtil.hook(HookPluginEnum.DEEP_SEEK)) {
            return;
        }
        // 不处理非本插件消息
        if (!PlayerChat.INSTANCE.getName().equals(event.getSource())) {
            return;
        }
        // 玩家有忽略权限
        if (event.getPlayer().hasPermission(ChatConstants.AI_IGNORE)) {
            return;
        }
        // 异步 AI 审核并提醒玩家
        HandySchedulerUtil.runTaskAsynchronously(() -> {
            String originalMessage = BaseUtil.stripColor(event.getOriginalMessage());
            String chat = DeepSeekApi.chat(PlayerChat.INSTANCE.getName(), originalMessage);
            if (!chat.contains(ChatConstants.ILLEGAL)) {
                return;
            }
            MessageUtil.sendConsoleDebugMessage("AI审核结果:" + chat);
            MessageUtil.sendMessage(event.getPlayer(), BaseUtil.getLangMsg("aiTip"));
            Bukkit.getServer().getPluginManager().callEvent(new PlayerAiChatEvent(event.getPlayer(), originalMessage, chat));
        });
    }

}