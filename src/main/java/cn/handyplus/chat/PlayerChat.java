package cn.handyplus.chat;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * 主类
 *
 * @author handy
 */
public class PlayerChat extends JavaPlugin {
    private static PlayerChat INSTANCE;

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    public static PlayerChat getInstance() {
        return INSTANCE;
    }

}