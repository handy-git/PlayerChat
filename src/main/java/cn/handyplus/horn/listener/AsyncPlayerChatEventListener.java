package cn.handyplus.horn.listener;

import cn.handyplus.horn.RiceHorn;
import cn.handyplus.horn.constants.RiceHornConstants;
import cn.handyplus.horn.util.ChatUtil;
import cn.handyplus.horn.util.CheckUtil;
import cn.handyplus.horn.util.ConfigUtil;
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
    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        boolean chatEnable = ConfigUtil.CONFIG.getBoolean("chat.enable");
        if (!chatEnable) {
            return;
        }
        // 取消事件
        event.setCancelled(true);
        // 参数构建
        BcMessageParam param = new BcMessageParam();
        param.setPluginName(RiceHorn.getInstance().getName());
        param.setType(RiceHornConstants.CHAT_TYPE);
        param.setMessage(event.getMessage());
        param.setSendTime(new Date());
        // 发送本服消息
        sendMsg(event, param);
    }

    /**
     * 展示物品处理.
     *
     * @param event 事件
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onItemChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        boolean itemEnable = ConfigUtil.CONFIG.getBoolean("item.enable");
        if (!itemEnable) {
            return;
        }
        String format = ConfigUtil.CONFIG.getString("item.format");
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
        param.setPluginName(RiceHorn.getInstance().getName());
        param.setType(RiceHornConstants.ITEM_TYPE);
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
        ChatUtil.sendMsg(event.getPlayer(), param);
        // 发送BC消息
        BcUtil.sendParamForward(event.getPlayer(), param);
        // 发送人数消息
        BcUtil.sendPlayerCount();
        // 校验本服人数
        CheckUtil.check();
    }

}