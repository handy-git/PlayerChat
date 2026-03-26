package cn.handyplus.chat.core;

import cn.handyplus.chat.param.ChatChildParam;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShortcutUtilTest {

    @Test
    public void shouldBuildQqDisplayFromShortcutConfig() {
        // 真实读取 shortcut.yml 中的 qq 节点，确保配置和提取逻辑联动正确。
        ChatChildParam display = buildDisplay("qq", "QQ 123456");

        Assertions.assertNotNull(display);
        Assertions.assertEquals("&8[&3&lQQ&8]", display.getText());
        Assertions.assertEquals("&3QQ: &b123456", display.getHover().get(1));
        Assertions.assertEquals("https://wpa.qq.com/msgrd?v=3&uin=123456&site=qq&menu=yes", display.getUrl());
    }

    @Test
    public void shouldBuildTpDisplayFromShortcutConfig() {
        ChatChildParam display = buildDisplay("tp", "tp我");

        Assertions.assertNotNull(display);
        Assertions.assertEquals("&8[&a&l点击传送&8]", display.getText());
        Assertions.assertEquals(Collections.singletonList("&7点击传送到 &e%player_name% &7身边"), display.getHover());
        Assertions.assertEquals("/tpa %player_name%", display.getClick());
        Assertions.assertNull(display.getClickSuggest());
        Assertions.assertNull(display.getUrl());
    }

    @Test
    public void shouldBuildGlowIpDisplayFromShortcutConfig() {
        ChatChildParam display = buildDisplay("glowIP", "127.0.0.1");

        Assertions.assertNotNull(display);
        // glowIP 依赖 text-filter 提取 {0}，这里验证替换结果不是原始占位符。
        Assertions.assertEquals("&e&n127.0.0.1", display.getText());
        Assertions.assertEquals("127.0.0.1", display.getClick());
    }

    @Test
    public void shouldBuildGlowEmailDisplayFromShortcutConfig() {
        ChatChildParam display = buildDisplay("glowEmail", "test@example.com");

        Assertions.assertNotNull(display);
        Assertions.assertEquals("&e&ntest@example.com", display.getText());
        Assertions.assertEquals("test@example.com", display.getClick());
    }

    @Test
    public void shouldBuildLandmarkDisplayFromShortcutConfig() {
        ChatChildParam display = buildDisplay("landmark", "#地标 123");

        Assertions.assertNotNull(display);
        Assertions.assertEquals("&8[&a&l传送地标 &e123&8]", display.getText());
        Assertions.assertEquals("/plw tpid 123", display.getClick());
    }

    @Test
    public void shouldReturnNullWhenWholeMessageDoesNotMatchConfigPattern() {
        // pattern 仍然是整句匹配，消息前后多出内容时不应命中。
        ChatChildParam display = buildDisplay("qq", "这是 QQ 123456");

        Assertions.assertNull(display);
    }

    @Test
    public void shouldExtractFirstPartialRegexMatch() {
        List<String> vars = ShortcutUtil.extractRegexVars("BV 1abcDEF234 xyz", "[0-9A-Za-z]{10}", "bilibili.text-filter");

        Assertions.assertEquals(Collections.singletonList("1abcDEF234"), vars);
    }

    @Test
    public void shouldExtractFullMatchAndGroups() {
        List<String> vars = ShortcutUtil.extractRegexVars("vip18", "vip(\\d+)", "vip.text-filter");

        Assertions.assertEquals(Arrays.asList("vip18", "18"), vars);
    }

    @Test
    public void shouldReturnNullWhenRegexDoesNotMatch() {
        List<String> vars = ShortcutUtil.extractRegexVars("abc", "\\d+", "number.text-filter");

        Assertions.assertNull(vars);
    }

    @Test
    public void shouldReturnNullWhenRegexIsInvalid() {
        List<String> vars = ShortcutUtil.extractRegexVars("abc", "[", "invalid.text-filter");

        Assertions.assertNull(vars);
    }

    @Test
    public void shouldReplaceAllTemplateVarsInStringAndList() {
        List<String> vars = Arrays.asList("123456", "12");

        Assertions.assertEquals("QQ-123456-12", ShortcutUtil.replaceTextFilterVar("QQ-{0}-{1}", vars));
        Assertions.assertEquals(Arrays.asList("A-123456", "B-12"), ShortcutUtil.replaceTextFilterVar(Arrays.asList("A-{0}", "B-{1}"), vars));
    }

    private static ChatChildParam buildDisplay(String key, String message) {
        // 测试直接复用资源目录下的 shortcut.yml，避免手写一份测试配置后与正式配置脱节。
        FileConfiguration shortcutConfig = loadShortcutConfig();
        return ShortcutUtil.buildShortcutDisplay(message,
                shortcutConfig.getString(key + ".pattern", ""),
                shortcutConfig.getString(key + ".text-filter"),
                key,
                shortcutConfig.getString(key + ".display.text"),
                shortcutConfig.getStringList(key + ".display.hover"),
                shortcutConfig.getString(key + ".display.click"),
                shortcutConfig.getString(key + ".display.clickSuggest"),
                shortcutConfig.getString(key + ".display.url"));
    }

    private static FileConfiguration loadShortcutConfig() {
        InputStream inputStream = ShortcutUtilTest.class.getClassLoader().getResourceAsStream("shortcut.yml");
        Assertions.assertNotNull(inputStream, "未找到 shortcut.yml 测试资源");
        // 明确按 UTF-8 读取，避免中文注释或内容在不同环境下出现编码问题。
        return YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

}
