package io.github.hefrankeleyn.hefconfig.client.util;

import io.github.hefrankeleyn.hefconfig.client.utils.PlaceholderHelper;
import org.junit.Test;

import java.util.Set;

/**
 * @Date 2024/6/3
 * @Author lifei
 */
public class PlaceholderHelperTest {

    @Test
    public void test01() {
        PlaceholderHelper placeholderHelper = PlaceholderHelper.instance();
        String spel01 = "${hh.aa}";
        String spel02 = "${${hh.bb:123def}}";
        String spel03 = "${some.key:${some.other.key:100}}";
        String spel04 = "${${some.key:other.key}}";
        String spel05 = "${${some.key}:${another.key}}";
        String spel06 = "#{new java.text.SimpleDateFormat('${some.key}').parse('${another.key}')}";
        System.out.println(placeholderHelper.extractPlaceholderKeys(spel01));
        System.out.println(placeholderHelper.extractPlaceholderKeys(spel02));
        System.out.println(placeholderHelper.extractPlaceholderKeys(spel03));
        System.out.println(placeholderHelper.extractPlaceholderKeys(spel04));
        System.out.println(placeholderHelper.extractPlaceholderKeys(spel05));
        System.out.println(placeholderHelper.extractPlaceholderKeys(spel06));
    }
}
