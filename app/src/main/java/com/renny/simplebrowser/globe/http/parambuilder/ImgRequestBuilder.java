package com.renny.simplebrowser.globe.http.parambuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class ImgRequestBuilder implements IParamBuilder {
    @Override
    public final Map<String, String> buildParams(Map<String, String> bodyParams, Object p) {
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(bodyParams);
        params.put("page", "1");
        params.put("h", "C73F10E7CD7B2F17F74D9BE67A55A4DA");
        params.put("v", "8.6.0.1467");
        params.put("r", "6990_sogou_pinyin_8.5.0.1183_6990");
        params.put("pv", "6.2.15063");
        params.put("sdk", "1.1.0.1766");
        return params;
    }
}
