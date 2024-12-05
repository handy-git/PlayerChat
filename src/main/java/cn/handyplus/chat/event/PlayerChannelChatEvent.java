package cn.handyplus.chat.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 玩家频道聊天 事件
 *
 * @author handy
 * @since 1.0.0
 */
public class PlayerChannelChatEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel = false;
    /**
     * 获取消息内容
     */
    @Getter
    private final Object bcMessageParam;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerChannelChatEvent(Player player, Object bcMessageParam) {
        super(player);
        this.bcMessageParam = bcMessageParam;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * 获取原始消息
     *
     * @return 原始消息
     * @since 1.1.7
     */
    public String getOriginalMessage() {
        return "";
    }

}