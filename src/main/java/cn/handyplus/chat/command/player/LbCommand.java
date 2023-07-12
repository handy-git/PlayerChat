package cn.handyplus.chat.command.player;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.enter.ChatPlayerHornEnter;
import cn.handyplus.chat.service.ChatPlayerHornService;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.chat.core.HornUtil;
import cn.handyplus.lib.annotation.HandyCommand;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.param.BcMessageParam;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 发送消息
 *
 * @author handy
 */
@HandyCommand(name = "lb", permission = "playerChat.lb", PERMISSION_DEFAULT = PermissionDefault.TRUE)
public class LbCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 参数是否正常
        if (args.length < 2) {
            MessageUtil.sendMessage(sender, "参数错误 /lb [喇叭类型] [消息内容]");
            return true;
        }
        // 是否为玩家
        if (BaseUtil.isNotPlayer(sender)) {
            MessageUtil.sendMessage(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
            return true;
        }
        Player player = (Player) sender;
        // 获取类型
        String type = args[0];
        List<String> serverList = ConfigUtil.LB_CONFIG.getStringList("lb." + type + ".server");
        if (CollUtil.isEmpty(serverList)) {
            MessageUtil.sendMessage(sender, "配置错误");
            return true;
        }
        boolean enable = ConfigUtil.LB_CONFIG.getBoolean("lb." + type + ".enable");
        if (!enable) {
            MessageUtil.sendMessage(player, type + " &7已经被管理员禁用");
            return true;
        }

        ChatPlayerHornEnter hornPlayerEnter = ChatPlayerHornService.getInstance().findByUidAndType(player.getUniqueId(), type);
        if (hornPlayerEnter == null) {
            MessageUtil.sendMessage(player, ConfigUtil.LANG_CONFIG.getString("noHave"));
            return true;
        }
        if (hornPlayerEnter.getNumber() < 1) {
            MessageUtil.sendMessage(player, ConfigUtil.LANG_CONFIG.getString("noHaveNumber"));
            return true;
        }
        // 进行扣除
        ChatPlayerHornService.getInstance().subtractNumber(hornPlayerEnter.getId(), 1);
        // 获取消息
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        BcMessageParam param = new BcMessageParam();
        param.setPluginName(PlayerChat.getInstance().getName());
        param.setType(type);
        param.setMessage(message.toString());
        param.setSendTime(new Date());
        param.setPlayerName(player.getName());
        BcUtil.sendParamForward(player, param);
        // 发送消息
        HornUtil.sendMsg(player, param);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = null;
        if (args.length == 1) {
            commands = HornUtil.getTabTitle();
        }
        if (commands == null) {
            return new ArrayList<>();
        }
        StringUtil.copyPartialMatches(args[args.length - 1].toLowerCase(), commands, completions);
        Collections.sort(completions);
        return completions;
    }

}