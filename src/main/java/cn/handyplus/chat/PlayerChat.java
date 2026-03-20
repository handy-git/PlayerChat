package cn.handyplus.chat;

import cn.handyplus.chat.hook.PlaceholderUtil;
import cn.handyplus.chat.listener.ChatPluginMessageListener;
import cn.handyplus.chat.util.ClearItemJob;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.chat.util.VersionUtil;
import cn.handyplus.lib.InitApi;
import cn.handyplus.lib.constants.HookPluginEnum;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.HookPluginUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 主类
 *
 * @author handy
 */
public class PlayerChat extends JavaPlugin {
    public static PlayerChat INSTANCE;
    public static boolean USE_PAPI;
    public static boolean USE_DISCORD_SRV;

    @Override
    public void onEnable() {
        INSTANCE = this;
        InitApi initApi = InitApi.getInstance(this);
        ConfigUtil.init();
        // 初始化版本检测
        VersionUtil.init();
        // 初始化物品安全检查
        cn.handyplus.chat.util.SafeItemUtil.init();
        // 加载 PlaceholderApi
        USE_PAPI = HookPluginUtil.hook(HookPluginEnum.PLACEHOLDER_API);
        if (USE_PAPI) {
            new PlaceholderUtil(this).register();
        }
        // 加载 DiscordSRV
        USE_DISCORD_SRV = HookPluginUtil.hook(HookPluginEnum.DISCORD_SRV);
        // 加载主数据
        initApi.initCommand("cn.handyplus.chat.command")
                .initListener("cn.handyplus.chat.listener")
                .enableSql("cn.handyplus.chat.enter")
                .initClickEvent("cn.handyplus.chat.listener.gui")
                .addMetrics(18860)
                .enableBc()
                .checkVersion();
        ChatPluginMessageListener.getInstance().register();
        // 定时任务启动
        ClearItemJob.init();
        MessageUtil.sendConsoleMessage(ChatColor.GREEN + "已成功载入服务器!");
        MessageUtil.sendConsoleMessage(ChatColor.GREEN + "Author: handy WIKI: https://ricedoc.handyplus.cn/wiki/PlayerChat/README/");
        MessageUtil.sendConsoleMessage(ChatColor.GREEN + "服务器版本：" + VersionUtil.getVersionString());
    }

    @Override
    public void onDisable() {
        InitApi.disable();
        BcUtil.unregisterOut();
        ChatPluginMessageListener.getInstance().unregister();
    }

}
