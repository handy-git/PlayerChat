package cn.handyplus.horn.constants;

import cn.handyplus.lib.InitApi;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.List;

/**
 * 常量
 *
 * @author handy
 */
public abstract class RiceHornConstants {

    /**
     * 渠道
     */
    public final static String RICE_HORN_CHANNEL = "BungeeCord";

    /**
     * 消息类型
     */
    public final static String CHAT_TYPE = "RICE_CHAT";

    /**
     * 物品类型
     */
    public final static String ITEM_TYPE = "RICE_ITEM";

    /**
     * 密钥
     */
    public static String SECRET_KEY = "1672244650668797952";

    /**
     * 最大数量
     */
    public static Integer MAX_PLAYER = 10;

    /**
     * 提醒
     */
    public final static String MAX_PLAYER_MSG = ChatColor.GREEN + "服务器人数大于10,您无法在免费使用该插件,插件已自动卸载";

    /**
     * 验证成功消息
     */
    public final static List<String> VERIFY_SIGN_SUCCEED_MSG = Collections.singletonList(ChatColor.GREEN + InitApi.PLUGIN.getName() + "插件验证成功,感谢您的支持...");

    /**
     * 验证失败消息
     */
    public final static List<String> VERIFY_SIGN_FAILURE_MSG = Collections.singletonList(ChatColor.GREEN + InitApi.PLUGIN.getName() + "您正在使用试用版,只支持服务器同时在线10人以下使用");

}