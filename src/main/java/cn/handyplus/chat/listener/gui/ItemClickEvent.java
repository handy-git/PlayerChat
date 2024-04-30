package cn.handyplus.chat.listener.gui;

import cn.handyplus.chat.constants.GuiTypeEnum;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.expand.adapter.PlayerSchedulerUtil;
import cn.handyplus.lib.inventory.HandyInventory;
import cn.handyplus.lib.inventory.HandyInventoryUtil;
import cn.handyplus.lib.inventory.IHandyClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

/**
 * item点击事件
 *
 * @author handy
 */
public class ItemClickEvent implements IHandyClickEvent {

    @Override
    public String guiType() {
        return GuiTypeEnum.ITEM.getType();
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void rawSlotClick(HandyInventory handyInventory, InventoryClickEvent event) {
        int rawSlot = event.getRawSlot();
        Player player = handyInventory.getPlayer();
        // 返回按钮
        if (HandyInventoryUtil.isIndex(rawSlot, ConfigUtil.ITEM_CONFIG, "back")) {
            handyInventory.syncClose();
            return;
        }
        // 自定义菜单处理
        Map<Integer, String> custom = HandyInventoryUtil.getCustomButton(ConfigUtil.ITEM_CONFIG, "custom");
        String command = custom.get(rawSlot);
        if (StrUtil.isNotEmpty(command)) {
            PlayerSchedulerUtil.syncPerformReplaceCommand(player, command);
        }
    }

}