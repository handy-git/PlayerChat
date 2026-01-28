package cn.handyplus.chat.listener;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.internal.PlayerSchedulerUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * 命令别名处理
 *
 * @author handy
 * @since 1.3.6
 */
@HandyListener
public class PlayerCommandPreprocessEventListener implements Listener {

    /**
     * 命令别名处理
     *
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onEvent(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        String[] param = event.getMessage().split(" ");
        String alias = param[0].replace("/", "");
        String command = ChatConstants.COMMAND_ALIAS_MAP.get(alias);
        if (StrUtil.isEmpty(command)) {
            return;
        }
        // 简单替换: tell xxx -> plc tell xxx
        String replace = event.getMessage().replace("/" + alias, command);
        event.setCancelled(true);
        PlayerSchedulerUtil.performCommand(event.getPlayer(), replace);
    }

}
