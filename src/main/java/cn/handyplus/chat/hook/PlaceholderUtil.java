package cn.handyplus.chat.hook;

import cn.handyplus.chat.PlayerChat;
import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.core.ChannelUtil;
import cn.handyplus.chat.enter.ChatPlayerChannelEnter;
import cn.handyplus.chat.enter.ChatPlayerHornEnter;
import cn.handyplus.chat.service.ChatPlayerChannelService;
import cn.handyplus.chat.service.ChatPlayerHornService;
import cn.handyplus.lib.core.StrUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

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
    public String getIdentifier() {
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
    public String onRequest(OfflinePlayer player, String placeholder) {
        if (player == null) {
            return null;
        }
        // %playerChat_channel%
        if ("channel".equals(placeholder)) {
            String channelName = ChatConstants.CHANNEL_MAP.get(player.getUniqueId());
            if (StrUtil.isEmpty(channelName)) {
                ChatPlayerChannelEnter enter = ChatPlayerChannelService.getInstance().findByUid(player.getUniqueId());
                channelName = enter != null ? enter.getChannel() : "default";
            }
            return ChannelUtil.getChannelName(channelName);
        }
        // %playerChat_[类型]%
        ChatPlayerHornEnter hornPlayerEnter = ChatPlayerHornService.getInstance().findByUidAndType(player.getUniqueId(), placeholder);
        return hornPlayerEnter != null ? hornPlayerEnter.getNumber().toString() : "0";
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
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * 版本
     *
     * @return 结果
     */
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
