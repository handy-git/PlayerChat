package cn.handyplus.chat;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * 主类
 *
 * @author handy
 */
public class PlayerChat extends JavaPlugin {
    public static PlayerChat INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
    }

}