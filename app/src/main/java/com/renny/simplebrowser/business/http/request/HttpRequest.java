package com.renny.simplebrowser.business.http.request;

import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.renny.simplebrowser.business.helper.DeviceHelper;
import com.renny.simplebrowser.business.http.constants.HttpCells;
import com.renny.simplebrowser.business.json.JsonHelper;
import com.renny.simplebrowser.globe.exception.NetworkNotAvailableException;
import com.renny.simplebrowser.globe.http.IHttpCell;
import com.renny.simplebrowser.globe.http.parambuilder.IParamBuilder;
import com.renny.simplebrowser.globe.http.request.Api;
import com.renny.simplebrowser.globe.http.request.ParamType;
import com.renny.simplebrowser.globe.http.request.RequestMethod;

import java.util.Map;

/**
 * Created by LuckyCrystal on 2017/10/26.
 */

public class HttpRequest {

    private Api api;
    Map<String, String> params;

    public HttpRequest(Api api, Map<String, String> params) {
        this.api = api;
        this.params = params;
    }

    public okhttp3.Response Call() throws Exception {
        if (!DeviceHelper.getNetworkState()) {
            throw new NetworkNotAvailableException();
        }
        IHttpCell iHttpCell = api.getIHttpCell();
        if (iHttpCell == null) {
            iHttpCell = HttpCells.Img;
        }
        IParamBuilder iParamBuilder = iHttpCell.getParamBuilder();
        Map<String, String> paramsAfter = params;
        if (iParamBuilder != null && params != null) {
            paramsAfter = iParamBuilder.buildParams(params, null);
        }
        okhttp3.Response response = null;
        RequestMethod method = api.getMethod();
        String url = api.getUrl();
        switch (method) {
            case Get: {
                GetRequest getRequest = OkGo.<String>get(url);
                if (!TextUtils.isEmpty(iHttpCell.getUA())) {
                    getRequest.headers("User-Agent", iHttpCell.getUA());
                }
                if (paramsAfter != null) {
                    getRequest.params(paramsAfter);
                }
                response = getRequest.execute();
                break;
            }
            case Post: {
                PostRequest postRequest = OkGo.<String>post(url);
                postRequest.headers("User-Agent", iHttpCell.getUA());
                ParamType paramType = api.getParamType();
                switch (paramType) {
                    case normal://普通请求方式

                        break;
                    case json:
                        postRequest.upJson(JsonHelper.toJSONString(paramsAfter));
                        break;
                }
                response = postRequest.execute();
                break;
            }
        }
        return response;
    }

}
