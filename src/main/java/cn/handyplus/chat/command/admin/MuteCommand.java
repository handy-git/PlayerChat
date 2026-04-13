package cn.handyplus.chat.command.admin;

import cn.handyplus.chat.api.PlayerChatApi;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.DateUtil;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * 禁言命令
 *
 * @author handy
 */
public class MuteCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "mute";
    }

    @Override
    public String permission() {
        return "playerChat.mute";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 参数校验: /plc mute <玩家名> <时长> (原因)
        AssertUtil.notTrue(args.length < 3, BaseUtil.getLangMsg("muteParamFailureMsg"));
        String playerName = args[1];
        Integer muteTime = DateUtil.parseTime(args[2]);
        AssertUtil.notNull(muteTime, BaseUtil.getLangMsg("timeFormatFailureMsg"));
        String reason = this.getArg(args, 3).orElse(BaseUtil.getLangMsg("muteDefaultReason"));

        OfflinePlayer offlinePlayer = BaseUtil.getOfflinePlayer(playerName);
        AssertUtil.notNull(offlinePlayer, BaseUtil.getLangMsg("playerNotFoundMsg"));

        boolean muted = PlayerChatApi.getInstance().mutePlayer(offlinePlayer, muteTime, reason, sender.getName());
        AssertUtil.isTrue(muted, BaseUtil.getLangMsg("timeFormatFailureMsg"));
        HashMap<String, String> map = MapUtil.of("${player}", playerName, "${reason}", reason, "${time}", muteTime.toString());
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("muteSuccessMsg", map));
        // 通知被禁言玩家
        MessageUtil.sendMessage(offlinePlayer.getUniqueId(), BaseUtil.getLangMsg("mutedNotifyMsg", map));
    }

}
