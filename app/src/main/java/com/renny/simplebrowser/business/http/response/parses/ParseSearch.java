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

/**
 * Created by LuckyCrystal on 2017/10/25.
 *
 */

public  class ParseSearch<T> implements IResultParse {
    @Override
    public Result<T> parseResult(IApi iApi, String json, Type type) {
        if (TextUtils.isEmpty(json)) {
            Logs.network.e("服务器数据返回异常 url=%s model=%s", iApi.getUrl(), type);
            return Result.fail(ResultCode.ERROR_SERVER_DATA_Empty, "搜索无结果");
        }
        try {
            int length=json.length()-2;
            String temp= json.substring(7,length);
            Logs.base.d("abc:"+temp);
            JSONObject jsonObject1 = JSON.parseObject(temp);
            if (jsonObject1 != null) {
                String code = ResultCode.success;
                String msg = "";
                Object body = jsonObject1.getObject("s",Object.class);
                if (body != null && type != null) {
                    if (type == String.class ) {
                        body = body.toString();
                    } else {
                        body = JSON.parseObject(body.toString(), type);
                    }
                }
                return new Result<>((T)body, true, code, msg, type);
            } else {
                Logs.network.e("数据结构错误 url=%s model=%s", iApi.getUrl(), type);
                return Result.fail(ResultCode.ERROR_SERVER_DATA_ERROR, "服务器数据返回异常");
            }
        } catch (JSONException e) {
            Logs.network.e("json解析失败 url=%s model=%s", iApi.getUrl(), type);
            Logs.network.e("json解析失败 data=%s ",json);
            return Result.fail(ResultCode.ERROR_SERVER_DATA_ERROR, "服务器数据返回异常");
        } catch (Throwable e) {
            Logs.network.e("url=%s model=%s e=%s", iApi.getUrl(), type, e);
            Logs.network.e("json解析失败 data=%s ",json);
            return Result.fail(ResultCode.ERROR_SERVER_DATA_ERROR, "服务器数据返回异常");
        }
    }
}
