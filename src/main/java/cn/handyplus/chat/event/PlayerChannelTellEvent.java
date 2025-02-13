package cn.handyplus.chat.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 玩家频道私信 异步事件
 *
 * @author handy
 * @since 1.2.0
 */
public class PlayerChannelTellEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel = false;

    /**
     * 获取消息内容
     */
    @Getter
    private final Object bcMessageParam;

    /**
     * 发送人
     */
    @Getter
    private final Player player;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerChannelTellEvent(Player player, BcUtil.BcMessageParam bcMessageParam) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
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
     */
    public String getOriginalMessage() {
        return null;
    }

    /**
     * 获取私信接收人
     *
     * @return 私信接收人
     */
    public String getTellPlayerName() {
        return null;
    }

}