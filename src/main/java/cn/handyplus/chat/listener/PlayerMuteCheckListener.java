package cn.handyplus.chat.listener;

import cn.handyplus.chat.core.MuteUtil;
import cn.handyplus.lib.annotation.HandyListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * 禁言检查监听器
 *
 * @author handy
 */
@HandyListener
public class PlayerMuteCheckListener implements Listener {

    /**
     * 禁言检查
     *
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onMuteChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        
        // 检查是否被禁言
        if (MuteUtil.checkMute(player)) {
            event.setCancelled(true);
        }
    }

}
