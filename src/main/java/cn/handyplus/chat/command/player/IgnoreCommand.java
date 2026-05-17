package cn.handyplus.chat.command.player;

import cn.handyplus.chat.enter.ChatPlayerIgnoreEnter;
import cn.handyplus.chat.service.ChatPlayerIgnoreService;
import cn.handyplus.lib.command.HandyTab;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

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
    public void tab(HandyTab handyTab) {
        handyTab.nextNull().next(Arrays.asList("true", "false"));
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 2, BaseUtil.getLangMsg("ignoreParamFailureMsg"));
        // 是否为玩家
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        // 禁止屏蔽自己
        String ignorePlayer = args[1];
        boolean ignore = this.isIgnore(args);
        AssertUtil.notTrue(ignorePlayer.equals(player.getName()), BaseUtil.getLangMsg("ignoreSelfFailureMsg"));
        ChatPlayerIgnoreEnter enter = new ChatPlayerIgnoreEnter();
        enter.setPlayerName(player.getName());
        enter.setPlayerUuid(player.getUniqueId());
        if (ignore) {
            enter.setIgnorePlayer(ignorePlayer);
            ChatPlayerIgnoreService.getInstance().setIgnore(enter);
        } else {
            enter.setWhitePlayer(ignorePlayer);
            ChatPlayerIgnoreService.getInstance().setWhite(enter);
        }
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("ignorePlayer", MapUtil.of("${player}", ignorePlayer)));
    }

    /**
     * 是否添加到屏蔽列表
     *
     * @param args 命令参数
     * @return true 添加到屏蔽列表
     */
    private boolean isIgnore(String[] args) {
        String ignore = this.getArg(args, 2).orElse("true");
        AssertUtil.notTrue(!"true".equalsIgnoreCase(ignore) && !"false".equalsIgnoreCase(ignore), BaseUtil.getLangMsg("ignoreParamFailureMsg"));
        return Boolean.parseBoolean(ignore);
    }

}
