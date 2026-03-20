package cn.handyplus.chat.util;

import cn.handyplus.lib.core.StrUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全的物品处理工具类
 * 防止 NBT 过大导致客户端崩溃
 *
 * @author handy
 */
public class SafeItemUtil {

    /**
     * 最大允许的 NBT 大小（字节）
     * 超过此大小的物品将被简化
     */
    private static int maxNbtSize = 8192; // 8KB

    /**
     * 最大允许的 Lore 行数
     */
    private static int maxLoreLines = 20;

    /**
     * 最大允许的物品名称长度
     */
    private static int maxNameLength = 50;

    /**
     * 是否启用安全检查
     */
    private static boolean enable = true;

    /**
     * 初始化安全设置（从配置文件读取）
     */
    public static void init() {
        try {
            if (ConfigUtil.CHAT_CONFIG != null) {
                enable = ConfigUtil.CHAT_CONFIG.getBoolean("itemSafety.enable", true);
                maxNbtSize = ConfigUtil.CHAT_CONFIG.getInt("itemSafety.maxNbtSize", 8192);
                maxLoreLines = ConfigUtil.CHAT_CONFIG.getInt("itemSafety.maxLoreLines", 20);
                maxNameLength = ConfigUtil.CHAT_CONFIG.getInt("itemSafety.maxNameLength", 50);
            }
        } catch (Exception e) {
            // 如果读取失败，使用默认值
            enable = true;
            maxNbtSize = 8192;
            maxLoreLines = 20;
            maxNameLength = 50;
        }
    }

    /**
     * 检查物品是否安全
     *
     * @param item 物品
     * @return true 如果物品安全，可以展示
     */
    public static boolean isItemSafe(ItemStack item) {
        // 如果未启用安全检查，直接返回 true
        if (!enable) {
            return true;
        }

        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        try {
            // 获取物品的 NBT 数据
            String nbtData = serializeItemToNbtString(item);
            
            // 检查 NBT 大小
            if (nbtData.length() > maxNbtSize) {
                return false;
            }

            // 检查是否有危险的 NBT 标签
            if (hasDangerousNBT(nbtData)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            // 如果无法解析，认为不安全
            return false;
        }
    }

    /**
     * 获取安全的物品用于展示
     * 如果物品过大，返回简化版本
     *
     * @param item 原始物品
     * @return 安全的物品
     */
    public static ItemStack getSafeItemForDisplay(ItemStack item) {
        if (!isItemSafe(item)) {
            // 返回简化版本
            return createSafeCopy(item);
        }
        return item.clone();
    }

    /**
     * 创建物品的安全副本
     * 移除可能导致崩溃的复杂 NBT 数据
     *
     * @param original 原始物品
     * @return 安全副本
     */
    private static ItemStack createSafeCopy(ItemStack original) {
        if (original == null || original.getType() == Material.AIR) {
            return null;
        }

        try {
            ItemStack safeItem = original.clone();
            ItemMeta meta = safeItem.getItemMeta();

            if (meta != null) {
                // 限制显示名称长度
                if (meta.hasDisplayName()) {
                    String displayName = meta.getDisplayName();
                    if (displayName.length() > maxNameLength) {
                        meta.setDisplayName(displayName.substring(0, maxNameLength) + "...");
                    }
                }

                // 限制 Lore 行数和每行长度
                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    List<String> safeLore = new ArrayList<>();
                    
                    for (int i = 0; i < Math.min(lore.size(), maxLoreLines); i++) {
                        String line = lore.get(i);
                        if (line.length() > maxNameLength) {
                            line = line.substring(0, maxNameLength) + "...";
                        }
                        safeLore.add(line);
                    }
                    meta.setLore(safeLore);
                }

                // 对于附魔书，限制附魔数量
                if (meta.getClass().getSimpleName().contains("BookMeta")) {
                    try {
                        org.bukkit.inventory.meta.BookMeta bookMeta = 
                            (org.bukkit.inventory.meta.BookMeta) meta;
                        if (bookMeta.hasPages() && bookMeta.getPageCount() > 10) {
                            // 限制书页数量
                            List<String> pages = new ArrayList<>();
                            for (int i = 0; i < Math.min(bookMeta.getPageCount(), 10); i++) {
                                String page = bookMeta.getPage(i);
                                if (page.length() > 500) {
                                    page = page.substring(0, 500) + "...";
                                }
                                pages.add(page);
                            }
                            bookMeta.setPages(pages);
                        }
                    } catch (ClassCastException e) {
                        // 忽略类型转换错误
                    }
                }
            }

            safeItem.setItemMeta(meta);

            // 再次检查是否安全
            if (!isItemSafe(safeItem)) {
                // 如果仍然不安全，返回基础物品信息
                return createMinimalItem(original);
            }

            return safeItem;
        } catch (Exception e) {
            // 如果出错，返回最小化版本
            return createMinimalItem(original);
        }
    }

