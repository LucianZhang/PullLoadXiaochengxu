package com.renny.simplebrowser.globe.image;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import com.renny.simplebrowser.business.log.Logs;


/**
 * Created by yh on 2016/6/2.
 */
public class AdJustImgSizeListener implements ImageLoadListener {
    private int width;

    public AdJustImgSizeListener(int width) {
        this.width = width;
    }

    @Override
    public void onLoadFail(String imageUri, View view, Throwable throwable) {

    }

    @Override
    public void onLoadSuccess(View view, Bitmap bitmap) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (bitmap != null && bitmap.getWidth() != 0 && width != 0) {
            lp.width = width;
            lp.height = width * bitmap.getHeight() / bitmap.getWidth();
            view.setLayoutParams(lp);
            Logs.defaults.i("width=%d,height=%d", lp.width, lp.height);
        }
    }

    @Override
    public void onProgressUpdate(int current, int total) {

    }
}
