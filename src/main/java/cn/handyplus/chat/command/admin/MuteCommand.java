package cn.handyplus.chat.command.admin;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.enter.ChatPlayerMuteEnter;
import cn.handyplus.chat.service.ChatPlayerMuteService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.DateUtil;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Calendar;
import java.util.Date;
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
        AssertUtil.notTrue(args.length < 3, BaseUtil.getMsgNotColor("muteParamFailureMsg"));
        String playerName = args[1];
        Integer muteTime = DateUtil.parseTime(args[2]);
        AssertUtil.notNull(muteTime, BaseUtil.getMsgNotColor("timeFormatFailureMsg"));
        String reason = this.getArg(args, 3).orElse(BaseUtil.getMsgNotColor("muteDefaultReason"));

        OfflinePlayer offlinePlayer = BaseUtil.getOfflinePlayer(playerName);
        AssertUtil.notNull(offlinePlayer, BaseUtil.getMsgNotColor("playerNotFoundMsg"));

        // 删除旧的禁言记录
        ChatPlayerMuteService.getInstance().removeByUuid(offlinePlayer.getUniqueId());
        // 移除缓存
        ChatConstants.PLAYER_MUTE_CACHE.remove(offlinePlayer.getUniqueId());

        // 创建新的禁言记录
        ChatPlayerMuteEnter muteEnter = new ChatPlayerMuteEnter();
        muteEnter.setPlayerName(offlinePlayer.getName());
        muteEnter.setPlayerUuid(offlinePlayer.getUniqueId());
        muteEnter.setReason(reason);
        muteEnter.setOperatorName(sender.getName());
        muteEnter.setMuteTime(muteTime);
        muteEnter.setCreateTime(new Date());
        muteEnter.setExpireTime(DateUtil.offset(new Date(), Calendar.SECOND, muteTime));
        ChatPlayerMuteService.getInstance().add(muteEnter);
        HashMap<String, String> map = MapUtil.of("${player}", playerName, "${reason}", reason, "${time}", muteTime.toString());
        MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("muteSuccessMsg", map));
    }

}
