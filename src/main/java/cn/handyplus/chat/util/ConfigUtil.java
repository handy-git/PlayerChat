package cn.handyplus.chat.util;

import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.util.HandyConfigUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;

/**
 * 配置
 *
 * @author handy
 */
public class ConfigUtil {
    public static FileConfiguration CHAT_CONFIG, LB_CONFIG, ITEM_CONFIG;

    /**
     * 加载全部配置
     */
    public static void init() {
        HandyConfigUtil.loadConfig();
        HandyConfigUtil.loadLangConfig(true);
        CHAT_CONFIG = HandyConfigUtil.load("chat.yml");
        LB_CONFIG = HandyConfigUtil.load("lb.yml");
        ITEM_CONFIG = HandyConfigUtil.load("gui/item.yml");
        upConfig();
    }

    /**
     * 升级节点处理
     *
     * @since 1.0.7
     */
    public static void upConfig() {
        // 1.0.7 添加聊天频率提醒
        String language = "languages/" + BaseConstants.CONFIG.getString("language") + ".yml";
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "chatTime", "&7你必须等待 &a${chatTime} &7秒后 才可以继续发言.", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "itemNotFoundMsg", "&8[&c✘&8] &7展示物品超过可查看时间", null, language);
        // 1.1.4 频道不存在
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "channelDoesNotExist", "&8[&c✘&8] &7频道不存在", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "pluginChannel", "&8[&c✘&8] &7无法切换到该频道", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "noChannelPermission", "&8[&c✘&8] &7你没有 &a${permission} &7权限切换到该频道", null, language);
        // 1.1.5 私信提醒
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "tabHelp.message", "&7请输入私信内容", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "sendTell", "&8&o你悄悄地对 ${player} 说: ${message}", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "sendTellErrorMsg", "&8[&c✘&8] &7不能发送私信给自己", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "blacklistMsg", "&8[&c✘&8] &7请文明用语", null, language);
        // 1.2.9 喇叭参数提醒
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "lbParamFailureMsg", "&8[&c✘&8] &7参数错误 使用方法: &a/lb [喇叭类型] [消息内容]", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "lbConfigFailureMsg", "&8[&c✘&8] &7喇叭配置错误", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "lbEnableMsg", "&8[&c✘&8] &7该喇叭已经被管理员禁用", null, language);
        // 1.4.3 屏蔽相关
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "ignorePlayer", "&8[&a✔&8] &7您已屏蔽玩家&a${player}&7的全部消息", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "ignoreParamFailureMsg", "&8[&c✘&8] &7请指定需要屏蔽的玩家名称", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "ignoreListEmptyMsg", "&8[&c✘&8] &7未屏蔽玩家", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "ignoreListMsg", "&8[&a✔&8] &7屏蔽数量:&a  ${number}", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "ignoreSelfFailureMsg", "&8[&c✘&8] &7无法屏蔽自己", null, language);
        // 2.0.0 AI
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "aiText", "&8[&a!&8] &7玩家&a${player}&7因多次违规发言 &8[&a点击投票封禁&8]", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "voteSuccessMsg", "&8[&a✔&8] &7投票成功,当前票数&a${number}&7/&a${max}", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "hasVotedMsg", "&8[&a✔&8] &7您已投票,当前票数&a${number}&7/&a${max}", null, language);
        HandyConfigUtil.setPathIsNotContains(BaseConstants.LANG_CONFIG, "aiTip", "&8[&a!&8] &7请文明发言,多次违规将禁言!", null, language);
        HandyConfigUtil.loadLangConfig(true);
        // 1.0.7 添加聊天频率配置和黑名单配置
        HandyConfigUtil.setPathIsNotContains(BaseConstants.CONFIG, "blacklist", Arrays.asList("操", "草", "cao"), Collections.singletonList("黑名单,关键字替换成*"), "config.yml");
        HandyConfigUtil.setPathIsNotContains(BaseConstants.CONFIG, "chatTime.default", 0, Collections.singletonList("聊天冷却时间(单位秒)(可无限扩展和修改子节点，权限格式: playerChat.chatTime.vip1)"), "config.yml");
        HandyConfigUtil.setPathIsNotContains(BaseConstants.CONFIG, "chatTime.vip1", 0, null, "config.yml");
        HandyConfigUtil.setPathIsNotContains(BaseConstants.CONFIG, "chatTime.vip2", 0, null, "config.yml");
        HandyConfigUtil.setPathIsNotContains(BaseConstants.CONFIG, "chatTime.vip3", 0, null, "config.yml");
        // 1.2.6 增加登录后默认频道设置
        HandyConfigUtil.setPathIsNotContains(BaseConstants.CONFIG, "firstLoginChatDefault", "default", Collections.singletonList("玩家第一次登录后的默认频道"), "config.yml");
        // 1.3.6 私信简化
        HandyConfigUtil.setPathIsNotContains(BaseConstants.CONFIG, "tellAlias", Arrays.asList("/tell", "/msg"), Collections.singletonList("/plc tell 指令的简写"), "config.yml");
        HandyConfigUtil.loadConfig();
        // 1.0.9 at功能
        HandyConfigUtil.setPathIsNotContains(CHAT_CONFIG, "at.enable", true, Collections.singletonList("是否开启"), "chat.yml");
        HandyConfigUtil.setPathIsNotContains(CHAT_CONFIG, "at.sound", "BLOCK_ANVIL_LAND", Collections.singletonList("音效列表 https://bukkit.windit.net/javadoc/org/bukkit/Sound.html"), "chat.yml");
        // 1.1.2 展示物品支持配置格式
        HandyConfigUtil.setPathIsNotContains(CHAT_CONFIG, "item.content", "&5[&a展示了一个 &6${item} &a点击查看&5]", Collections.singletonList("内容格式"), "chat.yml");
        HandyConfigUtil.setPathIsNotContains(CHAT_CONFIG, "item.length", 6, Collections.singletonList("物品名称长度限制 多余的会显示为..."), "chat.yml");
        // 1.3.5 at功能配置
        HandyConfigUtil.setPathIsNotContains(CHAT_CONFIG, "at.keepAt", false, Collections.singletonList("是否保留@符号"), "chat.yml");
        HandyConfigUtil.setPathIsNotContains(CHAT_CONFIG, "at.atColor", "&9", Collections.singletonList("@默认的颜色"), "chat.yml");
        CHAT_CONFIG = HandyConfigUtil.load("chat.yml");
    }

}