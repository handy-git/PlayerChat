package cn.handyplus.chat.listener;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.ChannelUtil;
import cn.handyplus.chat.core.ChatUtil;
import cn.handyplus.chat.enter.ChatPlayerItemEnter;
import cn.handyplus.chat.event.PlayerChannelChatEvent;
import cn.handyplus.chat.param.ChatParam;
import cn.handyplus.chat.service.ChatPlayerItemService;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.constants.BaseConstants;
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
        // 是否有对应频道权限 如果没有权限回到默认频道
        if (!ChatConstants.DEFAULT.equals(channel) && !player.hasPermission(ChatConstants.PLAYER_CHAT_USE + channel)) {
            channel = ChatConstants.DEFAULT;
            // 缓存为频道
            ChatConstants.PLAYER_CHAT_CHANNEL.put(player.getUniqueId(), channel);
        }
        // 频道是否开启
        if (StrUtil.isEmpty(ChannelUtil.isChannelEnable(channel))) {
            return;
        }
        // 取消事件
        event.setCancelled(true);
        // 发送消息
        sendMsg(player, event.getMessage(), channel, null);
    }

    /**
     * 发送消息
     *
     * @param player         玩家
     * @param message        消息
     * @param channel        渠道
     * @param tellPlayerName 接收人
     * @since 1.1.5
     */
    public static void sendMsg(Player player, String message, String channel, String tellPlayerName) {
        // 聊天频率处理
        if (chatTimeCheck(player)) {
            return;
        }
        // 参数构建
        BcUtil.BcMessageParam param = new BcUtil.BcMessageParam();
        param.setPluginName(PlayerChat.getInstance().getName());
        param.setPlayerName(player.getName());
        param.setTimestamp(System.currentTimeMillis());
        // 构建消息参数
        ChatParam chatParam = ChatUtil.buildChatParam(player, channel);
        if (chatParam == null) {
            return;
        }
        // 添加接收人 1.1.5
        chatParam.setTellPlayerName(tellPlayerName);
        // 内容黑名单处理
        if (blackListCheck(message)) {
            MessageUtil.sendMessage(player, BaseUtil.getMsgNotColor("blacklistMsg"));
            return;
        }
        // @处理
        message = ChatUtil.at(message);
        // 消息内容
        chatParam.setMessage(message);
        // 有权限进行颜色代码处理
        chatParam.setHasColor(player.hasPermission("playerChat.color"));
        chatParam.setChannel(channel);
        param.setType(ChatConstants.CHAT_TYPE);
        param.setMessage(JsonUtil.toJson(chatParam));
        // 发送事件
        HandySchedulerUtil.runTask(() -> Bukkit.getServer().getPluginManager().callEvent(new PlayerChannelChatEvent(player, param)));
    }

    /**
     * 替换黑名单词语为*
     *
     * @param message 消息
     * @return true 存在黑名单语言
     */
    private static boolean blackListCheck(String message) {
        List<String> blacklist = ConfigUtil.CONFIG.getStringList("blacklist");
        String stripColorMessage = BaseUtil.stripColor(message);
        if (CollUtil.isNotEmpty(blacklist)) {
            for (String blackMsg : blacklist) {
                if (StrUtil.isEmpty(blackMsg)) {
                    continue;
                }
                if (stripColorMessage.contains(blackMsg)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 聊天时间处理
     *
     * @param player 玩家
     * @return true 可
     */
    private static boolean chatTimeCheck(Player player) {
        int chatTime = HandyPermissionUtil.getReverseIntNumber(player, ConfigUtil.CONFIG, "chatTime");
        if (ChatConstants.PLAYER_CHAT_TIME.containsKey(player.getUniqueId())) {
            long keepAlive = (System.currentTimeMillis() - ChatConstants.PLAYER_CHAT_TIME.get(player.getUniqueId())) / 1000L;
            if (keepAlive < chatTime) {
                String waitTimeMsg = BaseUtil.getLangMsg("chatTime").replace("${chatTime}", (chatTime - keepAlive) + "");
                MessageUtil.sendMessage(player, waitTimeMsg);
                return true;
            }
        }
        ChatConstants.PLAYER_CHAT_TIME.put(player.getUniqueId(), System.currentTimeMillis());
        return false;
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
        Player player = event.getPlayer();
        // 聊天频率处理
        if (chatTimeCheck(player)) {
            return;
        }
        // 获取物品参数
        ItemStack itemInMainHand = ItemStackUtil.getItemInMainHand(player.getInventory());
        ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemInMainHand);

        // 存储数据
        ChatPlayerItemEnter itemEnter = new ChatPlayerItemEnter();
        itemEnter.setPlayerName(player.getName());
        itemEnter.setPlayerUuid(player.getUniqueId().toString());
        itemEnter.setVersion(BaseConstants.VERSION_ID);
        itemEnter.setItem(ItemStackUtil.itemStackSerialize(itemInMainHand));
        itemEnter.setCreateTime(new Date());
        Integer itemId = ChatPlayerItemService.getInstance().add(itemEnter);

        // 参数构建
        BcUtil.BcMessageParam param = new BcUtil.BcMessageParam();
        param.setPluginName(PlayerChat.getInstance().getName());
        param.setPlayerName(player.getName());
        param.setTimestamp(System.currentTimeMillis());
        String channel = ChatConstants.PLAYER_CHAT_CHANNEL.getOrDefault(player.getUniqueId(), ChatConstants.DEFAULT);
        // 构建消息参数
        ChatParam chatParam = ChatUtil.buildChatParam(player, channel);
        if (chatParam == null) {
            return;
        }
        // 内容格式
        String content = ConfigUtil.CHAT_CONFIG.getString("item.content");
        String itemText = StrUtil.replace(content, "item", BaseUtil.getDisplayName(itemInMainHand));
        chatParam.setChannel(channel);
        chatParam.setItemText(itemText);
        chatParam.setItemHover(itemMeta.getLore());
        chatParam.setItemId(itemId);
        param.setMessage(JsonUtil.toJson(chatParam));
        param.setType(ChatConstants.ITEM_TYPE);
        // 发送事件
        HandySchedulerUtil.runTask(() -> Bukkit.getServer().getPluginManager().callEvent(new PlayerChannelChatEvent(player, param)));
    }

}