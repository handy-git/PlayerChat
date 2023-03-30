package cn.handyplus.horn;

import cn.handyplus.horn.listener.HornPluginMessageListener;
import cn.handyplus.horn.util.ConfigUtil;
import cn.handyplus.lib.InitApi;
import cn.handyplus.lib.util.SqlManagerUtil;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 主类
 *
 * @author handy
 */
public class RiceHorn extends JavaPlugin {
    private static RiceHorn INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        InitApi initApi = InitApi.getInstance(this);
        ConfigUtil.init();
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

}