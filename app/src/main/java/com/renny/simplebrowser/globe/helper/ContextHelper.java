package com.renny.simplebrowser.globe.helper;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import com.renny.simplebrowser.App;
import com.renny.simplebrowser.BuildConfig;


/**
 * Created by yh on 2016/2/18.
 */
public class ContextHelper {

    private static Context application;
    private static Class splashCls;



    public static Context getAppContext() {
        if (application == null) {
            application= App.getContext();
        }
        return application.getApplicationContext();
    }



    public static Resources getResources() {
        Context context = getAppContext();
        if (context != null) {
            return context.getResources();
        }
        return null;
    }

    public static void setSplashCls(Class cls) {
        ContextHelper.splashCls = cls;
    }

    public static Class getSplashCls() {
        return splashCls;
    }

    /**
     * 资源ID获取String
     */
    public static String getString(int stringId) {
        return getAppContext().getString(stringId);
    }

    public static String getString(int stringId, Object... formatArgs) {
        return getAppContext().getString(stringId, formatArgs);
    }


    /**
     * 获取颜色
     */
    public static int getColor(int color) {
        if (getAppContext() == null) {
            return 0;
        }
        return ContextCompat.getColor(getAppContext(), color);
    }

    public static int getDimensionPixelSize(int dimenId) {
        return getResources().getDimensionPixelSize(dimenId);
    }

    public static String getApplicationId() {
        return BuildConfig.APPLICATION_ID;
    }
}

