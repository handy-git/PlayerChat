package cn.handyplus.chat.listener;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.util.BcUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * 玩家被服务器踢出事件
 * 清理缓存
 *
 * @author handy
 */
@HandyListener
public class PlayerQuitEventListener implements Listener {
    /**
     * 玩家被服务器踢出事件.
     *
     * @param event 事件
     */
    @EventHandler
    public void onKick(PlayerKickEvent event) {
        removeCache(event.getPlayer());
    }

    /**
     * 玩家离开服务器事件.
     *
     * @param event 事件
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        removeCache(event.getPlayer());
    }

    /**
     * 清理缓存
     *
     * @param player 事件
     */
    private void removeCache(Player player) {
        ChatConstants.PLAYER_CHAT_CHANNEL.remove(player.getUniqueId());
        ChatConstants.PLAYER_PLUGIN_CHANNEL.remove(player.getUniqueId());
        ChatConstants.PLAYER_CHAT_TIME.remove(player.getUniqueId());
        ChatConstants.PLAYER_LIST.remove(player.getName());
        ChatConstants.PLAYER_IGNORE_MAP.remove(player.getUniqueId());
        ChatConstants.PLAYER_VOTE_MAP.remove(player.getUniqueId());
        ChatConstants.PLAYER_CHAT_NICK.remove(player.getUniqueId());
        BcUtil.sendPlayerList();
    }

}