package cn.handyplus.chat.command.admin;

import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.util.BossBarUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * 移除全部BossBar消息
 *
 * @author handy
 * @since 1.2.1
 */
public class ClearBossBarCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "clearBossBar";
    }

    @Override
    public String permission() {
        return "playerChat.clearBossBar";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        BossBarUtil.removeAllBossBar();
    }

}