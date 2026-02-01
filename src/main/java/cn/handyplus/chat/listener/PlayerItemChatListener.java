package cn.handyplus.chat.listener;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.ChatUtil;
import cn.handyplus.chat.enter.ChatPlayerItemEnter;
import cn.handyplus.chat.event.PlayerChannelChatEvent;
import cn.handyplus.chat.hook.PlaceholderApiUtil;
import cn.handyplus.chat.param.ChatChildParam;
import cn.handyplus.chat.param.ChatParam;
import cn.handyplus.chat.service.ChatPlayerItemService;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.JsonUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
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
 * 展示物品聊天监听器
 *
 * @author handy
 */
@HandyListener
public class PlayerItemChatListener implements Listener {

    /**
     * 展示物品处理
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
        String format = ConfigUtil.CHAT_CONFIG.getString("item.format", "[i]");
        String message = event.getMessage();
        if (!message.contains(format)) {
            return;
        }
        // 取消事件
        event.setCancelled(true);
        Player player = event.getPlayer();
        // 聊天频率处理
        if (ChatUtil.chatCheck(player, message)) {
            return;
        }
        // 获取物品参数
        ItemStack itemInMainHand = ItemStackUtil.getItemInMainHand(player.getInventory());
        ItemMeta itemMeta = ItemStackUtil.getItemMeta(itemInMainHand);

        // 存储数据
        ChatPlayerItemEnter itemEnter = new ChatPlayerItemEnter();
        itemEnter.setPlayerName(player.getName());
        itemEnter.setPlayerUuid(player.getUniqueId());
        itemEnter.setVersion(BaseConstants.VERSION_ID);
        itemEnter.setItem(ItemStackUtil.itemStackSerialize(itemInMainHand));
        itemEnter.setCreateTime(new Date());
        Integer itemId = ChatPlayerItemService.getInstance().add(itemEnter);

        // 参数构建
        BcUtil.BcMessageParam param = new BcUtil.BcMessageParam();
        param.setPluginName(PlayerChat.INSTANCE.getName());
        param.setPlayerName(player.getName());
        param.setTimestamp(System.currentTimeMillis());
        // 所在频道
        String channel = ChatUtil.getChannel(player);
        // 构建消息参数
        ChatParam chatParam = ChatUtil.buildChatParam(player, channel);
        if (chatParam == null) {
            return;
        }
        // 内容格式
        String content = ConfigUtil.CHAT_CONFIG.getString("item.content", "&5[&a展示了一个 &f${item} &a点击查看&5]");
        String displayName = BaseUtil.getDisplayName(itemInMainHand);
        int itemLength = ConfigUtil.CHAT_CONFIG.getInt("item.length", 18);
        if (BaseUtil.stripColor(displayName).length() > itemLength) {
            displayName = BaseUtil.stripColor(displayName).substring(0, itemLength) + "...";
        }
        String itemText = StrUtil.replace(content, "item", displayName.replace("%", ""));
        itemText = message.replace(format, itemText);
        // 解析变量
        String text = PlaceholderApiUtil.set(player, itemText);
        // 替换组件
        text = BaseUtil.spriteComponent(text, itemInMainHand);
        // 给予展示属性
        ChatChildParam chatChildParam = chatParam.getChildList().get(chatParam.getChildList().size() - 1);
        chatChildParam.setText(text);
        chatChildParam.setHover(itemMeta.getLore());
        chatChildParam.setHoverItem(itemEnter.getItem());
        chatChildParam.setClick("/plc item " + itemId);
        // 消息内容
        chatParam.setMessage(chatChildParam.getText());
        param.setMessage(JsonUtil.toJson(chatParam));
        param.setType(ChatConstants.ITEM_TYPE);
        // 发送事件
        Bukkit.getServer().getPluginManager().callEvent(new PlayerChannelChatEvent(player, param));
    }

}
