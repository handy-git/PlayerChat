package cn.handyplus.chat.listener;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.ChannelUtil;
import cn.handyplus.chat.core.ChatUtil;
import cn.handyplus.chat.event.PlayerChannelChatEvent;
import cn.handyplus.chat.param.ChatParam;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.expand.adapter.HandySchedulerUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.HandyPermissionUtil;
import cn.handyplus.lib.util.ItemStackUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Date;
import java.util.List;

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
        String channel = ChatConstants.PLAYER_CHAT_CHANNEL.getOrDefault(player.getUniqueId(), ChatConstants.DEFAULT);
        // 渠道是否开启
        if (StrUtil.isEmpty(ChannelUtil.isChannelEnable(channel))) {
            return;
        }
        // 取消事件
        event.setCancelled(true);

        // 聊天频率处理
        if (!this.chatTimeCheck(player)) {
            return;
        }

        // 参数构建
        BcUtil.BcMessageParam param = new BcUtil.BcMessageParam();
        param.setPluginName(PlayerChat.getInstance().getName());
        param.setPlayerName(player.getName());
        param.setSendTime(new Date());
        // 构建消息参数
        ChatParam chatParam = ChatUtil.buildChatParam(player, channel);
        if (chatParam == null) {
            return;
        }

        // 内容黑名单处理
        String message = this.blackListCheck(event);
        // 有权限进行颜色代码处理
        chatParam.setMessage(ChatUtil.at(message));
        chatParam.setHasColor(event.getPlayer().hasPermission("playerChat.color"));
        chatParam.setChannel(channel);
        param.setType(ChatConstants.CHAT_TYPE);
        param.setMessage(JsonUtil.toJson(chatParam));
        // 发送事件
        HandySchedulerUtil.runTask(() -> Bukkit.getServer().getPluginManager().callEvent(new PlayerChannelChatEvent(player, param)));
    }

    /**
     * 替换黑名单词语为*
     *
     * @param event 事件
     * @return 健康消息
     */
    private String blackListCheck(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        List<String> blacklist = ConfigUtil.CONFIG.getStringList("blacklist");
        if (CollUtil.isNotEmpty(blacklist)) {
            for (String blackMsg : blacklist) {
                if (StrUtil.isNotEmpty(blackMsg)) {
                    message = message.replace(blackMsg, "*");
                }
            }
        }
        return message;
    }

    /**
     * 聊天时间处理
     *
     * @param player 玩家
     * @return true 可
     */
    private boolean chatTimeCheck(Player player) {
        int chatTime = HandyPermissionUtil.getReverseIntNumber(player, ConfigUtil.CONFIG, "chatTime");
        if (ChatConstants.PLAYER_CHAT_TIME.containsKey(player.getUniqueId())) {
            long keepAlive = (System.currentTimeMillis() - ChatConstants.PLAYER_CHAT_TIME.get(player.getUniqueId())) / 1000L;
            if (keepAlive < chatTime) {
                String waitTimeMsg = BaseUtil.getLangMsg("chatTime").replace("${chatTime}", (chatTime - keepAlive) + "");
                MessageUtil.sendMessage(player, waitTimeMsg);
                return false;
            }
        }
        ChatConstants.PLAYER_CHAT_TIME.put(player.getUniqueId(), System.currentTimeMillis());
        return true;
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
        BcUtil.BcMessageParam param = new BcUtil.BcMessageParam();
        param.setPluginName(PlayerChat.getInstance().getName());
        param.setPlayerName(player.getName());
        param.setSendTime(new Date());
        String channel = ChatConstants.PLAYER_CHAT_CHANNEL.getOrDefault(player.getUniqueId(), ChatConstants.DEFAULT);
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
        HandySchedulerUtil.runTask(() -> Bukkit.getServer().getPluginManager().callEvent(new PlayerChannelChatEvent(player, param)));
    }

}