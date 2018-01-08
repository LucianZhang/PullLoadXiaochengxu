package com.renny.simplebrowser.business.helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.renny.simplebrowser.App;


/**
 * 用途：取设备相关信息
 * 作者：Created by diaowj on 2016/06/08 20:49
 * 邮箱：diaowj@aixuedai.com
 */
public class DeviceHelper {



    /**
     * x
     * 屏幕宽度
     */
    public static int getDeviceWidth(Activity activity) {
        if (activity != null) {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            return dm.widthPixels;
        }
        return 0;
    }

    /**
     * 屏幕高度
     */
    public static int getDeviceHeight(Activity activity) {
        if (activity != null) {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            return dm.heightPixels;
        }
        return 0;
    }


    /**
     * 判断当前有没有网络连接
     */
    public static boolean getNetworkState() {
        Context context = App.getContext();
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            return !(networkinfo == null || !networkinfo.isAvailable());
        }
        return false;
    }

    /**
     * SD卡是否挂载
     */
    public static boolean mountedSdCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


}
