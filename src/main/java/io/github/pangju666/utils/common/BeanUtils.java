package io.github.pangju666.utils.common;

import net.sf.cglib.beans.BeanCopier;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BeanUtils {
    public static <S, T> T convert(S source, Function<S, T> convertFunc) {
        if (source == null) {
            return null;
        }
        return convertFunc.apply(source);
    }

    public static <S, T> List<T> convertList(List<S> sourceList, Function<S, T> convertFunc) {
        return sourceList.stream().map(convertFunc).collect(Collectors.toList());
    }

    public static <S, T> void copy(S source, T target, Class<S> sourceClass, Class<T> targetClass) {
        if (source != null && target != null) {
            BeanCopier copier = getBeanCopier(sourceClass, targetClass);
            copier.copy(source, target, null);
        }
    }

    public static <S, T> void copy(S source, T target, BeanCopier copier) {
        if (source != null && target != null) {
            copier.copy(source, target, null);
        }
    }

    public static <S, T> T convert(S entity, Class<S> sourceClass, Class<T> targetClass) {
        return convert(entity, targetClass, getBeanCopier(sourceClass, targetClass));
    }

    public static <S, T> T convert(S source, Class<T> targetClass, BeanCopier copier) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            copier.copy(source, target, null);
            return target;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("转换失败", e);
        }
    }

    public static <S, T> List<T> convertList(List<S> sourceList, Class<S> sourceClass, Class<T> targetClass) {
        BeanCopier copier = getBeanCopier(sourceClass, targetClass);
        return sourceList.stream()
                .map(source -> convert(source, targetClass, copier))
                .collect(Collectors.toList());
    }

    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass, BeanCopier copier) {
        return sourceList.stream()
                .map(source -> convert(source, targetClass, copier))
                .collect(Collectors.toList());
    }

    public static <S, T> BeanCopier getBeanCopier(Class<S> sourceClass, Class<T> targetClass) {
        return BeanCopier.create(sourceClass, targetClass, false);
    }
}
