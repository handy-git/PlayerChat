package cn.handyplus.horn.command.admin;

import cn.handyplus.horn.enter.HornPlayerEnter;
import cn.handyplus.horn.service.HornPlayerService;
import cn.handyplus.horn.util.ConfigUtil;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.util.AssertUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author handy
 */
public class TakeCommand implements IHandyCommandEvent {
    @Override
    public String command() {
        return "take";
    }

    @Override
    public String permission() {
        return "riceHorn.take";
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 3, sender, ConfigUtil.LANG_CONFIG.getString("paramFailureMsg"));
        String type = args[1];
        String playerName = args[2];
        Integer number = AssertUtil.isNumericToInt(args[3], sender, ConfigUtil.LANG_CONFIG.getString("amountFailureMsg"));
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);

        HornPlayerEnter hornPlayerEnter = HornPlayerService.getInstance().findByUidAndType(offlinePlayer.getUniqueId().toString(), type);
        if (hornPlayerEnter == null) {
            HornPlayerEnter hornPlayer = new HornPlayerEnter();
            hornPlayer.setPlayerName(offlinePlayer.getName());
            hornPlayer.setPlayerUuid(offlinePlayer.getUniqueId().toString());
            hornPlayer.setType(type);
            hornPlayer.setNumber(-number);
            HornPlayerService.getInstance().add(hornPlayer);
        } else {
            HornPlayerService.getInstance().subtractNumber(hornPlayerEnter.getId(), number);
        }
        MessageApi.sendMessage(sender, ConfigUtil.LANG_CONFIG.getString("takeSucceedMsg"));
    }

}