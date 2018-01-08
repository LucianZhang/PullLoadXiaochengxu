package com.renny.simplebrowser.globe.http.request;

import android.support.annotation.NonNull;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by yh on 2016/4/15.
 */
public interface IApi {
    @NonNull
    String getUrl();

    RequestMethod getMethod();

    Type getResultType();

    ContentType getContentType();

    ParamType getParamType();

    String getDefaultParams();

    Map<String,Object> getOtherHeaderParams();

    /**
     * 是否需要缓存请求
     *
     * @return
     */
    boolean enableCache();
}
