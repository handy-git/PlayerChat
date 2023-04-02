package cn.handyplus.horn;

import cn.handyplus.horn.listener.HornPluginMessageListener;
import cn.handyplus.horn.util.ConfigUtil;
import cn.handyplus.lib.InitApi;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.util.SqlManagerUtil;
import org.bukkit.Bukkit;
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
        ConfigUtil.init();

        // 加载PlaceholderApi
        this.loadPlaceholder();

        initApi.initCommand("cn.handyplus.horn.command")
                .initListener("cn.handyplus.horn.listener")
                .enableSql("cn.handyplus.horn.enter")
                .enableBc();
        new HornPluginMessageListener();
    }

    @Override
    public void onDisable() {
        SqlManagerUtil.getInstance().close();
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
            MessageApi.sendConsoleMessage(BaseConstants.PLACEHOLDER_API + "加载成功");
            return;
        }
        USE_PAPI = false;
        MessageApi.sendConsoleMessage(BaseConstants.PLACEHOLDER_API + "未加载");
    }

}