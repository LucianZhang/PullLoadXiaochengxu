package com.renny.simplebrowser.globe.http.callback;


import com.renny.simplebrowser.business.base.ILoading;

/**
 *
 */
public interface ITaskCallbackWithLoading<T> extends ITaskCallback<T> {
    void setLoading(ILoading loading);
}
