package com.renny.simplebrowser.business.log.impl;

import android.util.Log;

import com.renny.simplebrowser.business.log.ILogPrinter;


/**
 * 简单打印器
 * Created by yh on 2016/5/9.
 */
public class SimpleLogPrinter implements ILogPrinter {
    protected void info(String type, String tag, String content) {
    }

    @Override
    public void printI(String tag, String content, Object... args) {
        String realContent = getContent(content, args);
        Log.i(tag, realContent);
        info("info", tag, realContent);
    }

    @Override
    public void printE(String tag, String content, Object... args) {
        String realContent = getContent(content, args);
        Log.e(tag, realContent);
        info("error", tag, realContent);
    }

    @Override
    public void printD(String tag, String content, Object... args) {
        String realContent = getContent(content, args);
        Log.d(tag, realContent);
        info("debug", tag, realContent);
    }

    @Override
    public void printW(String tag, String content, Object... args) {
        String realContent = getContent(content, args);
        Log.w(tag, realContent);
        info("warning", tag, realContent);
    }

    public void printE(String tag, Throwable throwable, String content, Object... args) {
        String realContent = getContent(content, args);
        Log.e(tag, realContent, throwable);
        info("error", tag, realContent);
    }

    private String getContent(String msg, Object... args) {
        try {
            return String.format(msg, args);
        } catch (Throwable throwable) {
            return msg;
        }
    }
}
