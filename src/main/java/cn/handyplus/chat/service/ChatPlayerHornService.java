package cn.handyplus.chat.service;

import cn.handyplus.chat.enter.ChatPlayerHornEnter;
import cn.handyplus.lib.db.Db;

import java.util.List;
import java.util.UUID;

/**
 * 玩家喇叭
 *
 * @author handy
 */
public class ChatPlayerHornService {
    private ChatPlayerHornService() {
    }

    private static class SingletonHolder {
        private static final ChatPlayerHornService INSTANCE = new ChatPlayerHornService();
    }

    public static ChatPlayerHornService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 新增
     */
    public void add(ChatPlayerHornEnter enter) {
        Db.use(ChatPlayerHornEnter.class).execution().insert(enter);
    }

    /**
     * 根据uid和类型查询
     *
     * @param playerUuid uid
     * @param type       类型
     * @return 数据
     */
    public ChatPlayerHornEnter findByUidAndType(UUID playerUuid, String type) {
        Db<ChatPlayerHornEnter> use = Db.use(ChatPlayerHornEnter.class);
        use.where().eq(ChatPlayerHornEnter::getPlayerUuid, playerUuid)
                .eq(ChatPlayerHornEnter::getType, type);
        return use.execution().selectOne();
    }

    /**
     * 根据uid查询
     *
     * @param playerUuid uid
     * @return 数据
     */
    public List<ChatPlayerHornEnter> findByUid(UUID playerUuid) {
        Db<ChatPlayerHornEnter> use = Db.use(ChatPlayerHornEnter.class);
        use.where().eq(ChatPlayerHornEnter::getPlayerUuid, playerUuid);
        return use.execution().list();
    }

    /**
     * 根据id 减少数量
     *
     * @param id ID
     */
    public void subtractNumber(Integer id, int number) {
        Db<ChatPlayerHornEnter> db = Db.use(ChatPlayerHornEnter.class);
        db.update().subtract(ChatPlayerHornEnter::getNumber, ChatPlayerHornEnter::getNumber, number);
        db.execution().updateById(id);
    }

    /**
     * 根据id 设置数量
     *
     * @param id ID
     */
    public void setNumber(Integer id, int number) {
        Db<ChatPlayerHornEnter> db = Db.use(ChatPlayerHornEnter.class);
        db.update().set(ChatPlayerHornEnter::getNumber, number);
        db.execution().updateById(id);
    }

    /**
     * 根据id 增加数量
     *
     * @param id ID
     */
    public void addNumber(Integer id, int number) {
        Db<ChatPlayerHornEnter> db = Db.use(ChatPlayerHornEnter.class);
        db.update().add(ChatPlayerHornEnter::getNumber, ChatPlayerHornEnter::getNumber, number);
        db.execution().updateById(id);
    }

}