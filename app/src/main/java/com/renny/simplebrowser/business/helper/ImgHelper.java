package com.renny.simplebrowser.business.helper;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.renny.simplebrowser.App;
import com.renny.simplebrowser.R;
import com.renny.simplebrowser.globe.image.AdJustImgSizeListener;
import com.renny.simplebrowser.globe.image.DisplayOption;
import com.renny.simplebrowser.globe.image.ImageDisplayLoader;
import com.renny.simplebrowser.globe.image.ImageLoadListener;
import com.renny.simplebrowser.globe.image.impl.glide.GlideImageLoaderNew;

import java.io.File;

/**
 * 图片加载工具类
 * Created by yh on 2016/5/5.
 */
public final class ImgHelper {
    private static ImageDisplayLoader loader;

    static {
        GlideImageLoaderNew imageDisplayLoader = new GlideImageLoaderNew(App.getContext());
        DisplayOption options = DisplayOption.builder().setDefaultResId(R.mipmap.ic_launcher);
        imageDisplayLoader.setDefaultDisplayOption(options);
        loader = imageDisplayLoader;
    }

    /**
     * 加载本地图片
     */
    public static void displayImage(ImageView image, @DrawableRes int bitmapId) {
        loader.display(image, bitmapId);
    }

    public static void displayImage(ImageView image, String url, int defaultImg) {
        DisplayOption options = null;
        if (!TextUtils.isEmpty(url)) {
            options = DisplayOption.builder().setDefaultResId(defaultImg);
        }
        loader.display(image, url, null, options);
    }

    public static void displayImage(ImageView image, String url) {
        loader.display(image, url, null, null);
    }

    public static void displayImage(ImageView image, String url, ImageLoadListener imageLoadListener) {
        loader.display(image, url, imageLoadListener, null);
    }

    public static void displayImage(ImageView image, String url, DisplayOption option) {
        loader.display(image, url, null, option);
    }

    public static void displayImage(ImageView image, String url, ImageLoadListener imageLoadListener, DisplayOption option) {
        loader.display(image, url, imageLoadListener, option);
    }


    /**
     * 根据宽度，动态调整img的高度
     *
     * @param image
     * @param url
     * @param width
     */
    public static void displayAdjust(ImageView image, String url, int width) {
        loader.display(image, url, new AdJustImgSizeListener(width), null);
    }


    public static Bitmap syncLoad(String url) {
        return loader.syncLoad(url);
    }

    public static File syncLoadFile(String url) {
        return loader.syncLoadFile(url);
    }

    public static Bitmap syncLoad(File file) {
        return loader.syncLoad(file);
    }

    public static void pause() {
        loader.pause();
    }

    public static void resume() {
        loader.resume();
    }

    public static void preLoad(String url) {
        loader.preLoad(url);
    }

    public static void clearCache(int... cachePlaces) {
        loader.clearCache(cachePlaces);
    }
}
