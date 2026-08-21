package cn.handyplus.chat.service;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.enter.ChatPlayerColorEnter;
import cn.handyplus.lib.db.Db;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 玩家聊天颜色服务
 *
 * @author handy
 */
public class ChatPlayerColorService {

    private ChatPlayerColorService() {
    }

    private static class SingletonHolder {
        private static final ChatPlayerColorService INSTANCE = new ChatPlayerColorService();
    }

    public static ChatPlayerColorService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 设置玩家颜色
     *
     * @param playerUuid 玩家UUID
     * @param playerName 玩家名称
     * @param type       类型
     * @param color      颜色
     */
    public void setColor(@NotNull UUID playerUuid, String playerName, @NotNull String type, @NotNull String color) {
        Optional<ChatPlayerColorEnter> colorEnterOpt = this.findByPlayerUuidAndType(playerUuid, type);
        if (colorEnterOpt.isPresent()) {
            Db<ChatPlayerColorEnter> db = Db.use(ChatPlayerColorEnter.class);
            db.update().set(ChatPlayerColorEnter::getColor, color)
                    .set(ChatPlayerColorEnter::getPlayerName, playerName);
            db.execution().updateById(colorEnterOpt.get().getId());
            return;
        }
        ChatPlayerColorEnter colorEnter = new ChatPlayerColorEnter();
        colorEnter.setPlayerName(playerName);
        colorEnter.setPlayerUuid(playerUuid);
        colorEnter.setType(type);
        colorEnter.setColor(color);
        add(colorEnter);
    }

    /**
     * 刷新玩家颜色缓存
     *
     * @param playerUuid 玩家UUID
     */
    public void refreshCache(@NotNull UUID playerUuid) {
        List<ChatPlayerColorEnter> colorList = this.findByPlayerUuid(playerUuid);
        Map<String, String> colorMap = colorList.stream().collect(Collectors.toMap(ChatPlayerColorEnter::getType, ChatPlayerColorEnter::getColor, (oldValue, newValue) -> newValue));
        ChatConstants.PLAYER_COLOR_CACHE.put(playerUuid, colorMap);
    }

    /**
     * 新增
     *
     * @param enter 实体
     */
    private void add(@NotNull ChatPlayerColorEnter enter) {
        Db.use(ChatPlayerColorEnter.class).execution().insert(enter);
    }

    /**
     * 根据玩家查询颜色
     *
     * @param playerUuid 玩家UUID
     * @return 颜色列表
     */
    private List<ChatPlayerColorEnter> findByPlayerUuid(@NotNull UUID playerUuid) {
        Db<ChatPlayerColorEnter> use = Db.use(ChatPlayerColorEnter.class);
        use.where().eq(ChatPlayerColorEnter::getPlayerUuid, playerUuid);
        return use.execution().list();
    }

    /**
     * 根据玩家和类型查询颜色
     *
     * @param playerUuid 玩家UUID
     * @param type       类型
     * @return 颜色信息
     */
    private Optional<ChatPlayerColorEnter> findByPlayerUuidAndType(@NotNull UUID playerUuid, @NotNull String type) {
        Db<ChatPlayerColorEnter> use = Db.use(ChatPlayerColorEnter.class);
        use.where().eq(ChatPlayerColorEnter::getPlayerUuid, playerUuid)
                .eq(ChatPlayerColorEnter::getType, type);
        return use.execution().selectOne();
    }

}
