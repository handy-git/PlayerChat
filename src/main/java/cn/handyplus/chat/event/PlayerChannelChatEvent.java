package cn.handyplus.chat.event;

import cn.handyplus.chat.param.ChatParam;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.util.BcUtil;
import lombok.Getter;
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
    @Getter
    private final BcUtil.BcMessageParam bcMessageParam;

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

    public PlayerChannelChatEvent(Player player, BcUtil.BcMessageParam bcMessageParam) {
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
     * @since 1.1.7
     */
    public String getOriginalMessage() {
        ChatParam chatParam = JsonUtil.toBean(bcMessageParam.getMessage(), ChatParam.class);
        return chatParam.getMessage();
    }

    /**
     * 获取当前渠道
     *
     * @return 当前渠道
     * @since 1.2.3
     */
    public String getChannel() {
        ChatParam chatParam = JsonUtil.toBean(bcMessageParam.getMessage(), ChatParam.class);
        return chatParam.getChannel();
    }

    /**
     * 获取消息来源
     *
     * @return 消息来源
     * @since 1.2.3
     */
    public String getSource() {
        ChatParam chatParam = JsonUtil.toBean(bcMessageParam.getMessage(), ChatParam.class);
        return chatParam.getSource();
    }

}