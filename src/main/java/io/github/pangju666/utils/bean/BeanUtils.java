package io.github.pangju666.utils.bean;

import net.sf.cglib.beans.BeanCopier;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Bean工具类
 *
 * @author 胖橘
 * @version 1.0
 * @since 1.0
 */
public class BeanUtils {
    /**
     * 拷贝Java Bean 属性至目标Java Bean
     *
     * @param source 源Java Bean对象
     * @param target 目标Java Bean对象
     * @param sourceClass 源Java Bean Class
     * @param targetClass 目标Java Bean Class
     * @param <S> 源Java Bean
     * @param <T> 目标Java Bean
     */
    public static <S, T> void copy(S source, T target, Class<S> sourceClass, Class<T> targetClass) {
        if (source != null && target != null) {
            BeanCopier copier = getBeanCopier(sourceClass, targetClass);
            copier.copy(source, target, null);
        }
    }

    /**
     * 拷贝Java Bean 属性至目标Java Bean
     *
     * @param source 源Java Bean对象
     * @param target 目标Java Bean对象
     * @param copier Bean拷贝器, 可以通过{@link BeanUtils#getBeanCopier(Class, Class)}获取
     * @param <S> 源Java Bean
     * @param <T> 目标Java Bean
     * @see BeanCopier
     */
    public static <S, T> void copy(S source, T target, BeanCopier copier) {
        if (source != null && target != null) {
            copier.copy(source, target, null);
        }
    }

    /**
     * 转换到目标Java Bean
     * <code>
     *     User user = new User();
     *     user.setName("pangju");
     *     AdminUser adminUser = BeanUtils.convert(user, user -> {
     *         AdminUser adminUser = new AdminUser();
     *         adminUser.setName(user.getName());
     *         return adminUser;
     *     })
     * </code>
     *
     * @param source 源Java Bean对象
     * @param convertFunc 自定义转换方法
     * @param <S> 源Java Bean
     * @param <T> 目标Java Bean
     * @return 转换后的Java Bean
     */
    public static <S, T> T convert(S source, Function<S, T> convertFunc) {
        if (source == null) {
            return null;
        }
        return convertFunc.apply(source);
    }

    /**
     * 转换为目标Java Bean List
     *
     * @param sourceList 源Java Bean List
     * @param convertFunc 自定义转换方法
     * @param <S> 源Java Bean
     * @param <T> 目标Java Bean
     * @return 转换后的Java Bean List
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Function<S, T> convertFunc) {
        return sourceList.stream().map(convertFunc).collect(Collectors.toList());
    }

    /**
     * 转换到目标Java Bean
     * <code>
     *     User user = new User();
     *     user.setName("pangju");
     *     AdminUser admin = BeanUtils.convert(user, User.class, AdminUser.class);
     * </code>
     *
     * @param source 源Java Bean对象
     * @param sourceClass 源Java Bean Class
     * @param targetClass 目标Java Bean Class
     * @param <S> 源Java Bean
     * @param <T> 目标Java Bean
     * @return 转换后的Java Bean
     */
    public static <S, T> T convert(S source, Class<S> sourceClass, Class<T> targetClass) {
        return convert(source, targetClass, getBeanCopier(sourceClass, targetClass));
    }

    /**
     * 转换到目标Java Bean
     * <code>
     *     BeanCopier copier = BeanUtils.getBeanCopier(User.class, AdminUser.class);
     *     User user = new User();
     *     user.setName("pangju");
     *     AdminUser admin = BeanUtils.convert(user, AdminUser.class, copier);
     * </code>
     *
     * @param source 源Java Bean对象
     * @param targetClass 目标Java Bean Class, 必须实现公有无参构造方法
     * @param copier Bean拷贝器, 可以通过{@link BeanUtils#getBeanCopier(Class, Class)}获取
     * @param <S> 源Java Bean
     * @param <T> 目标Java Bean
     * @return 转换后的Java Bean
     * @see BeanCopier
     */
    public static <S, T> T convert(S source, Class<T> targetClass, BeanCopier copier) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            copier.copy(source, target, null);
            return target;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("转换失败", e);
        }
    }

    /**
     * 转换为目标Java Bean List
     *
     * @param sourceList 源Java Bean List
     * @param sourceClass 源Java Bean Class
     * @param targetClass 目标Java Bean Class, 必须实现公有无参构造方法
     * @param <S> 源Java Bean
     * @param <T> 目标Java Bean
     * @return 转换后的Java Bean List
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Class<S> sourceClass, Class<T> targetClass) {
        BeanCopier copier = getBeanCopier(sourceClass, targetClass);
        return sourceList.stream()
                .map(source -> convert(source, targetClass, copier))
                .collect(Collectors.toList());
    }

    /**
     * 转换为目标Java Bean List
     *
     * @param sourceList 源Java Bean List
     * @param targetClass 目标Java Bean Class, 必须实现公有无参构造方法
     * @param copier Bean拷贝器, 可以通过{@link BeanUtils#getBeanCopier(Class, Class)}获取
     * @param <S> 源Java Bean
     * @param <T> 目标Java Bean
     * @return 转换后的Java Bean List
     * @see BeanCopier
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass, BeanCopier copier) {
        return sourceList.stream()
                .map(source -> convert(source, targetClass, copier))
                .collect(Collectors.toList());
    }

    /**
     * 转换为目标Java Bean 拷贝器
     *
     * @param sourceClass 源Java Bean Class
     * @param targetClass 目标Java Bean Class
     * @param <S> 源Java Bean
     * @param <T> 目标Java Bean
     * @return Java Bean 拷贝器
     * @see BeanCopier
     */
    public static <S, T> BeanCopier getBeanCopier(Class<S> sourceClass, Class<T> targetClass) {
        return BeanCopier.create(sourceClass, targetClass, false);
    }

    protected BeanUtils() {
    }
}
