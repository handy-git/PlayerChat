package cn.handyplus.chat.command.admin;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.service.ChatPlayerMuteService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * 解除禁言命令
 *
 * @author handy
 */
public class UnmuteCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "unmute";
    }

    @Override
    public String permission() {
        return "playerChat.unmute";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 参数校验: /plc unmute <玩家名>
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("unmuteParamFailureMsg"));
        String playerName = args[1];

        OfflinePlayer offlinePlayer = BaseUtil.getOfflinePlayer(playerName);
        AssertUtil.notNull(offlinePlayer, BaseUtil.getLangMsg("playerNotFoundMsg"));
        int deleted = ChatPlayerMuteService.getInstance().removeByUuid(offlinePlayer.getUniqueId());
        // 移除缓存
        ChatConstants.PLAYER_MUTE_CACHE.remove(offlinePlayer.getUniqueId());

        Map<String, String> map = MapUtil.of("${player}", playerName);
        String msg = BaseUtil.getLangMsg(deleted > 0 ? "unmuteSuccessMsg" : "unmuteNotFoundMsg", map);
        MessageUtil.sendMessage(sender, msg);
        // 通知被解除禁言玩家
        if (deleted > 0) {
            MessageUtil.sendMessage(offlinePlayer.getUniqueId(), BaseUtil.getLangMsg("unmutedNotifyMsg"));
        }
    }

}
