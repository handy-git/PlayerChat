package cn.handyplus.chat.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 聊天工具测试
 *
 * @author handy
 */
public class ChatUtilTest {

    /**
     * 验证编辑距离对应的相似度百分比
     */
    @Test
    public void shouldCalculateExpectedSimilarity() {
        Assertions.assertEquals(100, ChatUtil.getSimilarity("测试消息", "测试消息"));
        Assertions.assertEquals(80, ChatUtil.getSimilarity("测试消息", "测试消息啊"));
        Assertions.assertEquals(80, ChatUtil.getSimilarity("测试消息啊", "测试消息"));
        Assertions.assertEquals(75, ChatUtil.getSimilarity("测试消息", "测试消信"));
        Assertions.assertEquals(0, ChatUtil.getSimilarity("测试消息", "完全不同"));
        Assertions.assertEquals(66, ChatUtil.getSimilarity("测试消息", " 测试消息."));
        Assertions.assertEquals(100, ChatUtil.getSimilarity("", ""));
        Assertions.assertEquals(0, ChatUtil.getSimilarity("", "测试"));
    }

}
