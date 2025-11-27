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
     * 查询
     *
     * @param id id
     * @return 数据
     */
    public Optional<ChatPlayerAiEnter> findById(Integer id) {
        return Db.use(ChatPlayerAiEnter.class).execution().selectById(id);
    }

}