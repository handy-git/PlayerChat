package cn.handyplus.chat.listener;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.event.PlayerChannelChatEvent;
import cn.handyplus.chat.param.ChatParam;
import cn.handyplus.chat.util.ChatUtil;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.param.BcMessageParam;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Date;

/**
 * 当玩家聊天时触发这个事件
 *
 * @author handy
 */
@HandyListener
public class AsyncPlayerChatEventListener implements Listener {

    /**
     * 聊天信息处理.
     *
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        String channel = ChatConstants.CHANNEL_MAP.getOrDefault(player.getUniqueId(), "default");
        boolean chatEnable = ConfigUtil.CHAT_CONFIG.getBoolean("chat." + channel + ".enable");
        if (!chatEnable) {
            return;
        }
        // 取消事件
        event.setCancelled(true);

        // 参数构建
        BcMessageParam param = new BcMessageParam();
        param.setPluginName(PlayerChat.getInstance().getName());
        param.setPlayerName(player.getName());
        param.setSendTime(new Date());
        // 构建消息参数
        ChatParam chatParam = ChatUtil.buildChatParam(player, channel);
        if (chatParam == null) {
            return;
        }
        // 有权限进行颜色代码处理
        if (event.getPlayer().hasPermission("playerChat.color")) {
            param.setMessage(BaseUtil.replaceChatColor(event.getMessage()));
        }
        chatParam.setMessage(event.getMessage());
        chatParam.setChannel(channel);
        param.setType(ChatConstants.CHAT_TYPE);
        param.setMessage(JsonUtil.toJson(chatParam));
        // 发送事件
        Bukkit.getScheduler().runTask(PlayerChat.getInstance(), () -> Bukkit.getServer().getPluginManager().callEvent(new PlayerChannelChatEvent(player, param)));
    }

    /**
     * 展示物品处理.
     *
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onItemChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        boolean itemEnable = ConfigUtil.CHAT_CONFIG.getBoolean("item.enable");
        if (!itemEnable) {
            return;
        }
        String format = ConfigUtil.CHAT_CONFIG.getString("item.format");
        if (!event.getMessage().equals(format)) {
            return;
        }
        // 取消事件
        event.setCancelled(true);
        // 获取物品参数
        Player player = event.getPlayer();
        ItemStack itemInMainHand = ItemStackUtil.getItemInMainHand(player.getInventory());
        ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemInMainHand);

        // 参数构建
        BcMessageParam param = new BcMessageParam();
        param.setPluginName(PlayerChat.getInstance().getName());
        param.setPlayerName(player.getName());
        param.setSendTime(new Date());
        String channel = ChatConstants.CHANNEL_MAP.getOrDefault(player.getUniqueId(), "default");
        // 构建消息参数
        ChatParam chatParam = ChatUtil.buildChatParam(player, channel);
        if (chatParam == null) {
            return;
        }
        chatParam.setChannel(channel);
        chatParam.setItemText(BaseUtil.getDisplayName(itemInMainHand));
        chatParam.setItemHover(itemMeta.getLore());
        param.setMessage(JsonUtil.toJson(chatParam));
        param.setType(ChatConstants.ITEM_TYPE);
        // 发送事件
        Bukkit.getScheduler().runTask(PlayerChat.getInstance(), () -> Bukkit.getServer().getPluginManager().callEvent(new PlayerChannelChatEvent(player, param)));
    }

}