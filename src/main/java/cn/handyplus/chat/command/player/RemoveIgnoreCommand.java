package cn.handyplus.chat.command.player;

import cn.handyplus.chat.service.ChatPlayerIgnoreService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 移除屏蔽
 *
 * @author handy
 */
public class RemoveIgnoreCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "removeIgnore";
    }

    @Override
    public String permission() {
        return "playerChat.removeIgnore";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 是否为玩家
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        // 移除忽略
        ChatPlayerIgnoreService.getInstance().removeIgnore(player.getUniqueId(), args[1]);
        // 发送忽略列表
        IgnoreListCommand.sendIgnoreList(sender);
    }

}