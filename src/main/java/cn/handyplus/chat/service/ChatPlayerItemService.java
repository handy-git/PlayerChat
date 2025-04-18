package cn.handyplus.chat.service;

import cn.handyplus.chat.enter.ChatPlayerItemEnter;
import cn.handyplus.lib.core.DateUtil;
import cn.handyplus.lib.db.Db;

import java.util.Date;
import java.util.Optional;

/**
 * @author handy
 */
public class ChatPlayerItemService {

    private ChatPlayerItemService() {
    }

    private static class SingletonHolder {
        private static final ChatPlayerItemService INSTANCE = new ChatPlayerItemService();
    }

    public static ChatPlayerItemService getInstance() {
        return ChatPlayerItemService.SingletonHolder.INSTANCE;
    }

    /**
     * 新增
     */
    public Integer add(ChatPlayerItemEnter enter) {
        return Db.use(ChatPlayerItemEnter.class).execution().insert(enter);
    }

    /**
     * 根据id查询
     *
     * @param id id
     * @return 数据
     */
    public Optional<ChatPlayerItemEnter> findById(Integer id) {
        return Db.use(ChatPlayerItemEnter.class).execution().selectById(id);
    }

    /**
     * 清理之前数据
     *
     * @return 清理数量
     * @since 1.1.0
     */
    public int clearItemData() {
        Db<ChatPlayerItemEnter> db = Db.use(ChatPlayerItemEnter.class);
        db.where().le(ChatPlayerItemEnter::getCreateTime, DateUtil.addDate(new Date(), -1));
        return db.execution().delete();
    }

}
