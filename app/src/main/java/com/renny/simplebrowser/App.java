package com.renny.simplebrowser;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by Renny on 2018/1/2.
 */

public class App extends Application {

    private static Context AppContext;

    public static Context getContext() {
        return AppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }


}

