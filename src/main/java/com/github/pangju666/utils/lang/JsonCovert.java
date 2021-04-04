package com.github.pangju666.utils.lang;

import net.sf.json.JSONObject;

/**
 * JSON转换接口
 *
 * @author 马佳乐
 */
public interface JsonCovert<T> {
    default JSONObject toJson() {
        return JSONObject.fromObject(this);
    }

    default T fromJson(JSONObject object) {
        return (T) JSONObject.toBean(object);
    }
}
