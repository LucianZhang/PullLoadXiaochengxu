package com.renny.simplebrowser.globe.helper;

import android.os.Handler;
import android.os.Looper;

import com.renny.simplebrowser.business.log.Logs;

/**
 * 线程帮助类
 */
public final class ThreadHelper {

    public final static Handler MAIN = new Handler(Looper.getMainLooper());

    public static boolean inMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    public static void postMain(Runnable runnable) {
        MAIN.post(runnable);
    }

    public static void postDelayed(final Runnable runnable, long delayMillis) {
        MAIN.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Throwable throwable) {
                    Logs.base.e(throwable);
                }
            }
        }, delayMillis);
    }

    public static void removeCallbacks(Runnable runnable) {
        MAIN.removeCallbacks(runnable);
    }

}
