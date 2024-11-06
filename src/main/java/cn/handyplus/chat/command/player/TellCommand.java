package cn.handyplus.chat.command.player;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.listener.AsyncPlayerChatEventListener;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * 私信
 *
 * @author handy
 */
public class TellCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "tell";
    }

    @Override
    public String permission() {
        return "playerChat.tell";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 2, sender, BaseUtil.getMsgNotColor("paramFailureMsg"));
        // 是否为玩家
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getMsgNotColor("noPlayerFailureMsg"));
        // 接收人
        String playerName = args[1];
        AssertUtil.notTrue(player.getName().equals(playerName), sender, BaseUtil.getMsgNotColor("sendTellErrorMsg"));
        // 获取消息
        StringBuilder message = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        // 发送消息
        AsyncPlayerChatEventListener.sendMsg(player, message.toString(), ChatConstants.TELL, playerName);
        HashMap<String, String> map = MapUtil.of("${player}", playerName, "${message}", message.toString());
        MessageUtil.sendMessage(player, BaseUtil.getMsgNotColor("sendTell", map));
    }

}