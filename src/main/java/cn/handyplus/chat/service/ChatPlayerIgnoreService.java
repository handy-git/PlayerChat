package cn.handyplus.chat.service;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.enter.ChatPlayerIgnoreEnter;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.db.Db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 玩家屏蔽
 *
 * @author handy
 */
public class ChatPlayerIgnoreService {

    private ChatPlayerIgnoreService() {
    }

    private static class SingletonHolder {
        private static final ChatPlayerIgnoreService INSTANCE = new ChatPlayerIgnoreService();
    }

    public static ChatPlayerIgnoreService getInstance() {
        return ChatPlayerIgnoreService.SingletonHolder.INSTANCE;
    }

    /**
     * 设置
     */
    public void setIgnore(ChatPlayerIgnoreEnter enter) {
        Optional<ChatPlayerIgnoreEnter> ignoreOptional = this.findByUid(enter.getPlayerUuid());
        if (!ignoreOptional.isPresent()) {
            this.add(enter);
        } else {
            ChatPlayerIgnoreEnter chatPlayerIgnoreEnter = ignoreOptional.get();
            List<String> ignorePlayerList = StrUtil.strToStrList(chatPlayerIgnoreEnter.getIgnorePlayer());
            ignorePlayerList.add(enter.getIgnorePlayer());
            chatPlayerIgnoreEnter.setIgnorePlayer(CollUtil.listToStr(ignorePlayerList));
        }
        // 重新缓存屏蔽列表
        ChatConstants.PLAYER_IGNORE_MAP.put(enter.getPlayerUuid(), ChatPlayerIgnoreService.getInstance().findIgnoreByUid(enter.getPlayerUuid()));
    }

    /**
     * 根据uid查询
     *
     * @param playerUuid uid
     * @return 数据
     */
    public List<String> findIgnoreByUid(UUID playerUuid) {
        return this.findByUid(playerUuid).map(ignoreEnter -> StrUtil.strToStrList(ignoreEnter.getIgnorePlayer())).orElse(new ArrayList<>());
    }

    /**
     * 移除屏蔽人
     */
    public void removeIgnore(UUID playerUuid, String ignorePlayer) {
        Optional<ChatPlayerIgnoreEnter> ignoreOptional = this.findByUid(playerUuid);
        if (!ignoreOptional.isPresent()) {
            return;
        }
        ChatPlayerIgnoreEnter chatPlayerIgnoreEnter = ignoreOptional.get();
        List<String> ignorePlayerList = StrUtil.strToStrList(chatPlayerIgnoreEnter.getIgnorePlayer());
        ignorePlayerList.remove(ignorePlayer);
        chatPlayerIgnoreEnter.setIgnorePlayer(CollUtil.listToStr(ignorePlayerList));
        if (CollUtil.isEmpty(ignorePlayerList)) {
            // 没有屏蔽人了直接删除
            Db.use(ChatPlayerIgnoreEnter.class).execution().deleteById(chatPlayerIgnoreEnter.getId());
        } else {
            // 还有屏蔽人就只更新
            Db<ChatPlayerIgnoreEnter> use = Db.use(ChatPlayerIgnoreEnter.class);
            use.update().set(ChatPlayerIgnoreEnter::getIgnorePlayer, CollUtil.listToStr(ignorePlayerList));
            use.execution().updateById(chatPlayerIgnoreEnter.getId());
        }
        // 重新缓存屏蔽列表
        ChatConstants.PLAYER_IGNORE_MAP.put(playerUuid, ChatPlayerIgnoreService.getInstance().findIgnoreByUid(playerUuid));
    }

    /**
     * 新增
     */
    private void add(ChatPlayerIgnoreEnter enter) {
        Db.use(ChatPlayerIgnoreEnter.class).execution().insert(enter);
    }

    /**
     * 根据uid查询
     *
     * @param playerUuid uid
     * @return 数据
     */
    private Optional<ChatPlayerIgnoreEnter> findByUid(UUID playerUuid) {
        Db<ChatPlayerIgnoreEnter> use = Db.use(ChatPlayerIgnoreEnter.class);
        use.where().eq(ChatPlayerIgnoreEnter::getPlayerUuid, playerUuid);
        return use.execution().selectOne();
    }

    /**
     * 查询全部
     *
     * @return list
     * @since 2.0.0
     */
    public List<ChatPlayerIgnoreEnter> findAll() {
        return Db.use(ChatPlayerIgnoreEnter.class).execution().list();
    }

}
