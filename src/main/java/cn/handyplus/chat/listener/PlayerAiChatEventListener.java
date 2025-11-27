package cn.handyplus.chat.listener;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.ChatUtil;
import cn.handyplus.chat.enter.ChatPlayerAiEnter;
import cn.handyplus.chat.event.PlayerAiChatEvent;
import cn.handyplus.chat.event.PlayerChannelChatEvent;
import cn.handyplus.chat.hook.PlaceholderApiUtil;
import cn.handyplus.chat.param.ChatChildParam;
import cn.handyplus.chat.param.ChatParam;
import cn.handyplus.chat.service.ChatPlayerAiService;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.expand.adapter.HandySchedulerUtil;
import cn.handyplus.lib.expand.adapter.PlayerSchedulerUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
import org.bukkit.Bukkit;
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
        // 发送投票消息
        this.sendVoteMsg(player, id);

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

    /**
     * 发送投票消息
     *
     * @param player 玩家
     * @param id     ID
     */
    private void sendVoteMsg(Player player, Integer id) {
        BcUtil.BcMessageParam param = new BcUtil.BcMessageParam();
        param.setPluginName(PlayerChat.INSTANCE.getName());
        param.setPlayerName(player.getName());
        param.setTimestamp(System.currentTimeMillis());
        // 构建消息参数
        ChatParam chatParam = ChatUtil.buildChatParam(player, ChatConstants.DEFAULT);
        if (chatParam == null) {
            return;
        }
        String aiText = BaseUtil.getMsgNotColor("aiText");
        // 给予展示属性
        ChatChildParam chatChildParam = chatParam.getChildList().get(chatParam.getChildList().size() - 1);
        chatChildParam.setText(PlaceholderApiUtil.set(player, aiText));
        chatChildParam.setClick("/plc vote " + id);
        param.setMessage(JsonUtil.toJson(chatParam));
        param.setType(ChatConstants.ITEM_TYPE);
        // 发送事件
        Bukkit.getServer().getPluginManager().callEvent(new PlayerChannelChatEvent(player, param));
    }

}