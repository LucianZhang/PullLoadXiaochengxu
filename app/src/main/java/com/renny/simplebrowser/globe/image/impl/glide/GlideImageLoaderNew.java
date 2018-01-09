package com.renny.simplebrowser.globe.image.impl.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.renny.simplebrowser.globe.helper.BitmapUtils;
import com.renny.simplebrowser.globe.helper.ThreadHelper;
import com.renny.simplebrowser.globe.image.DisplayOption;
import com.renny.simplebrowser.globe.image.ImageDisplayLoader;
import com.renny.simplebrowser.globe.image.ImageLoadListener;

import java.io.File;
import java.util.concurrent.ExecutionException;


public class GlideImageLoaderNew implements ImageDisplayLoader {
    private Context appContext;

    private DisplayOption defaultDisplayOption;

    public GlideImageLoaderNew(Context appContext) {
        this.appContext = appContext;
    }

    public void setDefaultDisplayOption(DisplayOption defaultDisplayOption) {
        this.defaultDisplayOption = defaultDisplayOption;
    }

    @Override
    public void display(final ImageView imageView, final String url, final ImageLoadListener listener, DisplayOption opts) {
        if (imageView != null && !TextUtils.isEmpty(url)) {
            GlideRequest<Drawable> requestBuilder = GlideApp.with(appContext).load(url);
            opts = opts == null ? defaultDisplayOption : opts;
            if (opts != null) {
                if (opts.cacheInMemory != null) {
                    requestBuilder.skipMemoryCache(!opts.cacheInMemory);
                }
                if (opts.cacheOnDisk != null) {
                    if (opts.cacheOnDisk) {
                        requestBuilder.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                    } else {
                        requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE);
                    }
                }

                if (opts.maxHeight != null && opts.maxWidth != null) {
                    requestBuilder.override(opts.maxWidth, opts.maxHeight);
                }

                if (opts.errorResId > 0) {
                    requestBuilder.error(opts.errorResId);
                }

                if (opts.defaultResId > 0) {
                    requestBuilder.placeholder(opts.defaultResId);
                }
                if (opts.loadingResId > 0) {
                    requestBuilder.placeholder(opts.loadingResId);
                }

            }
            if (listener != null) {
                requestBuilder.listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        listener.onLoadFail(url, imageView, e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        if (resource != null) {
                            listener.onLoadSuccess(imageView, BitmapUtils.drawableToBitmap(resource));
                        } else {
                            listener.onLoadSuccess(imageView, null);
                        }
                        return false;
                    }
                });
            }

            requestBuilder.thumbnail(0.4f).into(imageView);
        }
    }

    @Override
    public void display(ImageView imageView, int bitmap) {
        GlideApp.with(appContext).load(bitmap).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
    }

    @Override
    public Bitmap syncLoad(File file) {
        try {
            return GlideApp.with(appContext).asBitmap().load(file).submit().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Bitmap syncLoad(String url) {
        try {
            return GlideApp.with(appContext).asBitmap().load(url).submit().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public File syncLoadFile(String url){
        try {
            return GlideApp.with(appContext).asFile().load(url).submit().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void preLoad(final String url) {
        if (!ThreadHelper.inMainThread()) {
            ThreadHelper.postMain(new Runnable() {
                @Override
                public void run() {
                    GlideApp.with(appContext).asBitmap().load(url).preload();
                }
            });
        } else {
            GlideApp.with(appContext).asBitmap().load(url).preload();
        }
    }

    @Override
    public void clearCache(int... cachePlaces) {
        if (cachePlaces != null) {
            for (int cachePlace : cachePlaces) {
                switch (cachePlace) {
                    case ImageDisplayLoader.CACHE_IN_DISK:
                        if (ThreadHelper.inMainThread()) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    GlideApp.get(appContext).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
                                }
                            }).start();
                        } else {
                            GlideApp.get(appContext).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
                        }
                        break;
                    case ImageDisplayLoader.CACHE_IN_MEM:
                        GlideApp.get(appContext).clearMemory();//清理内存缓存  可以在UI主线程中进行
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void pause() {
        GlideApp.with(appContext).pauseRequests();
    }

    @Override
    public void resume() {
        GlideApp.with(appContext).resumeRequests();
    }
}
