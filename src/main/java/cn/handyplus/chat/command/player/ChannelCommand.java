package cn.handyplus.chat.command.player;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.service.ChatPlayerChannelService;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.HandyConfigUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 切换渠道
 *
 * @author handy
 */
public class ChannelCommand implements IHandyCommandEvent {

    @Override
    public String command() {
        return "channel";
    }

    @Override
    public String permission() {
        return "playerChat.channel";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 参数是否正常
        AssertUtil.notTrue(args.length < 2, sender, ConfigUtil.LANG_CONFIG.getString("paramFailureMsg"));
        // 是否为玩家
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        String channel = args[1];
        // 渠道存在判断
        Map<String, Object> chatChannel = HandyConfigUtil.getChildMap(ConfigUtil.CHAT_CONFIG, "chat");
        AssertUtil.isTrue(chatChannel.containsKey(channel), sender, "没有渠道");
        // 插件注册渠道处理
        List<String> pluginChannelList = ChatConstants.PLUGIN_CHANNEL.values().stream().distinct().collect(Collectors.toList());
        AssertUtil.notTrue(pluginChannelList.contains(channel), sender, "无法切换到该渠道");
        // 设置渠道
        ChatPlayerChannelService.getInstance().setChannel(player.getUniqueId(), channel);
    }

}