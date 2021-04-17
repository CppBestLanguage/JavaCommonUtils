package com.github.pangju666.utils.lang;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    @Test
    void fromJson() {
    }

    @Test
    void testFromJson() {
    }

    @Test
    void toJson() {
        String test = "{\"test\":\"hello world\"}";
        System.out.println(JsonUtils.toJson(test).isJsonNull());
        System.out.println(JsonUtils.toJson(test).isJsonObject());
        System.out.println(JsonUtils.toJson(test).isJsonPrimitive());
        JsonObject object = new JsonObject();
        object.addProperty("test", "hello world");
        System.out.println(object.toString());
    }

    @Test
    void testToJson() {
    }

    @Test
    void fromJsonArray() {
    }

    @Test
    void testFromJsonArray() {
        JsonArray array = new JsonArray();
        array.add("test");
        array.add("test2");
        array.add("test3");
        array.add("test4");
        List<String> list = JsonUtils.fromJsonArray(array);
        System.out.println(list.toString());
    }

    @Test
    void toJsonArray() {
    }

    @Test
    void testToJsonArray() {
        List<String> testList = new ArrayList<>();
        testList.add("test");
        testList.add("test2");
        testList.add("test3");
        testList.add("test4");
        System.out.println(JsonUtils.toJsonArray(testList));
    }
}