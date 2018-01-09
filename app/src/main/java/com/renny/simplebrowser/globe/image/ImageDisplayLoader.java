package com.renny.simplebrowser.globe.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by yh on 2016/4/11.
 */
public interface ImageDisplayLoader {
    int CACHE_IN_MEM = 1;
    int CACHE_IN_DISK = 2;

    void display(ImageView imageView, String url, ImageLoadListener listener, DisplayOption opts);

    void display(ImageView imageView, int bitmapId);

    Bitmap syncLoad(File file);

    Bitmap syncLoad(String url);

    File syncLoadFile(String url);

    /**
     * 清理缓存
     *
     * @param cachePlaces：缓存位置
     */
    void clearCache(int... cachePlaces);

    void pause();

    void resume();

    void preLoad(String url);
}
