package cn.handyplus.chat.service;

import cn.handyplus.chat.enter.ChatPlayerMuteEnter;
import cn.handyplus.lib.db.Db;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 玩家禁言
 *
 * @author handy
 */
public class ChatPlayerMuteService {

    private ChatPlayerMuteService() {
    }

    private static class SingletonHolder {
        private static final ChatPlayerMuteService INSTANCE = new ChatPlayerMuteService();
    }

    public static ChatPlayerMuteService getInstance() {
        return ChatPlayerMuteService.SingletonHolder.INSTANCE;
    }

    /**
     * 新增
     *
     * @param enter 实体
     * @return id
     */
    public int add(ChatPlayerMuteEnter enter) {
        return Db.use(ChatPlayerMuteEnter.class).execution().insert(enter);
    }

    /**
     * 查询玩家有效禁言
     *
     * @param playerUuid 玩家uuid
     * @return 禁言记录
     */
    public Optional<ChatPlayerMuteEnter> findActiveMute(UUID playerUuid) {
        Db<ChatPlayerMuteEnter> use = Db.use(ChatPlayerMuteEnter.class);
        use.where().eq(ChatPlayerMuteEnter::getPlayerUuid, playerUuid)
                .gt(ChatPlayerMuteEnter::getExpireTime, new Date());
        return use.execution().selectOne();
    }

    /**
     * 删除玩家禁言
     *
     * @param playerUuid 玩家uuid
     * @return 删除数量
     */
    public int removeByUuid(UUID playerUuid) {
        Db<ChatPlayerMuteEnter> use = Db.use(ChatPlayerMuteEnter.class);
        use.where().eq(ChatPlayerMuteEnter::getPlayerUuid, playerUuid);
        return use.execution().delete();
    }

    /**
     * 查询全部
     *
     * @return list
     */
    public List<ChatPlayerMuteEnter> findAll() {
        return Db.use(ChatPlayerMuteEnter.class).execution().list();
    }

}
