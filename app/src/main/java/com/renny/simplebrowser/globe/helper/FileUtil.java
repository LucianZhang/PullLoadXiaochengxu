package com.renny.simplebrowser.globe.helper;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by yh on 2016/12/07.
 */

public class FileUtil {
    /**
     * 递归删除目录
     */
    public static void deleteAll(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                for (File subFile : subFiles) {
                    deleteAll(subFile);
                }
            }
            file.delete();
        }
    }

    public static void deleteAll(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        deleteAll(file);
    }

    public static boolean isFolderNotEmpty(File folder) {
        if (folder == null || !folder.exists()) {
            return false;
        }
        File[] files = folder.listFiles();
        return !(files == null || files.length == 0);
    }

    public static long copyFile(File f1, File f2) {
        long time = new Date().getTime();
        int length = 2097152;
        FileInputStream in = null;
        try {
            in = new FileInputStream(f1);

            FileOutputStream out = new FileOutputStream(f2);
            byte[] buffer = new byte[length];
            while (true) {
                int ins = in.read(buffer);
                if (ins == -1) {
                    in.close();
                    out.flush();
                    out.close();
                    return new Date().getTime() - time;
                } else
                    out.write(buffer, 0, ins);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isEmpty(File file) {
        return file != null && file.exists();
    }

    /**
     * 文件重命名
     *
     * @param path    文件目录
     * @param oldname 原来的文件名
     * @param newname 新文件名
     */
    public static void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (!oldfile.exists()) {
                return;//重命名文件不存在
            }
            if (newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newname + "已经存在！");
            else {
                oldfile.renameTo(newfile);
            }
        } else {
            System.out.println("新文件名和旧文件名相同...");
        }
    }
}
