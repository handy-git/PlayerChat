package cn.handyplus.chat.hook;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.ChannelUtil;
import cn.handyplus.chat.enter.ChatPlayerChannelEnter;
import cn.handyplus.chat.enter.ChatPlayerHornEnter;
import cn.handyplus.chat.service.ChatPlayerChannelService;
import cn.handyplus.chat.service.ChatPlayerHornService;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * 变量扩展
 *
 * @author handy
 */
public class PlaceholderUtil extends PlaceholderExpansion {
    private final PlayerChat plugin;

    public PlaceholderUtil(PlayerChat plugin) {
        this.plugin = plugin;
    }

    /**
     * 变量前缀
     *
     * @return 结果
     */
    @Override
    public @NotNull String getIdentifier() {
        return "playerChat";
    }

    /**
     * 注册变量
     *
     * @param player      玩家
     * @param placeholder 变量字符串
     * @return 变量
     */
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String placeholder) {
        if (player == null) {
            return null;
        }
        // %playerChat_nick%
        if ("nick".equalsIgnoreCase(placeholder)) {
            return ChatConstants.PLAYER_CHAT_NICK.getOrDefault(player.getUniqueId(), player.getName());
        }
        // %playerChat_channel%
        if ("channel".equalsIgnoreCase(placeholder)) {
            String channelName = ChatConstants.PLAYER_CHAT_CHANNEL.get(player.getUniqueId());
            if (StrUtil.isEmpty(channelName)) {
                Optional<ChatPlayerChannelEnter> enterOptional = ChatPlayerChannelService.getInstance().findByUid(player.getUniqueId());
                channelName = enterOptional.isPresent() ? enterOptional.get().getChannel() : ChatConstants.DEFAULT;
            }
            return ChannelUtil.getChannelName(channelName);
        }
        // %playerChat_server_name%
        if ("server_name".equalsIgnoreCase(placeholder)) {
            return BaseUtil.replaceChatColor(BaseConstants.CONFIG.getString("serverName"));
        }
        // %playerChat_[类型]%
        Optional<ChatPlayerHornEnter> hornPlayerEnterOpt = ChatPlayerHornService.getInstance().findByUidAndType(player.getUniqueId(), placeholder);
        return hornPlayerEnterOpt.map(enter -> enter.getNumber().toString()).orElse("0");
    }

    /**
     * 因为这是一个内部类，
     * 你必须重写这个方法，让PlaceholderAPI知道不要注销你的扩展类
     *
     * @return 结果
     */
    @Override
    public boolean persist() {
        return true;
    }

    /**
     * 因为这是一个内部类，所以不需要进行这种检查
     * 我们可以简单地返回{@code true}
     *
     * @return 结果
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * 作者
     *
     * @return 结果
     */
    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * 版本
     *
     * @return 结果
     */
    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
