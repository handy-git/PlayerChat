package cn.handyplus.chat.command.admin;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.service.ChatPlayerColorService;
import cn.handyplus.lib.command.HandyTab;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * 玩家聊天颜色命令
 *
 * @author handy
 */
public class SetColorCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "setColor";
    }

    @Override
    public String permission() {
        return "playerChat.setColor";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void tab(HandyTab handyTab) {
        handyTab.nextNull()
                .nextLang("tabHelp.colorType")
                .nextLang("tabHelp.color");
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, Command command, String s, String[] args) {
        // 参数校验: /plc setColor <玩家名> <类型> <颜色>
        AssertUtil.notTrue(args.length < 4, BaseUtil.getLangMsg("colorParamFailureMsg"));
        String playerName = args[1];
        String type = args[2];
        String color = args[3];
        AssertUtil.isTrue(ChatConstants.COLOR_TYPE_PATTERN.matcher(type).matches(), BaseUtil.getLangMsg("colorTypeFailureMsg"));

        OfflinePlayer offlinePlayer = BaseUtil.getOfflinePlayer(playerName);
        AssertUtil.notNull(offlinePlayer, BaseUtil.getLangMsg("playerNotFoundMsg"));

        ChatPlayerColorService.getInstance().setColor(offlinePlayer.getUniqueId(), offlinePlayer.getName(), type, color);
        ChatPlayerColorService.getInstance().refreshCache(offlinePlayer.getUniqueId());
        HashMap<String, String> map = MapUtil.of("${player}", playerName, "${type}", type, "${color}", color);
        MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("colorSetSuccessMsg", map));
    }

}
