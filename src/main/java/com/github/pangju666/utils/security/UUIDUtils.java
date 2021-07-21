package com.github.pangju666.utils.security;

import java.util.UUID;

public class UUIDUtils {
    public static String randomUUIDStr() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
