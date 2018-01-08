package com.renny.simplebrowser.globe.http.parambuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class SearchRequestBuilder implements IParamBuilder {
    @Override
    public final Map<String, String> buildParams(Map<String, String> bodyParams, Object p) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(bodyParams);
        params.put("json", "1");
        params.put("p", "3");
        params.put("cb", "dachie");
        return params;
    }
}
