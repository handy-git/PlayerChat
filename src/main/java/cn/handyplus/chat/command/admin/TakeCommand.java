package cn.handyplus.chat.command.admin;

import cn.handyplus.chat.enter.ChatPlayerHornEnter;
import cn.handyplus.chat.service.ChatPlayerHornService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Optional;

/**
 * @author handy
 */
public class TakeCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "take";
    }

    @Override
    public String permission() {
        return "playerChat.take";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 4, sender, BaseUtil.getMsgNotColor("paramFailureMsg"));
        String type = args[1];
        String playerName = args[2];
        Integer number = AssertUtil.isNumericToInt(args[3], sender, BaseUtil.getMsgNotColor("amountFailureMsg"));
        OfflinePlayer offlinePlayer = BaseUtil.getOfflinePlayer(playerName);

        Optional<ChatPlayerHornEnter> hornPlayerEnterOpt = ChatPlayerHornService.getInstance().findByUidAndType(offlinePlayer.getUniqueId(), type);
        if (!hornPlayerEnterOpt.isPresent()) {
            ChatPlayerHornEnter hornPlayer = new ChatPlayerHornEnter();
            hornPlayer.setPlayerName(offlinePlayer.getName());
            hornPlayer.setPlayerUuid(offlinePlayer.getUniqueId().toString());
            hornPlayer.setType(type);
            hornPlayer.setNumber(-number);
            ChatPlayerHornService.getInstance().add(hornPlayer);
        } else {
            ChatPlayerHornService.getInstance().subtractNumber(hornPlayerEnterOpt.get().getId(), number);
        }
        MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("takeSucceedMsg"));
    }

}