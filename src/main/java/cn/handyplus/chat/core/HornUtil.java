package cn.handyplus.chat.core;

import cn.handyplus.chat.hook.PlaceholderApiUtil;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.constants.VersionCheckEnum;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.expand.BossBarUtil;
import cn.handyplus.lib.util.BcUtil;
import cn.handyplus.lib.util.HandyConfigUtil;
import cn.handyplus.lib.util.MessageUtil;
import cn.handyplus.lib.util.RgbTextUtil;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 喇叭消息工具
 *
 * @author handy
 */
public class HornUtil {

    /**
     * 发送消息
     *
     * @param player         玩家
     * @param bcMessageParam 消息
     */
    public static void sendMsg(Player player, BcUtil.BcMessageParam bcMessageParam) {
        String type = bcMessageParam.getType();
        String msg = bcMessageParam.getMessage();
        if (StrUtil.isEmpty(type) || StrUtil.isEmpty(msg)) {
            MessageUtil.sendConsoleMessage("消息错误:入参错误,type:" + type + ",msg:" + msg);
            return;
        }
        String rgb = ConfigUtil.LB_CONFIG.getString("lb." + type + ".rgb", "");
        String name = ConfigUtil.LB_CONFIG.getString("lb." + type + ".name");
        boolean message = ConfigUtil.LB_CONFIG.getBoolean("lb." + type + ".message.enable");
        boolean actionbar = ConfigUtil.LB_CONFIG.getBoolean("lb." + type + ".actionbar.enable");
        boolean boss = ConfigUtil.LB_CONFIG.getBoolean("lb." + type + ".boss.enable");
        boolean title = ConfigUtil.LB_CONFIG.getBoolean("lb." + type + ".title.enable", true);
        // 解析内部变量
        rgb = rgb.replace("${player}", bcMessageParam.getPlayerName());
        // 加载rgb颜色
        String msgRgb = rgb + msg;
        if (message) {
            List<String> messageFormatList = ConfigUtil.LB_CONFIG.getStringList("lb." + type + ".message.format");
            for (String messageFormat : messageFormatList) {
                messageFormat = messageFormat.replace("${message}", msgRgb);
                MessageUtil.sendAllMessage(PlaceholderApiUtil.set(player, messageFormat));
            }
        }
        // 1.9+ 才可使用
        if (actionbar && BaseConstants.VERSION_ID > VersionCheckEnum.V_1_8.getVersionId()) {
            String actionbarFormat = ConfigUtil.LB_CONFIG.getString("lb." + type + ".actionbar.format", "${message}");
            actionbarFormat = actionbarFormat.replace("${message}", msgRgb);
            actionbarFormat = PlaceholderApiUtil.set(player, actionbarFormat);
            RgbTextUtil.init(actionbarFormat).sendAllActionBar();
        }
        // 1.9+ 才可使用
        if (title && BaseConstants.VERSION_ID > VersionCheckEnum.V_1_8.getVersionId()) {
            String titleFormat = ConfigUtil.LB_CONFIG.getString("lb." + type + ".title.format", "${message}");
            titleFormat = titleFormat.replace("${message}", msgRgb);
            MessageUtil.sendAllTitle(PlaceholderApiUtil.set(player, name), PlaceholderApiUtil.set(player, titleFormat));
        }
        // 1.13+ 才可使用
        if (boss && BaseConstants.VERSION_ID > VersionCheckEnum.V_1_12.getVersionId()) {
            String bossFormat = ConfigUtil.LB_CONFIG.getString("lb." + type + ".boss.format", "${message}");
            String bossFormatStr = bossFormat.replace("${message}", msgRgb);
            KeyedBossBar bossBar = BossBarUtil.createBossBar(ConfigUtil.LB_CONFIG, "lb." + type + ".boss", PlaceholderApiUtil.set(player, bossFormatStr));
            BossBarUtil.addAllPlayer(bossBar);
            int time = ConfigUtil.LB_CONFIG.getInt("lb." + type + ".boss.time", 3);
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
        Map<String, String> map = HandyConfigUtil.getStringMapChild(ConfigUtil.LB_CONFIG, "lb");
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            boolean enable = ConfigUtil.LB_CONFIG.getBoolean("lb." + key + ".enable");
            if (!enable) {
                continue;
            }
            list.add(key);
        }
        return list;
    }

}