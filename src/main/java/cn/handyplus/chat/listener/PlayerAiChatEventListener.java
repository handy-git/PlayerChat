package cn.handyplus.chat.listener;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.api.PlayerChatApi;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.enter.ChatPlayerAiEnter;
import cn.handyplus.chat.event.PlayerAiChatEvent;
import cn.handyplus.chat.hook.PlaceholderApiUtil;
import cn.handyplus.chat.service.ChatPlayerAiService;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.expand.adapter.HandySchedulerUtil;
import cn.handyplus.lib.expand.adapter.PlayerSchedulerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * AI审核
 *
 * @author handy
 */
@HandyListener
public class PlayerAiChatEventListener implements Listener {

    /**
     * AI审核
     *
     * @param event 事件
     */
    @EventHandler
    public void onEvent(PlayerAiChatEvent event) {
        Player player = event.getPlayer();
        int count = ChatPlayerAiService.getInstance().count(player.getUniqueId());
        boolean votingEnabled = count >= BaseConstants.CONFIG.getInt("ai.chatMaxCount", 3);
        ChatPlayerAiEnter enter = new ChatPlayerAiEnter();
        enter.setPlayerName(player.getName());
        enter.setPlayerUuid(player.getUniqueId());
        enter.setOriginalMessage(enter.getOriginalMessage());
        enter.setAiMessage(enter.getAiMessage());
        enter.setVotingEnabled(votingEnabled);
        enter.setVoteNumber(0);
        enter.setCreateTime(new Date());
        int id = ChatPlayerAiService.getInstance().add(enter);
        if (votingEnabled && id > 0) {
            // 开启投票
            this.enabledVoting(player, id);
        }
    }

    /**
     * 开启投票
     *
     * @param player 玩家
     * @param id     ID
     */
    private void enabledVoting(Player player, Integer id) {
        // 发送消息
        PlayerChatApi.getInstance().sendMessage(player, ChatConstants.AI, "AI", PlayerChat.INSTANCE.getName() + "_" + ChatConstants.AI);
        // 60秒后投票结束
        HandySchedulerUtil.runTaskLaterAsynchronously(() -> {
            Optional<ChatPlayerAiEnter> chatPlayerAiOpt = ChatPlayerAiService.getInstance().findById(id);
            if (!chatPlayerAiOpt.isPresent()) {
                return;
            }
            ChatPlayerAiEnter chatPlayerAi = chatPlayerAiOpt.get();
            int aiVoteMaxNumber = BaseConstants.CONFIG.getInt("ai.voteMaxNumber");
            if (chatPlayerAi.getVoteNumber() >= aiVoteMaxNumber) {
                List<String> commandList = BaseConstants.CONFIG.getStringList("ai.command");
                for (String command : commandList) {
                    command = PlaceholderApiUtil.set(player, command);
                    command = StrUtil.replace(command, "player", player.getName());
                    PlayerSchedulerUtil.syncDispatchCommand(command);
                }
            }
        }, 20 * 60);
    }

}