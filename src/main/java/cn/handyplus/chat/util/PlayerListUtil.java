package cn.handyplus.chat.util;

import cn.handyplus.chat.constants.ChatConstants;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

/**
 * 全服在线玩家列表工具
 *
 * @author handy
 */
public final class PlayerListUtil {

    private PlayerListUtil() {
    }

    /**
     * 使用 BC 返回的完整在线玩家列表替换本地快照
     *
     * @param playerList BC 全服在线玩家列表
     */
    public static synchronized void replaceOnlinePlayers(@NotNull List<String> playerList) {
        ChatConstants.PLAYER_LIST = Collections.unmodifiableList(new ArrayList<>(new LinkedHashSet<>(playerList)));
    }

    /**
     * 将本服登录玩家加入在线玩家快照
     *
     * @param playerName 玩家名
     */
    public static synchronized void addOnlinePlayer(@NotNull String playerName) {
        if (getOnlinePlayerName(playerName).isPresent()) {
            return;
        }
        List<String> playerList = new ArrayList<>(ChatConstants.PLAYER_LIST);
        playerList.add(playerName);
        replaceOnlinePlayers(playerList);
    }

    /**
     * 将本服退出玩家移出在线玩家快照
     *
     * @param playerName 玩家名
     */
    public static synchronized void removeOnlinePlayer(@NotNull String playerName) {
        List<String> playerList = new ArrayList<>(ChatConstants.PLAYER_LIST);
        playerList.removeIf(name -> name.equalsIgnoreCase(playerName));
        replaceOnlinePlayers(playerList);
    }

    /**
     * 获取 BC 在线玩家的规范名称
     *
     * @param playerName 玩家名
     * @return 在线玩家名称
     */
    public static Optional<String> getOnlinePlayerName(@NotNull String playerName) {
        return ChatConstants.PLAYER_LIST.stream().filter(name -> name.equalsIgnoreCase(playerName)).findFirst();
    }

}
