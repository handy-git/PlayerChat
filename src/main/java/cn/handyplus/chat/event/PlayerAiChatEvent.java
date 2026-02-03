package cn.handyplus.chat.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * AI审核处理
 *
 * @author handy
 */
public class PlayerAiChatEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    /**
     * 发送人
     */
    private final Player player;
    /**
     * 获取消息内容
     */
    private final String originalMessage;
    /**
     * AI消息
     */
    private final String aiMessage;

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerAiChatEvent(Player player, String originalMessage, String aiMessage) {
        super(!Bukkit.isPrimaryThread());
        this.player = player;
        this.originalMessage = originalMessage;
        this.aiMessage = aiMessage;
    }

    /**
     * 获取发送人
     *
     * @return 发送人
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * 获取消息内容
     *
     * @return 消息内容
     */
    public String getOriginalMessage() {
        return originalMessage;
    }

    /**
     * 获取AI消息
     *
     * @return AI消息
     */
    public String getAiMessage() {
        return aiMessage;
    }

}
