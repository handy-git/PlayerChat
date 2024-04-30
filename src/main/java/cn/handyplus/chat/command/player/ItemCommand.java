package cn.handyplus.chat.command.player;

import cn.handyplus.chat.enter.ChatPlayerItemEnter;
import cn.handyplus.chat.inventory.ItemGui;
import cn.handyplus.chat.service.ChatPlayerItemService;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.expand.adapter.HandySchedulerUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

/**
 * 展示物品
 *
 * @author handy
 */
public class ItemCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "item";
    }

    @Override
    public String permission() {
        return "playerChat.item";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 2, sender, ConfigUtil.LANG_CONFIG.getString("paramFailureMsg"));
        // 是否为玩家
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getMsgNotColor("noPlayerFailureMsg"));
        // 展示物品ID
        Integer itemId = AssertUtil.isNumericToInt(args[1], sender, BaseUtil.getMsgNotColor("amountFailureMsg"));
        Optional<ChatPlayerItemEnter> chatPlayerItemOptional = ChatPlayerItemService.getInstance().findById(itemId);
        if (!chatPlayerItemOptional.isPresent()) {
            MessageUtil.sendMessage(sender, BaseUtil.getMsgNotColor("itemNotFoundMsg"));
            return;
        }
        Inventory inventory = ItemGui.getInstance().createGui(player, itemId);
        HandySchedulerUtil.runTask(() -> player.openInventory(inventory));
    }

}