package cn.handyplus.chat;

import cn.handyplus.chat.hook.PlaceholderUtil;
import cn.handyplus.chat.listener.ChatPluginMessageListener;
import cn.handyplus.chat.util.ClearItemJob;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.InitApi;
import cn.handyplus.lib.constants.HookPluginEnum;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
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
    public static boolean USE_AI;

    @Override
    public void onEnable() {
        INSTANCE = this;
        InitApi initApi = InitApi.getInstance(this);
        ConfigUtil.init();
        // 加载PlaceholderApi
        USE_PAPI = BaseUtil.hook(HookPluginEnum.PLACEHOLDER_API);
        if (USE_PAPI) {
            new PlaceholderUtil(this).register();
        }
        // 加载DiscordSRV
        USE_DISCORD_SRV = BaseUtil.hook(HookPluginEnum.DISCORD_SRV);
        // 加载 ai
        USE_AI = BaseUtil.hook(HookPluginEnum.DEEP_SEEK);
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
        MessageUtil.sendConsoleMessage(ChatColor.GREEN + "Author:handy WIKI: https://ricedoc.handyplus.cn/wiki/PlayerChat/README/");
    }

    @Override
    public void onDisable() {
        InitApi.disable();
        BcUtil.unregisterOut();
        ChatPluginMessageListener.getInstance().unregister();
    }

}