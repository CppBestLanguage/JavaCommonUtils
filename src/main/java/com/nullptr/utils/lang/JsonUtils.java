package com.nullptr.utils.lang;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * JSON字符串转换器接口
 *
 * @author nullptr
 * @version 1.0 2019-12-27
 * @since 1.0 2019-12-27
 */
public class JsonUtils<T> {
    public static <T extends JSONCovert> JSONArray toArray(List<T> jsonList) {
        JSONArray array = new JSONArray();
        for (JSONCovert jsonCovert : jsonList) {
            array.add(jsonCovert.toJSON());
        }
        return array;
    }
}
