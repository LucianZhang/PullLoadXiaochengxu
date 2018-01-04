package com.renny.simplebrowser.business.log;

/**
 * Created by yh on 2016/5/9.
 */
public interface ILogPrinter {

    void printI(String tag, String content, Object... args);

    void printE(String tag, String content, Object... args);

    void printD(String tag, String content, Object... args);

    void printW(String tag, String content, Object... args);

    void printE(String tag, Throwable throwable, String content, Object... args);
}
