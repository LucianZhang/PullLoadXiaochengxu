package com.renny.simplebrowser.globe.http.request;

import java.util.Map;

/**
 * Created by yh on 2016/5/12.
 */
public interface IRequest {
    Map<String, Object> getParams();

    IApi getApi();

    String getDefaultParams();

    Map<String, String> getHeaders();

    boolean enableCache();
}
