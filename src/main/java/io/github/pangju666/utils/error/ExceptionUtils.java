package io.github.pangju666.utils.error;

/**
 * 异常工具类
 *
 * @author 胖橘
 * @version 1.0
 * @since 1.0
 * @see org.apache.commons.lang3.exception.ExceptionUtils
 */
public class ExceptionUtils extends org.apache.commons.lang3.exception.ExceptionUtils {
    /**
     * 抛出异常
     *
     * @param condition 判断是否抛出异常时的条件
     * @param reason 异常原因
     * @throws Exception 条件成立时抛出
     */
    public static void throwException(boolean condition, String reason) throws Exception {
        throwException(condition, new Exception(reason));
    }

    /**
     * 抛出异常
     *
     * @param condition 判断是否抛出异常时的条件
     * @param exception 条件成立时抛出的异常
     * @param <E> 待抛出的异常
     */
    public static<E extends Throwable> void throwException(boolean condition, E exception) throws E {
        if (condition) {
            throw exception;
        }
    }
}
