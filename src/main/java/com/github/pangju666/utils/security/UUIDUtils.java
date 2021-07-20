package com.github.pangju666.utils.security;

import java.util.UUID;

/**
 * @author pangju666
 * @version 1.0 2021-7-20
 */
public class UUIDUtils {
    /**
     * 生成随机的UUID字符串
     */
    public static String randomUUIDStr() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    protected UUIDUtils() {
    }
}
