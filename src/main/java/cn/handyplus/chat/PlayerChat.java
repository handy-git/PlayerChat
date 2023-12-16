package cn.handyplus.chat;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.hook.PlaceholderUtil;
import cn.handyplus.chat.listener.ChatPluginMessageListener;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.InitApi;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.db.SqlManagerUtil;
import cn.handyplus.lib.expand.adapter.HandySchedulerUtil;
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
                .addMetrics(18860)
                .enableBc()
                .checkVersion(ConfigUtil.CONFIG.getBoolean(BaseConstants.IS_CHECK_UPDATE), ChatConstants.PLUGIN_VERSION_URL);
        ChatPluginMessageListener.getInstance().register();
        MessageUtil.sendConsoleMessage(ChatColor.GREEN + "已成功载入服务器！");
        MessageUtil.sendConsoleMessage(ChatColor.GREEN + "Author:handy QQ群:1064982471");
    }

    @Override
    public void onDisable() {
        // 关闭数据源
        BcUtil.unregisterOut();
        ChatPluginMessageListener.getInstance().unregister();
        SqlManagerUtil.getInstance().close();
        HandySchedulerUtil.cancelTask();
        MessageUtil.sendConsoleMessage("§a已成功卸载！");
        MessageUtil.sendConsoleMessage("§aAuthor:handy QQ群:1064982471");
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
            MessageUtil.sendConsoleMessage(ConfigUtil.LANG_CONFIG.getString("placeholderAPISucceedMsg"));
            return;
        }
        USE_PAPI = false;
        MessageUtil.sendConsoleMessage(ConfigUtil.LANG_CONFIG.getString("placeholderAPIFailureMsg"));
    }

}