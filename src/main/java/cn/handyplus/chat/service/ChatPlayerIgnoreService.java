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
     *
     * @param enter 玩家屏蔽记录
     */
    public void setIgnore(ChatPlayerIgnoreEnter enter) {
        Optional<ChatPlayerIgnoreEnter> ignoreOptional = this.findByUid(enter.getPlayerUuid());
        if (!ignoreOptional.isPresent()) {
            this.add(enter);
        } else {
            ChatPlayerIgnoreEnter chatPlayerIgnoreEnter = ignoreOptional.get();
            List<String> ignorePlayerList = StrUtil.strToStrList(chatPlayerIgnoreEnter.getIgnorePlayer());
            if (ignorePlayerList.contains(enter.getIgnorePlayer())) {
                this.refreshCache(enter.getPlayerUuid());
                return;
            }
            ignorePlayerList.add(enter.getIgnorePlayer());
            chatPlayerIgnoreEnter.setIgnorePlayer(CollUtil.listToStr(ignorePlayerList));
            Db<ChatPlayerIgnoreEnter> use = Db.use(ChatPlayerIgnoreEnter.class);
            use.update().set(ChatPlayerIgnoreEnter::getIgnorePlayer, chatPlayerIgnoreEnter.getIgnorePlayer());
            use.execution().updateById(chatPlayerIgnoreEnter.getId());
        }
        // 重新缓存屏蔽列表
        this.refreshCache(enter.getPlayerUuid());
    }

    /**
     * 设置白名单
     *
     * @param enter 玩家屏蔽记录
     */
    public void setWhite(ChatPlayerIgnoreEnter enter) {
        Optional<ChatPlayerIgnoreEnter> ignoreOptional = this.findByUid(enter.getPlayerUuid());
        if (!ignoreOptional.isPresent()) {
            this.add(enter);
        } else {
            ChatPlayerIgnoreEnter chatPlayerIgnoreEnter = ignoreOptional.get();
            List<String> whitePlayerList = StrUtil.strToStrList(chatPlayerIgnoreEnter.getWhitePlayer());
            if (whitePlayerList.contains(enter.getWhitePlayer())) {
                this.refreshCache(enter.getPlayerUuid());
                return;
            }
            whitePlayerList.add(enter.getWhitePlayer());
            Db<ChatPlayerIgnoreEnter> use = Db.use(ChatPlayerIgnoreEnter.class);
            use.update().set(ChatPlayerIgnoreEnter::getWhitePlayer, CollUtil.listToStr(whitePlayerList));
            use.execution().updateById(chatPlayerIgnoreEnter.getId());
        }
        // 重新缓存屏蔽列表
        this.refreshCache(enter.getPlayerUuid());
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
     * 根据uid查询白名单
     *
     * @param playerUuid uid
     * @return 数据
     */
    public List<String> findWhiteByUid(UUID playerUuid) {
        return this.findByUid(playerUuid).map(ignoreEnter -> StrUtil.strToStrList(ignoreEnter.getWhitePlayer())).orElse(new ArrayList<>());
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
        List<String> whitePlayerList = StrUtil.strToStrList(chatPlayerIgnoreEnter.getWhitePlayer());
        if (CollUtil.isEmpty(ignorePlayerList) && CollUtil.isEmpty(whitePlayerList)) {
            // 没有屏蔽人和白名单了直接删除
            Db.use(ChatPlayerIgnoreEnter.class).execution().deleteById(chatPlayerIgnoreEnter.getId());
        } else {
            // 还有屏蔽人就只更新
            Db<ChatPlayerIgnoreEnter> use = Db.use(ChatPlayerIgnoreEnter.class);
            use.update().set(ChatPlayerIgnoreEnter::getIgnorePlayer, CollUtil.listToStr(ignorePlayerList));
            use.execution().updateById(chatPlayerIgnoreEnter.getId());
        }
        // 重新缓存屏蔽列表
        this.refreshCache(playerUuid);
    }

    /**
     * 移除白名单
     *
     * @param playerUuid  玩家uuid
     * @param whitePlayer 白名单玩家
     */
    public void removeWhite(UUID playerUuid, String whitePlayer) {
        Optional<ChatPlayerIgnoreEnter> ignoreOptional = this.findByUid(playerUuid);
        if (!ignoreOptional.isPresent()) {
            return;
        }
        ChatPlayerIgnoreEnter chatPlayerIgnoreEnter = ignoreOptional.get();
        List<String> whitePlayerList = StrUtil.strToStrList(chatPlayerIgnoreEnter.getWhitePlayer());
        whitePlayerList.remove(whitePlayer);
        List<String> ignorePlayerList = StrUtil.strToStrList(chatPlayerIgnoreEnter.getIgnorePlayer());
        if (CollUtil.isEmpty(ignorePlayerList) && CollUtil.isEmpty(whitePlayerList)) {
            // 没有屏蔽人和白名单了直接删除
            Db.use(ChatPlayerIgnoreEnter.class).execution().deleteById(chatPlayerIgnoreEnter.getId());
        } else {
            Db<ChatPlayerIgnoreEnter> use = Db.use(ChatPlayerIgnoreEnter.class);
            use.update().set(ChatPlayerIgnoreEnter::getWhitePlayer, CollUtil.listToStr(whitePlayerList));
            use.execution().updateById(chatPlayerIgnoreEnter.getId());
        }
        // 重新缓存屏蔽列表
        this.refreshCache(playerUuid);
    }

    /**
     * 重新缓存屏蔽列表
     *
     * @param playerUuid 玩家uuid
     */
    private void refreshCache(UUID playerUuid) {
        Optional<ChatPlayerIgnoreEnter> ignoreOptional = this.findByUid(playerUuid);
        if (!ignoreOptional.isPresent()) {
            ChatConstants.PLAYER_IGNORE_MAP.put(playerUuid, new ArrayList<>());
            ChatConstants.PLAYER_IGNORE_WHITE_MAP.put(playerUuid, new ArrayList<>());
            return;
        }
        ChatPlayerIgnoreEnter ignoreEnter = ignoreOptional.get();
        ChatConstants.PLAYER_IGNORE_MAP.put(playerUuid, StrUtil.strToStrList(ignoreEnter.getIgnorePlayer()));
        ChatConstants.PLAYER_IGNORE_WHITE_MAP.put(playerUuid, StrUtil.strToStrList(ignoreEnter.getWhitePlayer()));
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
