package cn.handyplus.chat.event;

import cn.handyplus.lib.param.BcMessageParam;
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
    private final BcMessageParam bcMessageParam;
    private final boolean isConsoleMsg;

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerChannelChatEvent(Player player, BcMessageParam bcMessageParam) {
        super(player);
        this.bcMessageParam = bcMessageParam;
        this.isConsoleMsg = false;
    }

    public PlayerChannelChatEvent(Player player, BcMessageParam bcMessageParam, boolean isConsoleMsg) {
        super(player);
        this.bcMessageParam = bcMessageParam;
        this.isConsoleMsg = isConsoleMsg;
    }

    /**
     * 获取消息内容
     *
     * @return 消息内容
     */
    public BcMessageParam getBcMessageParam() {
        return bcMessageParam;
    }

    /**
     * 是否发送控制台消息
     *
     * @return true 是
     */
    public boolean isConsoleMsg() {
        return isConsoleMsg;
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