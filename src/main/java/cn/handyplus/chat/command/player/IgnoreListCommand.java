package cn.handyplus.chat.command.player;

import cn.handyplus.chat.service.ChatPlayerIgnoreService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * 屏蔽列表
 *
 * @author handy
 * @since 1.4.3
 */
public class IgnoreListCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "ignoreList";
    }

    @Override
    public String permission() {
        return "playerChat.ignoreList";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 是否为玩家
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getMsgNotColor("noPlayerFailureMsg"));
        List<String> ignoreList = ChatPlayerIgnoreService.getInstance().findIgnoreByUid(player.getUniqueId());
        if (CollUtil.isEmpty(ignoreList)) {
            MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("ignoreListEmptyMsg"));
            return;
        }
        MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("ignoreListMsg", MapUtil.of("${name}", CollUtil.listToStr(ignoreList))));
    }

}