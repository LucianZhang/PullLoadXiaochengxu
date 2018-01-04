package com.renny.simplebrowser.business.json;


import com.alibaba.fastjson.JSON;
import com.renny.simplebrowser.business.log.Logs;

import java.lang.reflect.Type;


public class FastJsonParse implements IParse {
    @Override
    public String toJson(Object bean) {
        if (bean != null) {
            try {
                return JSON.toJSONString(bean);
            } catch (Throwable throwable) {
                Logs.component.e(throwable);
            }
        }
        return null;
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        if (json != null) {
            try {
                return JSON.parseObject(json, clazz);
            } catch (Throwable throwable) {
                Logs.component.e("parse json error ,json string=%s", json);
            }
        }
        return null;
    }

    @Override
    public <T> T fromJson(String json, Type type) {
        if (json != null) {
            try {
                return JSON.parseObject(json, type);
            } catch (Throwable throwable) {
                Logs.component.e("parse json error ,json string=%s", json);
            }
        }
        return null;
    }

}
