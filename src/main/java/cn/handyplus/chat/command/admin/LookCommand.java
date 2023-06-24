package cn.handyplus.chat.command.admin;

import cn.handyplus.chat.enter.HornPlayerEnter;
import cn.handyplus.chat.service.HornPlayerService;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.util.AssertUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author handy
 */
public class LookCommand implements IHandyCommandEvent {
    @Override
    public String command() {
        return "look";
    }

    @Override
    public String permission() {
        return "playerChat.look";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 2, sender, ConfigUtil.LANG_CONFIG.getString("paramFailureMsg"));
        String playerName = args[1];
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);

        List<HornPlayerEnter> hornPlayerList = HornPlayerService.getInstance().findByUid(offlinePlayer.getUniqueId().toString());
        if (CollUtil.isNotEmpty(hornPlayerList)) {
            Map<String, Integer> hornPlayerMap = hornPlayerList.stream().collect(Collectors.toMap(HornPlayerEnter::getType, v -> v.getNumber()));
            for (String type : hornPlayerMap.keySet()) {
                MessageApi.sendMessage(sender, playerName + "拥有 " + type + " 喇叭:" + hornPlayerMap.get(type));
            }
        } else {
            MessageApi.sendMessage(sender, ConfigUtil.LANG_CONFIG.getString("noLook", "").replace("${player}", playerName));
        }
    }

}