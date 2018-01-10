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
 */

public class ParseDefault implements IResultParse {

    @Override
    public Result parseResult(IApi iApi, String json, Type type) {
        if (TextUtils.isEmpty(json)) {//返回结果为空
            Logs.network.e("服务器数据返回异常 url=%s model=%s", iApi.getUrl(), type);
            return Result.fail(ResultCode.ERROR_SERVER_DATA_ERROR, "服务器数据返回异常");
        }
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            if (jsonObject != null) {
                boolean isSuccess = jsonObject.getBoolean("success");
                String code = jsonObject.getString("code");
                String msg = jsonObject.getString("resultDes");
                Object body = jsonObject.getObject("result", Object.class);
                if (body != null && type != null) {
                    if (type == String.class || type == List.class) {
                        body = body.toString();
                    } else {
                        body = JSON.parseObject(body.toString(), type);
                    }
                }
                return new Result<>(body, isSuccess, code, msg, type);
            } else {
                Logs.network.e("数据结构错误 url=%s model=%s", iApi.getUrl(), type);
                return Result.fail(ResultCode.ERROR_SERVER_DATA_ERROR, "服务器数据返回异常");
            }
        } catch (JSONException e) {
            Logs.network.e("json解析失败 url=%s model=%s e=%s", iApi.getUrl(), type, e);
            return Result.fail(ResultCode.ERROR_SERVER_DATA_ERROR, "服务器数据返回异常");
        } catch (Throwable e) {
            Logs.network.e("url=%s model=%s e=%s", iApi.getUrl(), type, e);
            return Result.fail(ResultCode.ERROR_SERVER_DATA_ERROR, "服务器数据返回异常");
        }
    }
}
