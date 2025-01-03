package cn.handyplus.chat;

import cn.handyplus.chat.hook.PlaceholderUtil;
import cn.handyplus.chat.job.ClearItemJob;
import cn.handyplus.chat.listener.ChatPluginMessageListener;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.InitApi;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 主类
 *
 * @author handy
 */
public class PlayerChat extends JavaPlugin {
    private static PlayerChat INSTANCE;
    public static boolean USE_PAPI;

    @Override
    public void onEnable() {
        INSTANCE = this;
        InitApi initApi = InitApi.getInstance(this);
        ConfigUtil.init();
        // 加载PlaceholderApi
        this.loadPlaceholder();
        // 加载主数据
        initApi.initCommand("cn.handyplus.chat.command")
                .initListener("cn.handyplus.chat.listener")
                .enableSql("cn.handyplus.chat.enter")
                .initClickEvent("cn.handyplus.chat.listener.gui")
                .addMetrics(18860)
                .enableBc()
                .checkVersion(ConfigUtil.CONFIG.getBoolean(BaseConstants.IS_CHECK_UPDATE));
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

    public static PlayerChat getInstance() {
        return INSTANCE;
    }

    /**
     * 加载Placeholder
     */
    public void loadPlaceholder() {
        if (Bukkit.getPluginManager().getPlugin(BaseConstants.PLACEHOLDER_API) != null) {
            USE_PAPI = true;
            new PlaceholderUtil(this).register();
            MessageUtil.sendConsoleMessage(BaseUtil.getMsgNotColor("placeholderAPISucceedMsg"));
            return;
        }
        USE_PAPI = false;
        MessageUtil.sendConsoleMessage(BaseUtil.getMsgNotColor("placeholderAPIFailureMsg"));
    }

}