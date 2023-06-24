package cn.handyplus.horn;

import cn.handyplus.horn.constants.RiceHornConstants;
import cn.handyplus.horn.listener.HornPluginMessageListener;
import cn.handyplus.horn.util.ConfigUtil;
import cn.handyplus.lib.InitApi;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.util.SqlManagerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 主类
 *
 * @author handy
 */
public class RiceHorn extends JavaPlugin {
    private static RiceHorn INSTANCE;
    public static boolean USE_PAPI;

    @Override
    public void onEnable() {
        INSTANCE = this;
        InitApi initApi = InitApi.getInstance(this);
        BaseConstants.VERIFY_SIGN_SUCCEED_MSG = RiceHornConstants.VERIFY_SIGN_SUCCEED_MSG;
        BaseConstants.VERIFY_SIGN_FAILURE_MSG = RiceHornConstants.VERIFY_SIGN_FAILURE_MSG;
        ConfigUtil.init();
        // 加载PlaceholderApi
        this.loadPlaceholder();
        // 加载主数据
        initApi.initCommand("cn.handyplus.horn.command")
                .initListener("cn.handyplus.horn.listener")
                .enableSql("cn.handyplus.horn.enter")
                .enableBc();
        new HornPluginMessageListener();
        MessageApi.sendConsoleMessage(ChatColor.GREEN + "已成功载入服务器！");
        MessageApi.sendConsoleMessage(ChatColor.GREEN + "Author:handy QQ群:1064982471");

    }

    @Override
    public void onDisable() {
        // 关闭数据源
        SqlManagerUtil.getInstance().close();
        MessageApi.sendConsoleMessage("§a已成功卸载！");
        MessageApi.sendConsoleMessage("§aAuthor:handy QQ群:1064982471");
    }

    public static RiceHorn getInstance() {
        return INSTANCE;
    }

    /**
     * 加载Placeholder
     */
    public void loadPlaceholder() {
        if (Bukkit.getPluginManager().getPlugin(BaseConstants.PLACEHOLDER_API) != null) {
            USE_PAPI = true;
            MessageApi.sendConsoleMessage(ConfigUtil.LANG_CONFIG.getString("placeholderAPISucceedMsg"));
            return;
        }
        USE_PAPI = false;
        MessageApi.sendConsoleMessage(ConfigUtil.LANG_CONFIG.getString("placeholderAPIFailureMsg"));
    }

}