package cn.handyplus.chat.listener;

import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.expand.adapter.PlayerSchedulerUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

/**
 * 私信简化
 *
 * @author handy
 * @since 1.3.6
 */
@HandyListener
public class PlayerCommandPreprocessEventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onEvent(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        List<String> tellAlias = BaseConstants.CONFIG.getStringList("tellAlias");
        if (CollUtil.isEmpty(tellAlias)) {
            return;
        }
        for (String alias : tellAlias) {
            if (event.getMessage().startsWith(alias + " ")) {
                String replace = event.getMessage().replace(alias, "plc tell");
                PlayerSchedulerUtil.performCommand(event.getPlayer(), replace);
                event.setCancelled(true);
                return;
            }
        }
    }

}
