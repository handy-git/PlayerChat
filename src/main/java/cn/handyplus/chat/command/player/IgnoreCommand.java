package cn.handyplus.chat.command.player;

import cn.handyplus.chat.enter.ChatPlayerIgnoreEnter;
import cn.handyplus.chat.service.ChatPlayerIgnoreService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 屏蔽
 *
 * @author handy
 * @since 1.4.3
 */
public class IgnoreCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "ignore";
    }

    @Override
    public String permission() {
        return "playerChat.ignore";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 2, BaseUtil.getMsgNotColor("ignoreParamFailureMsg"));
        // 是否为玩家
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getMsgNotColor("noPlayerFailureMsg"));
        // 禁止屏蔽自己
        AssertUtil.notTrue(args[1].equals(player.getName()), BaseUtil.getMsgNotColor("ignoreSelfFailureMsg"));
        ChatPlayerIgnoreEnter enter = new ChatPlayerIgnoreEnter();
        enter.setPlayerName(player.getName());
        enter.setPlayerUuid(player.getUniqueId());
        enter.setIgnorePlayer(args[1]);
        ChatPlayerIgnoreService.getInstance().setIgnore(enter);
        MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("ignorePlayer", MapUtil.of("${player}", args[1])));
    }

}