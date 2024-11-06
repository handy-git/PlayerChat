package cn.handyplus.chat.command.player;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.ChannelUtil;
import cn.handyplus.chat.service.ChatPlayerChannelService;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 切换频道
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
        AssertUtil.notTrue(args.length < 2, sender, BaseUtil.getMsgNotColor("paramFailureMsg"));
        // 是否为玩家
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        String channel = args[1];
        // 频道存在判断
        String chatChannel = ChannelUtil.isChannelEnable(channel);
        AssertUtil.notNull(chatChannel, sender, BaseUtil.getMsgNotColor("channelDoesNotExist"));
        // 插件注册频道处理
        List<String> pluginChannelList = ChatConstants.PLUGIN_CHANNEL.values().stream().distinct().collect(Collectors.toList());
        AssertUtil.notTrue(pluginChannelList.contains(channel), sender, BaseUtil.getMsgNotColor("pluginChannel"));
        // 是否有频道权限
        String channelPermission = ChatConstants.PLAYER_CHAT_USE + chatChannel;
        AssertUtil.notTrue(!player.hasPermission(channelPermission), sender, BaseUtil.getMsgNotColor("noChannelPermission", MapUtil.of("${permission}", channelPermission)));
        // 设置频道
        ChatPlayerChannelService.getInstance().setChannel(player.getUniqueId(), channel);
    }

}