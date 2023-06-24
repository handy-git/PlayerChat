package cn.handyplus.chat.util;

import cn.handyplus.chat.hook.PlaceholderApiUtil;
import cn.handyplus.lib.api.MessageApi;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.BossBarUtil;
import cn.handyplus.lib.util.HandyPermissionUtil;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 消息工具
 *
 * @author handy
 */
public class HornUtil {

    /**
     * 发送消息
     *
     * @param player 玩家
     * @param type   类型
     * @param msg    消息
     */
    public static void sendMsg(Player player, String type, String msg) {
        if (StrUtil.isEmpty(type) || StrUtil.isEmpty(msg)) {
            MessageApi.sendConsoleMessage("消息错误:入参错误,type:" + type + ",msg:" + msg);
            return;
        }
        String rgb = ConfigUtil.CONFIG.getString("lb." + type + ".rgb");
        String name = ConfigUtil.CONFIG.getString("lb." + type + ".name");
        boolean message = ConfigUtil.CONFIG.getBoolean("lb." + type + ".message.enable");
        boolean actionbar = ConfigUtil.CONFIG.getBoolean("lb." + type + ".actionbar.enable");
        boolean boss = ConfigUtil.CONFIG.getBoolean("lb." + type + ".boss.enable");
        boolean title = ConfigUtil.CONFIG.getBoolean("lb." + type + ".title");

        // 解析变量
        rgb = PlaceholderApiUtil.set(player, rgb);
        // 加载rgb颜色
        String msgRgb = BaseUtil.replaceChatColor(rgb + msg);
        if (message) {
            List<String> messageFormatList = ConfigUtil.CONFIG.getStringList("lb." + type + ".message.format");
            for (String messageFormat : messageFormatList) {
                MessageApi.sendAllMessage(messageFormat.replace("${message}", msgRgb));
            }
        }
        if (actionbar) {
            String actionbarRgb = ConfigUtil.CONFIG.getString("lb." + type + ".actionbar.rgb");
            String actionbarRgbMsg = BaseUtil.replaceChatColor(actionbarRgb + msg);
            actionbarRgbMsg = PlaceholderApiUtil.set(player, actionbarRgbMsg);
            MessageApi.sendAllActionbar(actionbarRgbMsg);
        }
        if (title) {
            name = PlaceholderApiUtil.set(player, name);
            MessageApi.sendAllTitle(name, msgRgb);
        }
        if (boss) {
            KeyedBossBar bossBar = BossBarUtil.createBossBar(ConfigUtil.CONFIG, "lb." + type + ".boss", msgRgb);
            BossBarUtil.addAllPlayer(bossBar);
            int time = ConfigUtil.CONFIG.getInt("lb." + type + ".boss.time", 3);
            BossBarUtil.setProgress(bossBar.getKey(), time);
            BossBarUtil.removeBossBar(bossBar.getKey(), time);
        }
    }

    /**
     * 获取启动的喇叭
     *
     * @return 喇叭列表
     */
    public static List<String> getTabTitle() {
        Map<String, String> map = HandyPermissionUtil.getStringMapChild(ConfigUtil.CONFIG, "lb");
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            boolean enable = ConfigUtil.CONFIG.getBoolean("lb." + key + ".enable");
            if (!enable) {
                continue;
            }
            list.add(key);
        }
        return list;
    }

}