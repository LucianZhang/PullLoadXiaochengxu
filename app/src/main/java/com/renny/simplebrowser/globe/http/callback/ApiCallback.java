package com.renny.simplebrowser.globe.http.callback;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.renny.simplebrowser.business.base.ILoading;
import com.renny.simplebrowser.business.http.constants.ResultCode;
import com.renny.simplebrowser.business.log.Logs;
import com.renny.simplebrowser.business.toast.ToastHelper;
import com.renny.simplebrowser.globe.exception.NetworkNotAvailableException;
import com.renny.simplebrowser.globe.http.bean.Result;
import com.renny.simplebrowser.globe.http.reponse.IResult;

import java.io.IOException;
import java.net.SocketException;


/**
 * @author Created by yh on 2016/4/28
 */
public abstract class ApiCallback<T> extends SimpleCallback<IResult<T>> {

    public ApiCallback(ILoading iLoading) {
        this.iLoading = iLoading;
    }
    public ApiCallback() {
    }
    @Override
    public final void onException(Throwable t) {
        if (t instanceof NetworkNotAvailableException) {
            onFailure(Result.fail(ResultCode.LOCAL_NET_NOT_AVAILABLE, "网络未开启"));
            return;
        } else if (t instanceof SocketException) {
            onFailure(Result.fail(ResultCode.LOCAL_EXCEPTION, "网络异常"));
            return;
        }else if (t instanceof IOException) {
            onFailure(Result.fail(ResultCode.IO_EXCEPTION, "请求发起异常"));
            return;
        }
        Logs.defaults.e("ApiCallback 操作失败\n"+t);
    }

    @Override
    public final void onComplete(IResult<T> result) {
        if (result != null) {
            if (result.success()) {
                onSuccess(result);
            } else {
                onFailure(result);
            }
        }
    }

    public abstract void onSuccess(IResult<T> result);



    public void onFailure(@NonNull IResult result) {
        String code = result.code();
        if (TextUtils.isEmpty(code)) {
            return;
        }
        switch (code) {
            case ResultCode.TOKEN_ERROR:
            case ResultCode.ERROR_1998:
            case ResultCode.LOCAL_NONE:
                //以上code不需要提示
                break;
            default:
                ToastHelper.makeToast(result.msg());
                break;
        }
    }

}
