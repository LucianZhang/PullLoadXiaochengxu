package com.renny.simplebrowser.business.json;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.renny.simplebrowser.business.log.Logs;

import java.lang.reflect.Type;

/**
 * Created by yh on 2016/4/19.
 */
public class JsonHelper {
    private static IParse parse;

    private static IParse getParse() {
        if (parse == null) {
            parse =  new FastJsonParse();
        }
        return parse;
    }

    public static String toJSONString(@NonNull Object bean) {
        return getParse().toJson(bean);
    }



    @Nullable
    public static <T> T fromJson(@NonNull String json, @NonNull Type type) {
        String typeString = "";
        if (type instanceof Class) {
            typeString = ((Class) type).getSimpleName();
        }
        if (isString(typeString)) {
            try {
                return (T) json;
            } catch (Throwable throwable) {
                Logs.component.e(throwable);
                return null;
            }
        }
        return getParse().fromJson(json, type);
    }

    private static boolean isString(String typeString) {
        return typeString.startsWith("String");
    }

    public static boolean notJson(String str) {
        if (str == null) {
            return true;
        }
        if (!str.startsWith("{") || !str.endsWith("}")) {
            return true;
        }
        //TODO 需要增加其他不是json的判断逻辑
        return false;
    }

}
