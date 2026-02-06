package cn.handyplus.chat.command.admin;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.service.ChatPlayerNickService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;

/**
 * 玩家昵称命令
 *
 * @author handy
 */
public class NickCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "nick";
    }

    @Override
    public String permission() {
        return "playerChat.nick";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("paramFailureMsg"));
        // 昵称
        String nickName = args[1];
        // 获取玩家
        String playerName;
        Optional<String> playerNameOpt = this.getArg(args, 2);
        if (playerNameOpt.isPresent() && sender.hasPermission(ChatConstants.NICK_OTHER)) {
            playerName = args[2];
        } else {
            // 是否为玩家
            Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
            playerName = player.getName();
        }
        OfflinePlayer offlinePlayer = BaseUtil.getOfflinePlayer(playerName);
        // 设置昵称
        ChatPlayerNickService.getInstance().setNickName(offlinePlayer.getUniqueId(), offlinePlayer.getName(), nickName);
        HashMap<String, String> map = MapUtil.of("${player}", playerName, "${nickName}", nickName);
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("nickSetSuccessMsg", map));
    }

}