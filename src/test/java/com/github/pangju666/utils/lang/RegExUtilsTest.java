package com.github.pangju666.utils.lang;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

/**
 * 正则工具类, 只用于Unicode编码, 默认匹配空白字符
 *
 * @author nullptr
 * @version 1.0 2020-10-30
 * @since 1.1
 */
public final class RegExUtilsTest {

    @Test
    public void matchTest() {
        //String[] test = RegExUtils.match(RegExUtils.FLOAT, "-30.0adsad30.00");
        //System.out.println(Arrays.toString(test));
        Matcher matcher = RegExUtils.FLOAT.matcher("-30.0adsad30.00");
        matcher.find();
        System.out.println(matcher.group());
    }
}
