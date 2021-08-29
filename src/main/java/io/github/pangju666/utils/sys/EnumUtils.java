package io.github.pangju666.utils.sys;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Objects;

public class EnumUtils extends org.apache.commons.lang3.EnumUtils {
    public static <E extends Enum<?>> E valueOf(Class<E> enumClass, Object value, Method method) {
        E[] enumConstants = enumClass.getEnumConstants();
        for (E enumConstant : enumConstants) {
            Object actualValue = null;
            try {
                method.setAccessible(true);
                actualValue = method.invoke(actualValue);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
            if (value instanceof Number && actualValue instanceof Number && 
                    new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(String.valueOf(actualValue))) == 0) {
                return enumConstant;
            }
            if (Objects.equals(actualValue, value)) {
                return enumConstant;
            }
        }
        return null;
    }

    public static <E extends Enum<E>> E getEnumByValue(final Class<E> enumClass,Object value) {
        try {
            return valueOf(enumClass,value,enumClass.getMethod("getValue"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}