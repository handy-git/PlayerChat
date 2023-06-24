package cn.handyplus.chat.util;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.core.GenuineUtil;
import org.bukkit.Bukkit;

/**
 * 校验
 *
 * @author handy
 */
public class CheckUtil {

    /**
     * 校验
     */
    public static void check() {
        check(Bukkit.getOnlinePlayers().size());
    }

    /**
     * 校验
     *
     * @param playerCount 人数
     */
    public static void check(Integer playerCount) {
        MessageApi.sendConsoleDebugMessage("检查服务器当前人数:" + playerCount);
        // 在线人数大于10进行卸载插件
        if (playerCount >= ChatConstants.MAX_PLAYER && !GenuineUtil.isGenuine()) {
            MessageApi.sendConsoleMessage(ChatConstants.MAX_PLAYER_MSG);
            Bukkit.getPluginManager().disablePlugin(PlayerChat.getInstance());
        }
    }

}
