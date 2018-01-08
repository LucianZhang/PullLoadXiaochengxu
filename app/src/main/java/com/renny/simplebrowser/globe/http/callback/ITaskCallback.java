package com.renny.simplebrowser.globe.http.callback;

/**
 *
 */
public interface ITaskCallback<T> {

    void onBeforeCall();

    void onAfterCall();

    String getLoadingTip();

    void onComplete(T data);

    void onException(Throwable t);

    void onCancelled();

}
