package cn.handyplus.chat.event;

import cn.handyplus.lib.util.BcUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * 玩家频道聊天 事件
 *
 * @author handy
 * @since 1.0.0
 */
public class PlayerChannelChatEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel = false;
    private final BcUtil.BcMessageParam bcMessageParam;

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerChannelChatEvent(Player player, BcUtil.BcMessageParam bcMessageParam) {
        super(player);
        this.bcMessageParam = bcMessageParam;
    }

    /**
     * 获取消息内容
     *
     * @return 消息内容
     */
    public BcUtil.BcMessageParam getBcMessageParam() {
        return bcMessageParam;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}