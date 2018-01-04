package com.renny.simplebrowser.widget.toast;


import com.renny.simplebrowser.App;

/**
 * toast帮助类
 */
public class ToastHelper {
    private static AppToast appToast;

    public static void makeToast(String text) {
        if (appToast == null) {
            appToast = new AppToast(App.getContext());
        }
        appToast.makeToast(text);
    }

}