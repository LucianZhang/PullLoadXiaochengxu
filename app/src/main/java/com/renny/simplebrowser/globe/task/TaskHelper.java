package com.renny.simplebrowser.globe.task;


import com.renny.simplebrowser.business.http.request.HttpRequest;
import com.renny.simplebrowser.business.http.response.HttpResultParse;
import com.renny.simplebrowser.globe.http.callback.ApiCallback;
import com.renny.simplebrowser.globe.http.request.Api;

import java.io.IOException;
import java.util.Map;

/**
 * Created by LuckyCrystal on 2017/9/26.
 */

public class TaskHelper {

    public static void submit(ITaskBackground iTaskBackground) {
        SdkTask mTask = new SdkTask(iTaskBackground);
        mTask.execute();
    }

    public static <Result> void submitResult(ITaskWithResult<Result> iTaskBackground) {
        TaskWithResult<Result> mTask = new TaskWithResult<>(iTaskBackground);
        mTask.execute();
    }

    public static <T> void apiCall(final Api api, final Map<String, String> params, final ApiCallback<T> apiCallback) {
        apiCallback.onBeforeCall();
        new TaskWithResult<>(new ITaskWithResult<okhttp3.Response>() {
            @Override
            public okhttp3.Response onBackground() throws Exception {
                okhttp3.Response response = null;
                try {
                    HttpRequest request = new HttpRequest(api, params);
                    response = request.Call();
                } catch (IOException e) {
                    e.printStackTrace();
                    apiCallback.onException(e);
                }
                return response;
            }

            @Override
            public void onComplete(okhttp3.Response response) {
                HttpResultParse.parseResult(api, response, apiCallback);
                apiCallback.onAfterCall();
            }


        }).execute();
    }
}
