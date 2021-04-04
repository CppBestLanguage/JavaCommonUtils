package com.github.pangju666.utils.system;

import java.util.Random;
import java.util.UUID;

/**
 * 随机数工具类
 *
 * @author nullptr
 * @version 1.0 2020-3-31
 * @version 1.1 继承org.apache.commons.lang3.RandomUtils，增加获取随机中文和UUID
 * @since 1.0
 *
 * @see org.apache.commons.lang3.RandomUtils
 */
public class RandomUtils extends org.apache.commons.lang3.RandomUtils {
    protected static final Random RANDOM = new Random();

    private RandomUtils() {
    }

    /**
     * 生成指定范围内的数字
     *
     * @param bound 取值范围
     * @return 返回随机数字
     *
     * @since 1.0
     */
    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    /**
     * 生成指定范围内的字符
     *
     * @param min 最小取值
     * @param max 最大取值
     * @return 返回[min-max]之间的字符
     *
     * @since 1.0
     */
    public static char nextCharacter(char min, char max) {
        return (char) (nextInt(min, max));
    }

    /**
     * 生成随机数字字符
     *
     * @since 1.0
     */
    public static char nextNumber() {
        return nextCharacter('0', '9');
    }

    /**
     * 生成随机字母
     *
     * @since 1.0
     */
    public static char nextLetter() {
        return nextCharacter('A', 'z');
    }

    /**
     * 生成随机字母或数字
     *
     * @since 1.0
     */
    public static char nextLetterOrNumber() {
        if (RandomUtils.nextBoolean()) {
            return RandomUtils.nextCharacter('0', '9');
        } else {
            return RandomUtils.nextLetter();
        }
    }

    /**
     * 生成随机大写字母
     * @since 1.1
     */
    public static char nextUppercase() {
        return nextCharacter('A', 'Z');
    }

    /**
     * 生成随机小写字母
     * @since 1.1
     */
    public static char nextLowerCase() {
        return nextCharacter('a', 'z');
    }

    /**
     * 生成随机中文字符
     * @since 1.1
     */
    public static char nextChinese() {
        return nextCharacter('\u4e00', '\u9fa5');
    }

    /**
     * 生成随机的UUID
     * @since 1.1
     */
    public static String nexUUID() {
        return UUID.randomUUID().toString();
    }
}
