package cn.handyplus.chat.event;

import cn.handyplus.lib.util.BcUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 玩家频道聊天 异步事件
 *
 * @author handy
 * @since 1.0.0
 */
public class PlayerChannelChatEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel = false;
    /**
     * 获取消息内容
     */
    private final Object bcMessageParam;

    /**
     * 发送人
     */
    private final Player player;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerChannelChatEvent(Player player, BcUtil.BcMessageParam bcMessageParam) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.bcMessageParam = bcMessageParam;
    }

    /**
     * 获取消息内容
     *
     * @return 消息内容
     */
    public Object getBcMessageParam() {
        return bcMessageParam;
    }

    /**
     * 获取发送人
     *
     * @return 发送人
     */
    public Player getPlayer() {
        return player;
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

    /**
     * 获取当前渠道
     *
     * @return 当前渠道
     * @since 1.2.4
     */
    public String getChannel() {
        return "";
    }

    /**
     * 获取消息来源
     *
     * @return 消息来源
     * @since 1.2.4
     */
    public String getSource() {
        return "";
    }

}
