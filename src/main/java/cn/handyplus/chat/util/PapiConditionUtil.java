package cn.handyplus.chat.util;

import cn.handyplus.chat.constants.ChatConstants;
import cn.handyplus.chat.hook.PlaceholderApiUtil;
import cn.handyplus.lib.constants.BaseConstants;
import cn.handyplus.lib.core.CollUtil;
import cn.handyplus.lib.core.StrUtil;
import cn.handyplus.lib.util.BaseUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

/**
 * PAPI变量条件工具
 *
 * @author handy
 */
public final class PapiConditionUtil {

    private PapiConditionUtil() {
    }

    /**
     * 判断条件列表是否全部满足
     *
     * @param player        玩家
     * @param channelName   频道名称
     * @param conditionList 条件列表
     * @return true 满足
     */
    public static boolean checkCondition(@NotNull Player player, @NotNull String channelName, @Nullable List<String> conditionList) {
        if (CollUtil.isEmpty(conditionList)) {
            return true;
        }
        for (String condition : conditionList) {
            if (StrUtil.isEmpty(condition)) {
                continue;
            }
            if (!checkCondition(player, channelName, condition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断单条条件是否满足
     *
     * @param player      玩家
     * @param channelName 频道名称
     * @param condition   条件
     * @return true 满足
     */
    private static boolean checkCondition(@NotNull Player player, @NotNull String channelName, @NotNull String condition) {
        condition = replaceConditionVar(player, channelName, condition);
        String operator = getConditionOperator(condition);
        if (StrUtil.isEmpty(operator)) {
            return false;
        }
        String[] values = condition.split(Pattern.quote(operator), 2);
        if (values.length != 2) {
            return false;
        }
        String left = values[0].trim();
        String right = values[1].trim();
        if (StrUtil.isEmpty(left) || StrUtil.isEmpty(right)) {
            return false;
        }
        if ("=".equals(operator)) {
            return left.equals(right);
        }
        if ("!=".equals(operator)) {
            return !left.equals(right);
        }
        return compareNumber(left, right, operator);
    }

    /**
     * 解析条件中的变量
     *
     * @param player      玩家
     * @param channelName 频道名称
     * @param condition   条件
     * @return 解析后条件
     */
    private static String replaceConditionVar(@NotNull Player player, @NotNull String channelName, @NotNull String condition) {
        condition = StrUtil.replace(condition, "channel", channelName);
        condition = StrUtil.replace(condition, "player", player.getName());
        condition = StrUtil.replace(condition, "nickName", ChatConstants.PLAYER_NICK_CACHE.getOrDefault(player.getUniqueId(), player.getName()));
        condition = StrUtil.replace(condition, "serverName", BaseConstants.CONFIG.getString("serverName"));
        condition = BaseUtil.headComponent(condition, player.getName());
        return PlaceholderApiUtil.set(player, condition);
    }

    /**
     * 获取条件比较符
     *
     * @param condition 条件
     * @return 比较符
     */
    private static @Nullable String getConditionOperator(@NotNull String condition) {
        String[] operators = {">=", "<=", "!=", "=", ">", "<"};
        for (String operator : operators) {
            if (condition.contains(operator)) {
                return operator;
            }
        }
        return null;
    }

    /**
     * 比较数字条件
     *
     * @param left     左值
     * @param right    右值
     * @param operator 比较符
     * @return true 满足
     */
    private static boolean compareNumber(@NotNull String left, @NotNull String right, @NotNull String operator) {
        BigDecimal leftNumber;
        BigDecimal rightNumber;
        try {
            leftNumber = new BigDecimal(left);
            rightNumber = new BigDecimal(right);
        } catch (NumberFormatException e) {
            return false;
        }
        int compare = leftNumber.compareTo(rightNumber);
        if (">=".equals(operator)) {
            return compare >= 0;
        }
        if ("<=".equals(operator)) {
            return compare <= 0;
        }
        if (">".equals(operator)) {
            return compare > 0;
        }
        if ("<".equals(operator)) {
            return compare < 0;
        }
        return false;
    }

}
