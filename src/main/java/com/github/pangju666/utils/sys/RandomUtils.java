package com.github.pangju666.utils.sys;

import java.util.Random;
import java.util.UUID;

/**
 * 随机数工具类
 *
 * @author 胖橘
 * @version 1.0 2020-3-31
 * @since 1.0
 *
 * @see org.apache.commons.lang3.RandomUtils
 */
public class RandomUtils extends org.apache.commons.lang3.RandomUtils {
    private static final Integer ASCII_MIN = 0;
    private static final Integer ASCII_MAX = 127;
    protected static final Random RANDOM = new Random();

    protected RandomUtils() {
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
     * 生成指定范围内的字符(ascii表内)
     *
     * @return 返回随机字符
     *
     * @since 1.0
     */
    public static char nextCharacter() {
        return (char) (nextInt(ASCII_MIN, ASCII_MAX));
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
    public static char nextNumberCharacter() {
        return nextCharacter('0', '9');
    }

    /**
     * 生成随机字母
     *
     * @since 1.0
     */
    public static char nextLetterCharacter() {
        return nextCharacter('A', 'z');
    }

    /**
     * 生成随机字母或数字
     *
     * @since 1.0
     */
    public static char nextLetterOrNumberCharacter() {
        if (RandomUtils.nextBoolean()) {
            return RandomUtils.nextCharacter('0', '9');
        } else {
            return RandomUtils.nextLetterCharacter();
        }
    }

    /**
     * 生成随机大写字母
     *
     * @since 1.0
     */
    public static char nextUppercaseCharacter() {
        return nextCharacter('A', 'Z');
    }

    /**
     * 生成随机小写字母
     *
     * @since 1.0
     */
    public static char nextLowerCaseCharacter() {
        return nextCharacter('a', 'z');
    }

    /**
     * 生成随机特殊字符
     *
     * @since 1.0
     */
    public static char nextSpecialCharacter() {
        int range = nextInt(3);
        switch (range) {
            case 1: return RandomUtils.nextCharacter('!', '/');
            case 2: return RandomUtils.nextCharacter(':', '@');
            case 3: return RandomUtils.nextCharacter('[', '`');
            default: return RandomUtils.nextCharacter('{', '~');
        }
    }

    /**
     * 生成随机中文字符
     *
     * @since 1.0
     */
    public static char nextChineseCharacter() {
        return nextCharacter('\u4e00', '\u9fa5');
    }

    /**
     * 生成随机的UUID
     *
     * @since 1.0
     */
    public static String nexUUIDString() {
        return UUID.randomUUID().toString();
    }
}
