package cn.handyplus.chat.command.player;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.MuteUtil;
import cn.handyplus.chat.listener.PlayerChatListener;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

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
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        // 是否为玩家
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        // 检查是否被禁言
        if (MuteUtil.checkMute(player)) {
            return;
        }
        // 接收人
        String playerName = args[1];
        AssertUtil.notTrue(player.getName().equals(playerName), BaseUtil.getLangMsg("sendTellErrorMsg"));
        // 私信玩家是否在线
        Optional<Player> onlinePlayer = BaseUtil.getOnlinePlayer(playerName);
        AssertUtil.isTrue(onlinePlayer.isPresent(), BaseUtil.getLangMsg("playerOfflineMsg", MapUtil.of("${player}", playerName)));
        // 获取消息
        String message = Arrays.stream(args, 2, args.length).collect(Collectors.joining(" "));
        // 发送消息
        PlayerChatListener.sendMsg(player, message, ChatConstants.TELL, playerName);
        HashMap<String, String> map = MapUtil.of("${player}", playerName, "${message}", message);
        String sendTell = BaseUtil.getLangMsg("sendTell", map);
        // 没有颜色代码权限，移除颜色代码
        if (!sender.hasPermission(ChatConstants.CHAT_COLOR)) {
            sendTell = BaseUtil.stripColor(sendTell);
        }
        MessageUtil.sendMessage(player, sendTell);
    }

}