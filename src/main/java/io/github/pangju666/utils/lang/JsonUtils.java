package io.github.pangju666.utils.lang;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * json工具类
 *
 * @author 胖橘
 */
public class JsonUtils {
    private static final Gson DEFAULT_GSON = new GsonBuilder().create();

    protected JsonUtils() {
    }

    /**
     * json对象转换为java对象
     *
     * @param element json对象
     * @param <T>     待转换对象类型
     * @return 转换后的java对象
     */
    public static <T> T fromJson(JsonElement element) {
        return fromJson(element, DEFAULT_GSON);
    }

    /**
     * json对象转换为java对象
     *
     * @param element      json对象
     * @param deserializer json反序列化器
     * @param <T>          待转换对象类型
     * @return 转换后的java对象
     */
    public static <T> T fromJson(JsonElement element, JsonDeserializer<T> deserializer) {
        return fromJson(element, createGson(deserializer));
    }

    /**
     * json对象转换为java对象
     *
     * @param element      json对象
     * @param gson         gson对象
     * @param <T>          待转换对象类型
     * @return 转换后的java对象
     */
    public static <T> T fromJson(JsonElement element, Gson gson) {
        return gson.fromJson(element, new TypeToken<T>() {}.getType());
    }

    /**
     * java对象转换为json对象
     *
     * @param javaBean java对象
     * @param <T>      待转换对象类型
     * @return 转换后的java对象
     */
    public static <T> JsonElement toJson(T javaBean) {
        return toJson(javaBean, DEFAULT_GSON);
    }

    /**
     * java对象转换为json对象
     *
     * @param javaObject java对象
     * @param serializer json序列化器
     * @param <T>        待转换对象类型
     * @return 转换后的java对象，为空则返回空json对象
     */
    public static <T> JsonElement toJson(T javaObject, JsonSerializer<T> serializer) {
        return toJson(javaObject, createGson(serializer));
    }

    /**
     * java对象转换为json对象
     *
     * @param javaObject java对象
     * @param gson gson对象
     * @param <T>        待转换对象类型
     * @return 转换后的java对象，为空则返回空json对象
     */
    public static <T> JsonElement toJson(T javaObject, Gson gson) {
        if (javaObject == null) {
            return new JsonObject();
        }
        return gson.toJsonTree(javaObject, new TypeToken<T>() {}.getType());
    }

    /**
     * json数组转换为对象集合
     *
     * @param array json数组
     * @param <T>       待转换对象类型
     * @return 转换后的集合，数组为空或没有元素时返回一个空的集合
     */
    public static <T> List<T> fromJsonArray(JsonArray array) {
        return fromJsonArray(array, DEFAULT_GSON);
    }

    /**
     * json数组转换为对象集合
     *
     * @param array    json数组
     * @param deserializer json反序列化器
     * @param <T>          待转换对象类型
     * @return 转换后的集合，数组为空或没有元素时返回一个空的集合
     */
    public static <T> List<T> fromJsonArray(JsonArray array, JsonDeserializer<T> deserializer) {
        return fromJsonArray(array, createGson(deserializer));
    }

    /**
     * json数组转换为对象集合
     *
     * @param array    json数组
     * @param gson     gson对象
     * @param <T>          待转换对象类型
     * @return 转换后的集合，数组为空或没有元素时返回一个空的集合
     */
    public static <T> List<T> fromJsonArray(JsonArray array, Gson gson) {
        if (array != null && array.size() != 0) {
            return gson.fromJson(array, new TypeToken<List<T>>() {}.getType());
        }
        return Collections.emptyList();
    }

    /**
     * 对象集合转json数组
     *
     * @param list 对象集合
     * @param <T>      待转换对象类型
     * @return 转换后的json数组，集合为空或没有元素时返回一个空的json数组
     */
    public static <T> JsonArray toJsonArray(List<T> list) {
        return toJsonArray(list, DEFAULT_GSON);
    }

    /**
     * 对象集合转json数组
     *
     * @param serializer json序列化器
     * @param list   对象集合
     * @param <T>        待转换对象类型
     * @return 转换后的json数组，集合为空或没有元素时返回一个空的json数组
     */
    public static <T> JsonArray toJsonArray(List<T> list, JsonSerializer<T> serializer) {
        return toJsonArray(list, createGson(serializer));
    }

    /**
     * json数组转换为对象集合
     *
     * @param list    对象集合
     * @param gson     gson对象
     * @param <T>          待转换对象类型
     * @return 转换后的json数组，集合为空或没有元素时返回一个空的json数组
     */
    public static <T> JsonArray toJsonArray(List<T> list, Gson gson) {
        if (ObjectUtils.isNotEmpty(list)) {
            JsonElement jsonElement = gson.toJsonTree(list, new TypeToken<List<T>>() {}.getType());
            return jsonElement.getAsJsonArray();
        }
        return new JsonArray();
    }

    protected static <T> Gson createGson(JsonSerializer<T> serializer) {
        return new GsonBuilder()
                .registerTypeAdapter(getActualType(new TypeToken<T>() {}), serializer)
                .setPrettyPrinting()
                .create();
    }

    protected static <T> Gson createGson(JsonDeserializer<T> deserializer) {
        return new GsonBuilder()
                .registerTypeAdapter(getActualType(new TypeToken<T>() {}), deserializer)
                .setPrettyPrinting()
                .create();
    }

    protected static <T> Type getActualType(TypeToken<T> typeToken) {
        Type typeClass = typeToken.getClass().getGenericSuperclass();
        return ((ParameterizedType) typeClass).getActualTypeArguments()[0];
    }
}
