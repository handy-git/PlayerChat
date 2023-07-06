package cn.handyplus.chat.command.admin;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * 重载配置
 *
 * @author handy
 */
public class ReloadCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "reload";
    }

    @Override
    public String permission() {
        return "playerChat.reload";
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ConfigUtil.init();
                MessageUtil.sendMessage(sender, ConfigUtil.LANG_CONFIG.getString("reloadMsg"));
            }
        }.runTaskAsynchronously(PlayerChat.getInstance());
    }

}