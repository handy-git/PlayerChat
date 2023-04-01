package cn.handyplus.horn.util;

import cn.handyplus.horn.RiceHorn;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BossBarUtil;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * 消息工具
 *
 * @author handy
 */
public class HornUtil {

    /**
     * 发送消息
     *
     * @param type 类型
     * @param msg  消息
     */
    public static void sendMsg(String type, String msg) {
        if (StrUtil.isEmpty(type) || StrUtil.isEmpty(msg)) {
            MessageApi.sendConsoleMessage("消息错误:入参错误,type:" + type + ",msg:" + msg);
            return;
        }
        String rgb = ConfigUtil.CONFIG.getString("lb." + type + ".rgb");
        msg = rgb + msg;
        String name = ConfigUtil.CONFIG.getString("lb." + type + ".name");
        boolean message = ConfigUtil.CONFIG.getBoolean("lb." + type + ".message");
        boolean actionbar = ConfigUtil.CONFIG.getBoolean("lb." + type + ".actionbar");
        boolean boss = ConfigUtil.CONFIG.getBoolean("lb." + type + ".boss");
        boolean title = ConfigUtil.CONFIG.getBoolean("lb." + type + ".title");

        msg = BaseUtil.replaceChatColor(msg);
        if (message) {
            MessageApi.sendAllMessage(msg);
        }
        if (actionbar) {
            MessageApi.sendAllActionbar(msg);
        }
        if (title) {
            MessageApi.sendAllTitle(name, msg);
        }
        if (boss) {
            BossBar bossBar = BossBarUtil.createBossBar(msg);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(onlinePlayer);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    bossBar.removeAll();
                }
            }.runTaskLater(RiceHorn.getInstance(), 3 * 20);
        }
    }

}