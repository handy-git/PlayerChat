package cn.handyplus.chat.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 频道工具测试
 *
 * @author handy
 */
public class ChannelUtilTest {

    /**
     * 验证世界隔离键格式
     * 该值随跨服消息传递, 格式变更会导致不同版本子服之间的隔离失效
     */
    @Test
    public void shouldBuildExpectedWorldKey() {
        Assertions.assertEquals("lobby:world", ChannelUtil.buildWorldKey("lobby", "world"));
        Assertions.assertEquals("arena:arena_1", ChannelUtil.buildWorldKey("arena", "arena_1"));
        Assertions.assertEquals("lobby:arena_1", ChannelUtil.buildWorldKey("lobby", "arena_1"));
    }

}
