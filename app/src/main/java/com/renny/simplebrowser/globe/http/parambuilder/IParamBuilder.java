package com.renny.simplebrowser.globe.http.parambuilder;

import java.util.Map;


public interface IParamBuilder {
    /**
     * 组装参数
     *
     * @param bodyParams
     * @param params     传过来的参数
     * @return
     */
    Map<String, String> buildParams(Map<String, String> bodyParams, Object params);
}