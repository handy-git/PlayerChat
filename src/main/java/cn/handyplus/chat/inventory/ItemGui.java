package cn.handyplus.chat.inventory;

import cn.handyplus.chat.constants.GuiTypeEnum;
import cn.handyplus.chat.enter.ChatPlayerItemEnter;
import cn.handyplus.chat.hook.PlaceholderApiUtil;
import cn.handyplus.chat.service.ChatPlayerItemService;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.inventory.HandyInventory;
import cn.handyplus.lib.inventory.HandyInventoryUtil;
import cn.handyplus.lib.util.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * 展示物品GUI
 *
 * @author handy
 */
public class ItemGui {
    private ItemGui() {
    }

    private final static ItemGui INSTANCE = new ItemGui();

    public static ItemGui getInstance() {
        return INSTANCE;
    }

    /**
     * 创建gui
     *
     * @param player 玩家
     * @param enter  物品
     * @return gui
     */
    public Inventory createGui(Player player, ChatPlayerItemEnter enter) {
        String title = ConfigUtil.ITEM_CONFIG.getString("title");
        int size = ConfigUtil.ITEM_CONFIG.getInt("size", BaseConstants.GUI_SIZE_54);
        title = PlaceholderApiUtil.set(player, title);
        title = StrUtil.replace(title, "player", enter.getPlayerName());
        HandyInventory handyInventory = new HandyInventory(GuiTypeEnum.ITEM.getType(), title, size);
        handyInventory.setPlayer(player);
        handyInventory.setId(enter.getId());
        this.setInventoryDate(handyInventory);
        return handyInventory.getInventory();
    }

    /**
     * 设置数据
     *
     * @param handyInventory gui
     */
    private void setInventoryDate(HandyInventory handyInventory) {
        // 基础设置
        handyInventory.setGuiType(GuiTypeEnum.ITEM.getType());
        // 1. 刷新
        HandyInventoryUtil.refreshInventory(handyInventory.getInventory());
        // 2.设置数据
        this.setDate(handyInventory);
        // 3.设置功能性菜单
        this.setFunctionMenu(handyInventory);
    }

    /**
     * 设置数据
     *
     * @param handyInventory gui
     */
    private void setDate(HandyInventory handyInventory) {
        Inventory inventory = handyInventory.getInventory();
        Integer id = handyInventory.getId();
        Optional<ChatPlayerItemEnter> chatPlayerItemOptional = ChatPlayerItemService.getInstance().findById(id);
        if (!chatPlayerItemOptional.isPresent()) {
            return;
        }
        ChatPlayerItemEnter chatPlayerItem = chatPlayerItemOptional.get();
        ItemStack itemStack = ItemStackUtil.itemStackDeserialize(chatPlayerItem.getItem(), Material.STONE);
        int index = ConfigUtil.ITEM_CONFIG.getInt("index");
        inventory.setItem(index, itemStack);
    }

    /**
     * 设置功能性菜单
     *
     * @param handyInventory GUI
     */
    private void setFunctionMenu(HandyInventory handyInventory) {
        // 关闭按钮
        HandyInventoryUtil.setButton(ConfigUtil.ITEM_CONFIG, handyInventory, "back");
        // 自定义按钮
        HandyInventoryUtil.setCustomButton(ConfigUtil.ITEM_CONFIG, handyInventory, "custom");
    }

}