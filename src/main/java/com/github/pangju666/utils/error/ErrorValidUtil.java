package com.github.pangju666.utils.error;

/**
 * 错误验证工具
 */
public class ErrorValidUtil {
    private ErrorValidUtil() {
    }

    /**
     * 判断是否存在错误
     *
     * @param condition 判断条件, 为假则说明存在错误
     * @param reason 错误原因
     * @throws Exception 判断条件为假时抛出以异常
     */
    public static void isExistError(boolean condition, String reason) throws Exception {
        if (!condition) {
            throw new Exception(reason);
        }
    }
}
