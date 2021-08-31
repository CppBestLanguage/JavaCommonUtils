package io.github.pangju666.utils.common;

import java.util.UUID;

public class UUIDUtils {
    public static String randomUUIDStr() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
