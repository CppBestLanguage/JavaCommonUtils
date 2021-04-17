package com.github.pangju666.utils.lang;


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
public final class JsonUtils {
    /**
     * json对象转换为java对象
     *
     * @param element json对象
     * @param <T> 待转换对象类型
     * @return 转换后的java对象
     */
    public static <T> T fromJson(JsonElement element) {
        return fromJson(element, new Gson());
    }

    /**
     * json对象转换为java对象
     *
     * @param element json对象
     * @param deserializer json反序列化器
     * @param <T> 待转换对象类型
     * @return 转换后的java对象
     */
    public static <T> T fromJson(JsonElement element, JsonDeserializer<T> deserializer) {
        return fromJson(element, createGson(deserializer));
    }
    
    private static <T> T fromJson(JsonElement element, Gson gson) {
        return gson.fromJson(element, getActualType(new TypeToken<T>() {}));
    }

    /**
     * java对象转换为json对象
     *
     * @param javaBean java对象
     * @param <T> 待转换对象类型
     * @return 转换后的java对象
     */
    public static <T> JsonElement toJson(T javaBean) {
        return toJson(javaBean, new Gson());
    }

    /**
     * java对象转换为json对象
     *
     * @param javaObject java对象
     * @param serializer json序列化器
     * @param <T> 待转换对象类型
     * @return 转换后的java对象，为空则返回空json对象
     */
    public static <T> JsonElement toJson(T javaObject, JsonSerializer<T> serializer) {
        return toJson(javaObject, createGson(serializer));
    }

    private static <T> JsonElement toJson(T javaObject, Gson gson) {
        if (javaObject == null) {
            return new JsonObject();
        }
        return gson.toJsonTree(javaObject, getActualType(new TypeToken<T>() {}));
    }

    /**
     * json数组转换为对象集合
     *
     * @param jsonArray json数组
     * @param <T> 待转换对象类型
     * @return 转换后的集合，数组为空或没有元素时返回一个空的集合
     */
    public static <T> List<T> fromJsonArray(JsonArray jsonArray) {
        return fromJsonArray(jsonArray, new Gson());
    }

    /**
     * json数组转换为对象集合
     *
     * @param jsonArray json数组
     * @param deserializer json反序列化器
     * @param <T> 待转换对象类型
     * @return 转换后的集合，数组为空或没有元素时返回一个空的集合
     */
    public static <T> List<T> fromJsonArray(JsonArray jsonArray, JsonDeserializer<T> deserializer) {
        return fromJsonArray(jsonArray, createGson(deserializer));
    }

    public static <T> List<T> fromJsonArray(JsonArray array, Gson gson) {
        if (array != null && array.size() != 0) {
            // 自定义通用类型
            TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {};
            return gson.fromJson(array, typeToken.getType());
        }
        return Collections.emptyList();
    }

    /**
     * 对象集合转json数组
     *
     * @param javaList 对象集合
     * @param <T> 待转换对象类型
     * @return 转换后的json数组，集合为空或没有元素时返回一个空的json数组
     */
    public static <T> JsonArray toJsonArray(List<T> javaList) {
        return toJsonArray(javaList, new Gson());
    }

    /**
     * 对象集合转json数组
     *
     * @param serializer json序列化器
     * @param javaList 对象集合
     * @param <T> 待转换对象类型
     * @return 转换后的json数组，集合为空或没有元素时返回一个空的json数组
     */
    public static <T> JsonArray toJsonArray(List<T> javaList, JsonSerializer<T> serializer) {
        return toJsonArray(javaList, createGson(serializer));
    }

    public static <T> JsonArray toJsonArray(List<T> objectList, Gson gson) {
        if (objectList != null && !objectList.isEmpty()) {
            // 自定义通用类型
            TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {};
            JsonElement jsonElement = gson.toJsonTree(objectList, typeToken.getType());
            return jsonElement.getAsJsonArray();
        }
        return new JsonArray();
    }
    
    private static <T> Gson createGson(JsonSerializer<T> serializer) {
        return new GsonBuilder()
                .registerTypeAdapter(getActualType(new TypeToken<T>() {}), serializer)
                .setPrettyPrinting()
                .create();
    }

    private static <T> Gson createGson(JsonDeserializer<T> serializer) {
        return new GsonBuilder()
                .registerTypeAdapter(getActualType(new TypeToken<T>() {}), serializer)
                .setPrettyPrinting()
                .create();
    }

    private static <T> Type getActualType(TypeToken<T> typeToken) {
        Type typeClass = typeToken.getClass().getGenericSuperclass();
        return ((ParameterizedType)typeClass).getActualTypeArguments()[0];
    }
}
