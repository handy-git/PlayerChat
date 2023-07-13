package cn.handyplus.chat.service;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.enter.ChatPlayerChannelEnter;
import cn.handyplus.lib.db.Db;

import java.util.List;
import java.util.UUID;

/**
 * 玩家渠道
 *
 * @author handy
 */
public class ChatPlayerChannelService {
    private ChatPlayerChannelService() {
    }

    private static class SingletonHolder {
        private static final ChatPlayerChannelService INSTANCE = new ChatPlayerChannelService();
    }

    public static ChatPlayerChannelService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 新增
     */
    public void add(ChatPlayerChannelEnter enter) {
        Db.use(ChatPlayerChannelEnter.class).execution().insert(enter);
    }

    /**
     * 根据uid查询
     *
     * @param playerUuid uid
     * @return 数据
     */
    public ChatPlayerChannelEnter findByUid(UUID playerUuid) {
        Db<ChatPlayerChannelEnter> use = Db.use(ChatPlayerChannelEnter.class);
        use.where().eq(ChatPlayerChannelEnter::getPlayerUuid, playerUuid);
        return use.execution().selectOne();
    }

    /**
     * 根据playerUuid设置渠道
     *
     * @param playerUuid uid
     * @param channel    渠道
     * @since 1.0.6
     */
    public boolean setChannel(UUID playerUuid, String channel) {
        Db<ChatPlayerChannelEnter> db = Db.use(ChatPlayerChannelEnter.class);
        db.update().set(ChatPlayerChannelEnter::getChannel, channel);
        db.where().eq(ChatPlayerChannelEnter::getPlayerUuid, playerUuid);
        int update = db.execution().update();
        // 重新缓存数据
        ChatConstants.PLAYER_CHAT_CHANNEL.put(playerUuid, channel);
        return update > 0;
    }

    /**
     * 根据渠道查询
     *
     * @param channel 渠道
     * @return 数据
     * @since 1.0.6
     */
    public List<ChatPlayerChannelEnter> findByChannel(String channel) {
        Db<ChatPlayerChannelEnter> use = Db.use(ChatPlayerChannelEnter.class);
        use.where().eq(ChatPlayerChannelEnter::getChannel, channel);
        return use.execution().list();
    }

    /**
     * 根据playerUuid设置渠道
     *
     * @param channel    渠道
     * @param newChannel 新渠道
     * @since 1.0.6
     */
    public boolean setChannel(String channel, String newChannel) {
        Db<ChatPlayerChannelEnter> db = Db.use(ChatPlayerChannelEnter.class);
        db.update().set(ChatPlayerChannelEnter::getChannel, newChannel);
        db.where().eq(ChatPlayerChannelEnter::getChannel, channel);
        return db.execution().update() > 0;
    }

}