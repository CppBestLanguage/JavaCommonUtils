package com.github.pangju666.utils.security;

import com.github.pangju666.utils.sys.RandomUtils;

/**
 * 验证码生成器
 *
 * @author 胖橘
 * @version 1.0 2020-3-18
 * @version 1.1 2020-3-31 增加生成器模式枚举和生成器接口
 * @since 1.0
 */
public final class VerificationCodeUtils {
    /** 验证码模式 */
    public enum Mode {
        /** 数字 */
        NUMBER,
        /** 字母 */
        LETTER,
        /** 混合 */
        MIX
    }

    private VerificationCodeUtils() {}

    /**
     * 获取验证码
     *
     * @param size 验证码位数
     * @since 1.1
     */
    public String generate(final int size) {
        return generate(size, Mode.MIX);
    }

    /**
     * 生成验证码
     *
     * @param size 验证码位数
     * @param mode 验证码模式
     * @return 验证码字符串
     * @since 1.1
     */
    public static String generate(final int size, final Mode mode) {
        StringBuilder builder = new StringBuilder();
        Generator generator = getGenerator(mode);
        for (int index = 0; index < size; index++) {
           builder.append(generator.generateCode());
        }
        return builder.toString();
    }

    /**
     * 获取生成器
     *
     * @param mode 验证码模式
     * @since 1.1
     */
    static Generator getGenerator(final Mode mode) {
        switch(mode) {
            case NUMBER: return RandomUtils::nextNumberCharacter;
            case MIX: return RandomUtils::nextLetterCharacter;
            default: return RandomUtils::nextLetterOrNumberCharacter;
        }
    }

    /**
     * 生成器
     */
    interface Generator {
        /**
         * 生成随机代码
         * @return 随即代码
         */
        Character generateCode();
    }
}
