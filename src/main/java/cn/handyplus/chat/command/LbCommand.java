package cn.handyplus.chat.command;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.core.ChatUtil;
import cn.handyplus.chat.core.HornUtil;
import cn.handyplus.chat.enter.ChatPlayerHornEnter;
import cn.handyplus.chat.service.ChatPlayerHornService;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.annotation.HandyCommand;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 发送消息
 *
 * @author handy
 */
@HandyCommand(name = "lb", permission = "playerChat.lb", PERMISSION_DEFAULT = PermissionDefault.TRUE)
public class LbCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        // 参数是否正常
        if (args.length < 2) {
            MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("lbParamFailureMsg"));
            return true;
        }
        // 获取类型
        String type = args[0];
        List<String> serverList = ConfigUtil.LB_CONFIG.getStringList("lb." + type + ".server");
        if (CollUtil.isEmpty(serverList)) {
            MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("lbConfigFailureMsg"));
            return true;
        }
        boolean enable = ConfigUtil.LB_CONFIG.getBoolean("lb." + type + ".enable");
        if (!enable) {
            MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("lbEnableMsg"));
            return true;
        }
        // 获取消息
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        String originalMessage = message.toString();
        // 内容黑名单处理
        if (ChatUtil.blackListCheck(originalMessage)) {
            MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("blacklistMsg"));
            return true;
        }
        // 校验玩家
        Player player = null;
        if (BaseUtil.isPlayer(sender)) {
            player = this.checkPlayer((Player) sender, type);
            if (player == null) {
                return true;
            }
        }
        String playerName = player != null ? player.getName() : "CONSOLE";
        originalMessage = StrUtil.replace(originalMessage, "player", playerName);
        BcUtil.BcMessageParam param = new BcUtil.BcMessageParam();
        param.setPluginName(PlayerChat.INSTANCE.getName());
        param.setType(type);
        param.setMessage(originalMessage);
        param.setTimestamp(System.currentTimeMillis());
        param.setPlayerName(playerName);
        BcUtil.sendParamForward(sender, param);
        // 发送消息
        HornUtil.sendMsg(player, param);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
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

    /**
     * 校验玩家喇叭
     *
     * @param player 玩家
     * @param type   类型
     * @return 玩家或者 null
     */
    private @Nullable Player checkPlayer(@NotNull Player player, String type) {
        // 查询喇叭
        Optional<ChatPlayerHornEnter> hornPlayerEnterOpt = ChatPlayerHornService.getInstance().findByUidAndType(player.getUniqueId(), type);
        if (!hornPlayerEnterOpt.isPresent()) {
            MessageUtil.sendMessage(player, BaseUtil.getMsgNotColor("noHave"));
            return null;
        }
        // 校验喇叭数量
        ChatPlayerHornEnter hornPlayerEnter = hornPlayerEnterOpt.get();
        if (hornPlayerEnter.getNumber() < 1) {
            MessageUtil.sendMessage(player, BaseUtil.getMsgNotColor("noHaveNumber"));
            return null;
        }
        // 进行扣除
        ChatPlayerHornService.getInstance().subtractNumber(hornPlayerEnter.getId(), 1);
        return player;
    }

}