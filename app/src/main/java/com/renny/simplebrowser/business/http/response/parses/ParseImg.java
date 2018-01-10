package com.renny.simplebrowser.business.http.response.parses;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.renny.simplebrowser.business.http.constants.ResultCode;
import com.renny.simplebrowser.business.log.Logs;
import com.renny.simplebrowser.globe.http.bean.Result;
import com.renny.simplebrowser.globe.http.reponse.IResultParse;
import com.renny.simplebrowser.globe.http.request.IApi;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by LuckyCrystal on 2017/10/25.
 *
 */

public  class ParseImg implements IResultParse {
    @Override
    public Result parseResult(IApi iApi, String json, Type type) {
        if (TextUtils.isEmpty(json)) {
            Logs.network.e("服务器数据返回异常 url=%s model=%s", iApi.getUrl(), type);
            return Result.fail(ResultCode.ERROR_SERVER_DATA_Empty, "搜索无结果");
        }
        try {
            JSONObject jsonObject1 = JSON.parseObject(json);
            if (jsonObject1 != null) {
                String code = ResultCode.success;
                String msg = "";
                Object body = jsonObject1.toJavaObject(Object.class);
                if (body != null && type != null) {
                    if (type == String.class || type == List.class) {
                        body = body.toString();
                    } else {
                        body = JSON.parseObject(body.toString(), type);
                    }
                }
                return new Result<>(body, true, code, msg, type);
            } else {
                Logs.network.e("数据结构错误 url=%s model=%s", iApi.getUrl(), type);
                return Result.fail(ResultCode.ERROR_SERVER_DATA_ERROR, "服务器数据返回异常");
            }
        } catch (JSONException e) {
            Logs.network.e("json解析失败 url=%s model=%s", iApi.getUrl(), type);
            return Result.fail(ResultCode.ERROR_SERVER_DATA_ERROR, "服务器数据返回异常");
        } catch (Throwable e) {
            Logs.network.e("url=%s model=%s e=%s", iApi.getUrl(), type, e);
            return Result.fail(ResultCode.ERROR_SERVER_DATA_ERROR, "服务器数据返回异常");
        }
    }
}
