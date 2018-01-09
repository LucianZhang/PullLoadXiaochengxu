package com.renny.simplebrowser.globe.image;

import android.graphics.Bitmap;
import android.view.View;

/**
 * 图片加载监听
 */
public interface ImageLoadListener {

    /**
     * 加载失败
     *
     * @param imageUri
     * @param view
     * @param throwable
     */
    void onLoadFail(String imageUri, View view, Throwable throwable);


    /**
     * 加载成功
     *
     * @param bitmap
     */
    void onLoadSuccess(View view, Bitmap bitmap);

    void onProgressUpdate(int current, int total);
}
