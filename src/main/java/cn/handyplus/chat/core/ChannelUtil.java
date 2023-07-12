package cn.handyplus.chat.core;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.util.ConfigUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;

/**
 * 渠道处理
 *
 * @author handy
 * @since 1.0.6
 */
public class ChannelUtil {

    /**
     * 获取渠道
     *
     * @param channel 渠道
     * @return true开启
     */
    public static String isChannelEnable(String channel) {
        // chat自带渠道
        boolean chatEnable = ConfigUtil.CHAT_CONFIG.getBoolean("chat." + channel + ".enable");
        if (chatEnable) {
            return channel;
        }
        // 第三方插件渠道
        String pluginChannel = ChatConstants.PLUGIN_CHANNEL.get(channel);
        if (StrUtil.isEmpty(pluginChannel)) {
            return null;
        }
        // 第三方插件渠道是否启用
        boolean pluginChannelEnable = ConfigUtil.CHAT_CONFIG.getBoolean("chat." + pluginChannel + ".enable");
        if (!pluginChannelEnable) {
            return null;
        }
        return pluginChannel;
    }

    /**
     * 获取渠道
     *
     * @param channel 渠道
     * @return channel名称
     */
    public static String getChannelName(String channel) {
        String channelEnable = isChannelEnable(channel);
        String pluginChannel = ConfigUtil.CHAT_CONFIG.getString("chat." + channelEnable + ".name", channelEnable);
        return BaseUtil.replaceChatColor(pluginChannel);
    }

}
