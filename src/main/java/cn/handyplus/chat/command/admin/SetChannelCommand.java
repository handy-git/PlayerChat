package cn.handyplus.chat.command.admin;

import cn.handyplus.chat.command.player.ChannelCommand;
import cn.handyplus.lib.command.HandyTab;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;

/**
 * 设置玩家聊天频道
 *
 * @author handy
 */
public class SetChannelCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "setChannel";
    }

    @Override
    public String permission() {
        return "playerChat.setChannel";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.nextNull()
                .next(context -> ChannelCommand.getChannelList(context.getSender()));
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        String paramFailureMsg = BaseUtil.getLangMsg("setChannelParamFailureMsg");
        String playerName = this.getArg(args, 1, paramFailureMsg);
        String channel = this.getArg(args, 2, paramFailureMsg);
        Optional<Player> playerOptional = BaseUtil.getOnlinePlayer(playerName);
        AssertUtil.isTrue(playerOptional.isPresent(), BaseUtil.getLangMsg("playerOfflineMsg", MapUtil.of("${player}", playerName)));

        Player player = playerOptional.get();
        String channelName = ChannelCommand.setChannel(player, channel);
        MessageUtil.sendMessage(player, BaseUtil.getLangMsg("channelSwitchMsg", MapUtil.of("${channel}", channelName)));
        HashMap<String, String> map = MapUtil.of("${player}", player.getName(), "${channel}", channelName);
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("channelSetSuccessMsg", map));
    }

}
