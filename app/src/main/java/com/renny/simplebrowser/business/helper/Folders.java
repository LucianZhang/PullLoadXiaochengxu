package com.renny.simplebrowser.business.helper;


import android.graphics.Bitmap;

import com.renny.simplebrowser.App;
import com.renny.simplebrowser.business.log.Logs;
import com.renny.simplebrowser.globe.helper.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件存储目录
 * Created by yh on 2016/5/20.
 */
public enum Folders {
    cache("cache"),//
    temp("temp"),//临时目录
    crash("logs/crash"),//错误日志
    img("image"),//图片下载
    gallery("图库", false),//图片保存
    ;
    private String subFolder;
    private boolean isCache = true;
    public static File rootFolder;

    Folders(String subFolder, boolean isCache) {
        this.subFolder = subFolder;
        this.isCache = isCache;
    }

    Folders(String subFolder) {
        this.subFolder = subFolder;
    }

    /**
     * 设置文件存储根目录
     *
     * @param dir
     */
    public static void setRootFolder(File dir) {
        rootFolder = dir;
    }

    public File getRootFolder() {
        if (rootFolder == null) {
            if (!isCache) {
                rootFolder = App.getContext().getExternalFilesDir("data");//默认外部缓存
            } else {
                rootFolder = App.getContext().getExternalCacheDir();//默认外部缓存

            }
        }

        if (rootFolder == null) {
            Logs.common.e("外部存储不可用");
            if (!isCache) {
                rootFolder = App.getContext().getFilesDir();//外部存储不可用则用内部存储
            } else {
                rootFolder = App.getContext().getCacheDir();//内部存储
            }
        }
        if (rootFolder != null) {
            Logs.common.e("rootFolder:  " + rootFolder.getAbsolutePath());
        }
        return rootFolder;
    }

    public File getFolder() {
        return new File(getRootFolder(), subFolder);
    }


    public File getFile(String name) {
        File folder = getFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return new File(folder, name);
    }

    public void cleanFolder() {
        FileUtil.deleteAll(getFolder());
    }

    /**
     * 取得目录
     *
     * @return
     */
    public File getSubFolder(String sub) {
        return new File(getFolder(), sub);
    }

    public File newTempFile() {
        long time = System.currentTimeMillis();
        try {
            File folder = getFolder();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return File.createTempFile(time + "", ".temp", folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File newJpgFile(Bitmap bitmap) {

        long time = System.currentTimeMillis();
        try {
            File folder = getFolder();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = File.createTempFile(time + "", ".jpg", folder);
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                out.flush();
                out.close();
                Logs.base.d("已经保存");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
