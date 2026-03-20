package cn.handyplus.chat.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * 文本组件兼容工具类
 * 处理不同 Minecraft 版本的 TextComponent API 差异
 *
 * @author handy
 */
public class CompatibleTextUtil {

    /**
     * 创建文本组件
     *
     * @param text 文本内容
     * @return TextComponent
     */
    public static TextComponent createTextComponent(String text) {
        // 1.12.2 和高版本都支持这个构造方法
        return new TextComponent(TextComponent.fromLegacyText(text));
    }

    /**
     * 设置悬浮事件
     *
     * @param component 文本组件
     * @param hoverText 悬浮文本列表
     */
    public static void setHoverEvent(TextComponent component, List<String> hoverText) {
        if (hoverText == null || hoverText.isEmpty()) {
            return;
        }
        String joined = String.join("\n", hoverText);
        // 将颜色代码转换为 ChatColor 格式
        String formattedText = ChatColor.translateAlternateColorCodes('&', joined);
        
        // 创建悬浮事件 - 使用 SHOW_TEXT 动作
        HoverEvent hoverEvent = new HoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            new ComponentBuilder(formattedText).create()
        );
        component.setHoverEvent(hoverEvent);
    }

    /**
     * 设置物品悬浮事件
     *
     * @param component 文本组件
     * @param item      物品
     */
    public static void setItemHoverEvent(TextComponent component, ItemStack item) {
        if (item == null) {
            return;
        }
        // 使用 ItemStackUtil 序列化物品为 JSON
        String itemJson = serializeItemStack(item);
        if (itemJson != null) {
            HoverEvent hoverEvent = new HoverEvent(
                HoverEvent.Action.SHOW_ITEM,
                new ComponentBuilder(itemJson).create()
            );
            component.setHoverEvent(hoverEvent);
        }
    }

    /**
     * 设置点击事件 - 执行命令
     *
     * @param component 文本组件
     * @param command   命令
     */
    public static void setClickCommand(TextComponent component, String command) {
        if (command == null || command.isEmpty()) {
            return;
        }
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
        component.setClickEvent(clickEvent);
    }

    /**
     * 设置点击事件 - 建议命令
     *
     * @param component 文本组件
     * @param command   建议命令
     */
    public static void setClickSuggestCommand(TextComponent component, String command) {
        if (command == null || command.isEmpty()) {
            return;
        }
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command);
        component.setClickEvent(clickEvent);
    }

    /**
     * 添加额外文本到组件
     *
     * @param parent 父组件
     * @param extra  额外组件
     */
    public static void addExtra(TextComponent parent, TextComponent extra) {
        parent.addExtra(extra);
    }

    /**
     * 序列化物品堆
     * 在 1.12.2 和高版本中使用不同的方法
     *
     * @param item 物品
     * @return JSON 字符串
     */
    private static String serializeItemStack(ItemStack item) {
        try {
            // 尝试使用 Bukkit 的 ItemSerializer（如果可用）
            if (VersionUtil.isModern()) {
                // 1.13+ 使用新方法
                return item.serialize().toString();
            } else {
                // 1.12.2 使用旧方法
                return legacySerializeItemStack(item);
            }
        } catch (Exception e) {
            // 如果序列化失败，返回 null
            return null;
        }
    }

    /**
     * 1.12.2 物品序列化
     *
     * @param item 物品
     * @return JSON 字符串
     */
    private static String legacySerializeItemStack(ItemStack item) {
        try {
            // 使用 NMS 序列化（1.12.2）
            net.minecraft.server.v1_12_R1.ItemStack nmsStack = 
                org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(item);
            net.minecraft.server.v1_12_R1.NBTTagCompound tag = new net.minecraft.server.v1_12_R1.NBTTagCompound();
            nmsStack.save(tag);
            return tag.toString();
        } catch (Exception e) {
            // 如果失败，尝试简单描述
            return "{\"id\":\"" + item.getType().getName() + "\",\"Count\":" + item.getAmount() + "}";
        }
    }

    /**
     * 转换颜色代码
     * 将 & 格式的颜色代码转换为 ChatColor 格式
     *
     * @param text 原始文本
     * @return 格式化后的文本
     */
    public static String translateColorCodes(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * 转换颜色代码列表
     *
     * @param texts 文本列表
     * @return 格式化后的文本列表
     */
    public static List<String> translateColorCodes(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return texts;
        }
        for (int i = 0; i < texts.size(); i++) {
            texts.set(i, translateColorCodes(texts.get(i)));
        }
        return texts;
    }
}
