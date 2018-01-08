package com.renny.simplebrowser.business.http.response;


import com.renny.simplebrowser.business.http.constants.ResultCode;
import com.renny.simplebrowser.business.http.response.parses.ParseDefault;
import com.renny.simplebrowser.business.log.Logs;
import com.renny.simplebrowser.globe.http.IHttpCell;
import com.renny.simplebrowser.globe.http.bean.Result;
import com.renny.simplebrowser.globe.http.callback.ApiCallback;
import com.renny.simplebrowser.globe.http.reponse.IResultParse;
import com.renny.simplebrowser.globe.http.request.Api;
import com.renny.simplebrowser.globe.http.request.IApi;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.acl.NotOwnerException;

import okhttp3.ResponseBody;

/**
 * Created by LuckyCrystal on 2017/10/25.
 */


public class HttpResultParse {

    public static <T> void parseResult(IApi api, okhttp3.Response response, ApiCallback<T> apiCallback) {
        if (response == null) {
            apiCallback.onException(new NotOwnerException());
            return;
        }
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            apiCallback.onException(new NotOwnerException());
            return;
        }
        String json = null;
        try {
            json = responseBody.string();
        } catch (IOException e) {
            apiCallback.onException(e);
        }
        long startTime1 = System.currentTimeMillis();
        Type type = api.getResultType();
        Result<T> result = null;
        if (api instanceof Api) {
            Api api2 = (Api) api;
            IHttpCell iHttpCell = api2.getIHttpCell();
            IResultParse iResultParse = iHttpCell.getResultParse();
            if (iResultParse == null) {
                iResultParse = new ParseDefault();
            }
            result = iResultParse.parseResult(api2, json, type);
            if (api2.isNeedJson()) {
                result.setJson(json);
            }
        }
        long endTime1 = System.currentTimeMillis();
        long useTime1 = endTime1 - startTime1;
        Logs.component.d("解析用时：" + useTime1);
        if (result == null) {
            apiCallback.onFailure(Result.fail(ResultCode.LOCAL_EXCEPTION, "数据返回异常"));
            return;
        }
        if (result.success()) {
            apiCallback.onSuccess(result);
        } else {
            apiCallback.onFailure(result);
        }

    }


}