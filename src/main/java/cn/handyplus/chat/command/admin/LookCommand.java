package cn.handyplus.chat.command.admin;

import cn.handyplus.chat.enter.ChatPlayerHornEnter;
import cn.handyplus.chat.service.ChatPlayerHornService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author handy
 */
public class LookCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "look";
    }

    @Override
    public String permission() {
        return "playerChat.look";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 2, BaseUtil.getMsgNotColor("paramFailureMsg"));
        String playerName = args[1];
        OfflinePlayer offlinePlayer = BaseUtil.getOfflinePlayer(playerName);

        List<ChatPlayerHornEnter> hornPlayerList = ChatPlayerHornService.getInstance().findByUid(offlinePlayer.getUniqueId());
        if (CollUtil.isNotEmpty(hornPlayerList)) {
            Map<String, Integer> hornPlayerMap = hornPlayerList.stream().collect(Collectors.toMap(ChatPlayerHornEnter::getType, ChatPlayerHornEnter::getNumber));
            for (String type : hornPlayerMap.keySet()) {
                MessageUtil.sendMessage(sender, playerName + "拥有 " + type + " 喇叭:" + hornPlayerMap.get(type));
            }
        } else {
            MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("noLook", "").replace("${player}", playerName));
        }
    }

}