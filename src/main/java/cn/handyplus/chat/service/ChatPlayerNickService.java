package cn.handyplus.chat.service;

import cn.handyplus.chat.enter.ChatPlayerNickEnter;
import cn.handyplus.lib.db.Db;

import java.util.Optional;
import java.util.UUID;

/**
 * 玩家昵称服务
 *
 * @author handy
 */
public class ChatPlayerNickService {

    private ChatPlayerNickService() {
    }

    private static class SingletonHolder {
        private static final ChatPlayerNickService INSTANCE = new ChatPlayerNickService();
    }

    public static ChatPlayerNickService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 新增
     *
     * @param enter 实体
     */
    public void add(ChatPlayerNickEnter enter) {
        Db.use(ChatPlayerNickEnter.class).execution().insert(enter);
    }

    /**
     * 根据玩家查询昵称
     *
     * @param playerUuid 玩家UUID
     * @return 昵称信息
     */
    public Optional<ChatPlayerNickEnter> findByPlayerUuid(UUID playerUuid) {
        Db<ChatPlayerNickEnter> use = Db.use(ChatPlayerNickEnter.class);
        use.where().eq(ChatPlayerNickEnter::getPlayerUuid, playerUuid);
        return use.execution().selectOne();
    }

    /**
     * 设置玩家昵称
     *
     * @param playerUuid 玩家UUID
     * @param playerName 玩家名称
     * @param nickName   昵称
     */
    public void setNickName(UUID playerUuid, String playerName, String nickName) {
        Optional<ChatPlayerNickEnter> nickEnterOpt = this.findByPlayerUuid(playerUuid);
        if (nickEnterOpt.isPresent()) {
            // 更新昵称
            Db<ChatPlayerNickEnter> db = Db.use(ChatPlayerNickEnter.class);
            db.update().set(ChatPlayerNickEnter::getNickName, nickName);
            db.execution().updateById(nickEnterOpt.get().getId());
        } else {
            // 新增昵称记录
            ChatPlayerNickEnter nickEnter = new ChatPlayerNickEnter();
            nickEnter.setPlayerName(playerName);
            nickEnter.setPlayerUuid(playerUuid);
            nickEnter.setNickName(nickName);
            add(nickEnter);
        }
    }

}