package com.github.pangju666.utils.common;

import com.google.gson.reflect.TypeToken;

import net.sf.cglib.beans.BeanCopier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BeanUtils {
    public static <T, R> R convert(T source, Function<T, R> convertFunc) {
        if (source == null) {
            return null;
        }
        return convertFunc.apply(source);
    }

    public static <T, R> List<R> convertList(List<T> sourceList, Function<T, R> convertFunc) {
        return sourceList.stream().map(convertFunc).collect(Collectors.toList());
    }

    public static <T, R> void copy(T source, R target) {
        if (source != null && target != null) {
            BeanCopier copier = getBeanCopier(new TypeToken<T>() {}, new TypeToken<R>() {});
            copier.copy(source, target, null);
        }
    }

    public static <T, R> R convert(T entity, Class<R> targetClass) throws RuntimeException {
        // 获取源对象类型
        Class<T> sourceClass = getGenericClass(new TypeToken<T>() {});
        BeanCopier copier = getBeanCopier(sourceClass, targetClass);
        return convert(entity, targetClass, copier);
    }

    public static <T, R> List<R> convertList(List<T> sourceList, Class<R> targetClass) throws RuntimeException {
        BeanCopier copier = getBeanCopier(new TypeToken<T>() {}, new TypeToken<R>() {});
        return sourceList.stream()
                .map(source -> convert(source, targetClass, copier))
                .collect(Collectors.toList());
    }

    protected static <T, R> R convert(T source, Class<R> targetClass, BeanCopier copier) throws RuntimeException {
        try {
            R target = targetClass.getDeclaredConstructor().newInstance();
            copier.copy(source, target, null);
            return target;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("转换失败", e);
        }
    }

    protected static <T, R> BeanCopier getBeanCopier(TypeToken<T> sourceTypeToken, TypeToken<R> targetTypeToken) {
        // 获取源对象类型
        Class<T> sourceClass = getGenericClass(sourceTypeToken);
        // 获取目标对象类型
        Class<R> targetClass = getGenericClass(targetTypeToken);
        return getBeanCopier(sourceClass, targetClass);
    }

    protected static <T, R> BeanCopier getBeanCopier(Class<T> sourceClass, Class<R> targetClass) {
        return BeanCopier.create(sourceClass, targetClass, false);
    }

    @SuppressWarnings("unchecked")
    protected static <T> Class<T> getGenericClass(TypeToken<T> value) {
        Type type = value.getClass().getGenericSuperclass();
        ParameterizedType genericType = (ParameterizedType) type;
        return (Class<T>) genericType.getActualTypeArguments()[0];
    }
}
