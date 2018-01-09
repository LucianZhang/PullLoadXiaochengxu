package com.renny.simplebrowser.globe.json;

import java.lang.reflect.Type;

/**
 * Created by yh on 2016/4/19.
 */
public interface IParse {
    String toJson(Object bean);

    <T> T fromJson(String json, Class<T> clazz);

    <T> T fromJson(String json, Type type);
}
