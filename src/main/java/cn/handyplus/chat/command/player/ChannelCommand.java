package cn.handyplus.chat.command.player;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.ChannelUtil;
import cn.handyplus.chat.service.ChatPlayerChannelService;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.command.HandyTab;
import cn.handyplus.lib.command.IHandyCommandEvent;
import cn.handyplus.lib.core.MapUtil;
import cn.handyplus.lib.util.AssertUtil;
import cn.handyplus.lib.util.BaseUtil;
import cn.handyplus.lib.util.HandyConfigUtil;
import cn.handyplus.lib.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 切换频道
 *
 * @author handy
 */
public class ChannelCommand implements IHandyCommandEvent {
    private static final String ADMIN_PERMISSION = "playerChat.reload";

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
    public void tab(HandyTab handyTab) {
        handyTab.next(context -> getChannelList(context.getSender()));
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String s, String[] args) {
        // 是否为玩家
        Player player = AssertUtil.notPlayer(sender, BaseUtil.getLangMsg("noPlayerFailureMsg"));
        String channel = this.getArg(args, 1, BaseUtil.getLangMsg("paramFailureMsg"));
        String channelName = setChannel(player, channel);
        MessageUtil.sendMessage(player, BaseUtil.getLangMsg("channelSwitchMsg", MapUtil.of("${channel}", channelName)));
    }

    /**
     * 设置玩家频道
     *
     * @param player  玩家
     * @param channel 频道
     * @return 频道显示名称
     */
    public static @NotNull String setChannel(@NotNull Player player, @NotNull String channel) {
        // 频道存在判断
        String chatChannel = ChannelUtil.isChannelEnable(channel);
        AssertUtil.notNull(chatChannel, BaseUtil.getLangMsg("channelDoesNotExist"));
        // 禁止切换到私信频道
        AssertUtil.notTrue(ChatConstants.TELL.equals(chatChannel), BaseUtil.getLangMsg("channelDoesNotExist"));
        // 插件注册频道处理
        List<String> pluginChannelList = ChatConstants.PLUGIN_CHANNEL.values().stream().distinct().collect(Collectors.toList());
        AssertUtil.notTrue(pluginChannelList.contains(channel), BaseUtil.getLangMsg("pluginChannel"));
        // 是否有频道权限
        String channelPermission = ChatConstants.PLAYER_CHAT_USE + chatChannel;
        AssertUtil.notTrue(!player.hasPermission(channelPermission), BaseUtil.getLangMsg("noChannelPermission", MapUtil.of("${permission}", channelPermission)));
        // 设置频道
        ChatPlayerChannelService.getInstance().setChannel(player.getUniqueId(), channel);
        return ChannelUtil.getChannelName(chatChannel);
    }

    /**
     * 获取可切换频道
     *
     * @param sender 发送人
     * @return 频道列表
     */
    public static @NotNull List<String> getChannelList(@NotNull CommandSender sender) {
        Set<String> chatChannelKeySet = HandyConfigUtil.getKey(ConfigUtil.CHAT_CONFIG, "chat");
        List<String> chatChannelList = new ArrayList<>(chatChannelKeySet);
        // 过滤私信频道
        chatChannelList = chatChannelList.stream().filter(s -> !ChatConstants.TELL.equals(s)).collect(Collectors.toList());
        // 只显示有权限的频道
        if (!sender.hasPermission(ADMIN_PERMISSION)) {
            chatChannelList = chatChannelList.stream().filter(s -> sender.hasPermission(ChatConstants.PLAYER_CHAT_USE + s)).collect(Collectors.toList());
        }
        // 过滤插件频道
        List<String> pluginChannelList = ChatConstants.PLUGIN_CHANNEL.values().stream().distinct().collect(Collectors.toList());
        return chatChannelList.stream().filter(s -> !pluginChannelList.contains(s)).collect(Collectors.toList());
    }

}
