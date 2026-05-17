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
        // 参数是否正常
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("ignoreParamFailureMsg"));
        // 是否为玩家
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        // 移除忽略
        String ignorePlayer = args[1];
        boolean ignore = this.isIgnore(args);
        if (ignore) {
            ChatPlayerIgnoreService.getInstance().removeIgnore(player.getUniqueId(), ignorePlayer);
        } else {
            ChatPlayerIgnoreService.getInstance().removeWhite(player.getUniqueId(), ignorePlayer);
        }
        // 发送忽略列表
        IgnoreListCommand.sendIgnoreList(sender);
    }

    /**
     * 是否移除屏蔽列表
     *
     * @param args 命令参数
     * @return true 移除屏蔽列表
     */
    private boolean isIgnore(String[] args) {
        String ignore = this.getArg(args, 2).orElse("true");
        AssertUtil.notTrue(!"true".equalsIgnoreCase(ignore) && !"false".equalsIgnoreCase(ignore), BaseUtil.getLangMsg("ignoreParamFailureMsg"));
        return Boolean.parseBoolean(ignore);
    }

}
