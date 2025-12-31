package cn.handyplus.chat.command.player;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.enter.ChatPlayerAiEnter;
import cn.handyplus.chat.service.ChatPlayerAiService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 投票
 *
 * @author handy
 * @since 2.0.0
 */
public class VoteCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "vote";
    }

    @Override
    public String permission() {
        return "playerChat.vote";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 2, BaseUtil.getMsgNotColor("ignoreParamFailureMsg"));
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getMsgNotColor("noPlayerFailureMsg"));
        // 是否为id
        Integer id = AssertUtil.isPositiveToInt(args[1], BaseUtil.getMsgNotColor("amountFailureMsg"));
        Optional<ChatPlayerAiEnter> chatPlayerAiOpt = ChatPlayerAiService.getInstance().findChatAi(id);
        if (!chatPlayerAiOpt.isPresent()) {
            return;
        }
        String aiVoteMaxNumber = BaseConstants.CONFIG.getString("ai.voteMaxNumber");
        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put("${max}", aiVoteMaxNumber);
        // 是否已投票
        Integer existId = ChatConstants.PLAYER_VOTE_MAP.getOrDefault(player.getUniqueId(), 0);
        if (existId.equals(id)) {
            replaceMap.put("${number}", chatPlayerAiOpt.get().getVoteNumber() + "");
            MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("hasVotedMsg", replaceMap));
            return;
        }
        replaceMap.put("${number}", chatPlayerAiOpt.get().getVoteNumber() + 1 + "");
        // 增加投票
        ChatPlayerAiService.getInstance().addVoteNumber(id);
        ChatConstants.PLAYER_VOTE_MAP.put(player.getUniqueId(), id);
        MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("voteSuccessMsg", replaceMap));
    }

}