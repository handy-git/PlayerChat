package cn.handyplus.chat.service;

import cn.handyplus.chat.enter.ChatPlayerAiEnter;
import cn.handyplus.lib.core.DateUtil;
import cn.handyplus.lib.db.Db;

import java.util.Optional;
import java.util.UUID;

/**
 * AI 审核
 *
 * @author handy
 */
public class ChatPlayerAiService {

    private ChatPlayerAiService() {
    }

    private static class SingletonHolder {
        private static final ChatPlayerAiService INSTANCE = new ChatPlayerAiService();
    }

    public static ChatPlayerAiService getInstance() {
        return ChatPlayerAiService.SingletonHolder.INSTANCE;
    }

    /**
     * 新增
     *
     * @return id
     */
    public int add(ChatPlayerAiEnter enter) {
        return Db.use(ChatPlayerAiEnter.class).execution().insert(enter);
    }

    /**
     * 查询今日AI审核次数
     *
     * @param playerUuid uid
     * @return 数据
     */
    public int count(UUID playerUuid) {
        Db<ChatPlayerAiEnter> use = Db.use(ChatPlayerAiEnter.class);
        use.where().eq(ChatPlayerAiEnter::getPlayerUuid, playerUuid)
                .ge(ChatPlayerAiEnter::getCreateTime, DateUtil.getToday());
        return use.execution().count();
    }

    /**
     * 更新结果
     *
     * @param id     id
     * @param result 结果
     */
    public void updateResult(Integer id, Boolean result) {
        Db<ChatPlayerAiEnter> use = Db.use(ChatPlayerAiEnter.class);
        use.update().set(ChatPlayerAiEnter::getResult, result);
        use.execution().updateById(id);
    }

    /**
     * 增加投票结果
     *
     * @param id id
     */
    public void addVoteNumber(Integer id) {
        Db<ChatPlayerAiEnter> use = Db.use(ChatPlayerAiEnter.class);
        use.update().add(ChatPlayerAiEnter::getVoteNumber, ChatPlayerAiEnter::getVoteNumber, 1);
        use.execution().updateById(id);
    }

    /**
     * 查询
     *
     * @param id id
     * @return 数据
     */
    public Optional<ChatPlayerAiEnter> findChatAi(Integer id) {
        Db<ChatPlayerAiEnter> use = Db.use(ChatPlayerAiEnter.class);
        use.where().eq(ChatPlayerAiEnter::getId, id)
                .eq(ChatPlayerAiEnter::getResult, false);
        return use.execution().selectOne();
    }

}