package com.renny.simplebrowser.globe.image;

import android.support.annotation.DrawableRes;

/**
 * 图片显示的参数
 */
public class DisplayOption {
    public Integer loadingResId = -1;
    public Integer defaultResId = -1;
    public Integer errorResId = -1;
    public Integer maxWidth;
    public Integer maxHeight;
    public Boolean cacheInMemory = true;
    public Boolean cacheOnDisk = true;

    private DisplayOption() {
    }

    public DisplayOption setLoadingResId(@DrawableRes int loadingResId) {
        this.loadingResId = loadingResId;
        return this;
    }

    public DisplayOption setDefaultResId(@DrawableRes int defaultResId) {
        this.defaultResId = defaultResId;
        return this;
    }

    public DisplayOption setErrorResId(@DrawableRes int errorResId) {
        this.errorResId = errorResId;
        return this;
    }

    public DisplayOption setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public DisplayOption setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public DisplayOption setCacheInMemory(boolean cacheInMemory) {
        this.cacheInMemory = cacheInMemory;
        return this;
    }

    public DisplayOption setCacheOnDisk(boolean cacheOnDisk) {
        this.cacheOnDisk = cacheOnDisk;
        return this;
    }

    public static DisplayOption builder() {
        return new DisplayOption();
    }

}
