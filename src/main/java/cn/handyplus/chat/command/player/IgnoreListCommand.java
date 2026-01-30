package cn.handyplus.chat.command.player;

import cn.handyplus.chat.service.ChatPlayerIgnoreService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import cn.handyplus.lib.util.RgbTextUtil;
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
        sendIgnoreList(sender);
    }

    /**
     * 发送屏蔽列表
     *
     * @param sender 发送人
     */
    public static void sendIgnoreList(CommandSender sender) {
        // 是否为玩家
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getMsgNotColor("noPlayerFailureMsg"));
        List<String> ignoreList = ChatPlayerIgnoreService.getInstance().findIgnoreByUid(player.getUniqueId());
        if (CollUtil.isEmpty(ignoreList)) {
            MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("ignoreListEmptyMsg"));
            return;
        }
        RgbTextUtil rgbTextUtil = RgbTextUtil.getInstance().init("&7========================\n");
        String ignoreListMsg = BaseUtil.getMsgNotColor("ignoreListMsg", MapUtil.of("${number}", ignoreList.size() + ""));
        rgbTextUtil.addExtra(RgbTextUtil.getInstance().init(ignoreListMsg));
        for (String playerName : ignoreList) {
            RgbTextUtil childText = RgbTextUtil.getInstance().init("\n" + playerName);
            childText.addExtra(RgbTextUtil.getInstance().init(" &8[&cㄨ&8] ").addClickCommand("/plc removeIgnore " + playerName));
            rgbTextUtil.addExtra(childText);
        }
        rgbTextUtil.addExtra(RgbTextUtil.getInstance().init("\n&7========================"));
        rgbTextUtil.send(sender);
    }

}