    /**
     * 创建最小化的物品（仅保留基本信息）
     *
     * @param original 原始物品
     * @return 最小化物品
     */
    private static ItemStack createMinimalItem(ItemStack original) {
        ItemStack minimalItem = new ItemStack(original.getType(), original.getAmount());
        
        // 只保留最基本的信息
        ItemMeta minimalMeta = minimalItem.getItemMeta();
        if (minimalMeta != null) {
            // 只保留简短的名称
            String typeName = original.getType().name();
            if (typeName.length() > 30) {
                typeName = typeName.substring(0, 30);
            }
            minimalMeta.setDisplayName("&7" + typeName);
            minimalMeta.setLore(java.util.Arrays.asList(
                "&c物品数据过大",
                "&c已简化显示",
                "",
                "&e类型：" + original.getType().name(),
                "&e数量：" + original.getAmount()
            ));
        }
        minimalItem.setItemMeta(minimalMeta);
        
        return minimalItem;
    }

    /**
     * 将物品序列化为 NBT 字符串（用于大小检查）
     *
     * @param item 物品
     * @return NBT 字符串
     */
    private static String serializeItemToNbtString(ItemStack item) {
        try {
            // 使用 CraftBukkit 的 NMS 进行序列化
            net.minecraft.server.v1_12_R1.ItemStack nmsStack = 
                org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(item);
            net.minecraft.server.v1_12_R1.NBTTagCompound tag = new net.minecraft.server.v1_12_R1.NBTTagCompound();
            nmsStack.save(tag);
            return tag.toString();
        } catch (Exception e) {
            // 如果 NMS 不可用，使用简单序列化
            try {
                return cn.handyplus.lib.util.ItemStackUtil.itemStackSerialize(item);
            } catch (Exception ex) {
                return "";
            }
        }
    }

    /**
     * 检查是否包含危险的 NBT 标签
     *
     * @param nbtData NBT 数据
     * @return true 如果包含危险标签
     */
    private static boolean hasDangerousNBT(String nbtData) {
        if (StrUtil.isEmpty(nbtData)) {
            return false;
        }

        // 检查常见的危险 NBT 标签
        String[] dangerousTags = {
            "BlockEntityTag",      // 方块实体标签（可能包含大量数据）
            "CustomModelData",      // 自定义模型数据
            "Enchantments",         // 附魔（如果数量异常）
            "StoredEnchantments",   // 存储的附魔
            "AttributeModifiers",   // 属性修饰符
            "CanDestroy",           // 可破坏的方块列表
            "CanPlaceOn",          // 可放置的方块列表
            "pages",                // 书的页面
            "SkullOwner"            // 头颅所有者（可能包含复杂的纹理数据）
        };

        int count = 0;
        for (String tag : dangerousTags) {
            if (nbtData.contains(tag)) {
                count++;
            }
        }

        // 如果同时存在多个危险标签，可能有问题
        return count >= 3;
    }

    /**
     * 获取物品的安全 Lore（用于悬浮提示）
     *
     * @param item 物品
     * @return 安全的 Lore 列表
     */
    public static List<String> getSafeLore(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return new ArrayList<>();
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return new ArrayList<>();
        }

        List<String> lore = meta.getLore();
        List<String> safeLore = new ArrayList<>();

        for (int i = 0; i < Math.min(lore.size(), maxLoreLines); i++) {
            String line = lore.get(i);
            if (line.length() > maxNameLength) {
                line = line.substring(0, maxNameLength) + "...";
            }
            safeLore.add(line);
        }

        return safeLore;
    }

    /**
     * 设置最大 NBT 大小限制
     *
     * @param size 最大字节数
     */
    public static void setMaxNbtSize(int size) {
        if (size > 0 && size <= 65536) {
            maxNbtSize = size;
        }
    }

    /**
     * 设置最大 Lore 行数
     *
     * @param lines 最大行数
     */
    public static void setMaxLoreLines(int lines) {
        if (lines > 0 && lines <= 100) {
            maxLoreLines = lines;
        }
    }

    /**
     * 设置最大名称长度
     *
     * @param length 最大长度
     */
    public static void setMaxNameLength(int length) {
        if (length > 0 && length <= 200) {
            maxNameLength = length;
        }
    }

    /**
     * 启用/禁用安全检查
     *
     * @param enabled 是否启用
     */
    public static void setEnable(boolean enabled) {
        enable = enabled;
    }
}
