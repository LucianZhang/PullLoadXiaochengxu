package com.renny.simplebrowser.business.toast;


import android.annotation.SuppressLint;

import com.renny.simplebrowser.App;

/**
 * toast帮助类
 */
public class ToastHelper {
    @SuppressLint("StaticFieldLeak")
    private static AppToast appToast;

    public static void makeToast(String text) {
        if (appToast == null) {
            appToast = new AppToast(App.getContext());
        }
        appToast.makeToast(text);
    }

}