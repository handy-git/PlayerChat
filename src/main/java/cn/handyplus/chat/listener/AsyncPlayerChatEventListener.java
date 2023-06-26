package cn.handyplus.chat.listener;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.util.ChatUtil;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.annotation.HandyListener;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.param.BcMessageParam;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.ItemStackUtil;
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
        boolean chatEnable = ConfigUtil.CHAT_CONFIG.getBoolean("chat.enable");
        if (!chatEnable) {
            return;
        }
        // 取消事件
        event.setCancelled(true);
        // 参数构建
        BcMessageParam param = new BcMessageParam();
        param.setPluginName(PlayerChat.getInstance().getName());
        param.setType(ChatConstants.CHAT_TYPE);
        param.setMessage(event.getMessage());
        // 有权限进行颜色代码处理
        if (event.getPlayer().hasPermission("playerChat.color")) {
            param.setMessage(BaseUtil.replaceChatColor(event.getMessage()));
        }
        param.setSendTime(new Date());
        // 发送本服消息
        sendMsg(event, param);
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
        String displayName = BaseUtil.getDisplayName(itemInMainHand);
        List<String> loreList = itemMeta.getLore();
        String loreStr = CollUtil.listToStr(loreList, "\n");
        // 参数构建
        BcMessageParam param = new BcMessageParam();
        param.setPluginName(PlayerChat.getInstance().getName());
        param.setType(ChatConstants.ITEM_TYPE);
        param.setMessage(displayName);
        param.setHover(loreStr);
        param.setSendTime(new Date());
        sendMsg(event, param);
    }

    /**
     * 发送对应BC消息
     *
     * @param event 事件
     * @param param 参数
     */
    private static void sendMsg(AsyncPlayerChatEvent event, BcMessageParam param) {
        // 发送本服消息
        ChatUtil.sendMsg(event.getPlayer(), param, true);
        // 发送BC消息
        BcUtil.sendParamForward(event.getPlayer(), param);
    }

}