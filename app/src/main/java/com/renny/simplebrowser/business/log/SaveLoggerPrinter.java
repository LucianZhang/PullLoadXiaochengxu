package com.renny.simplebrowser.business.log;

import android.util.Log;

/**
 * Created by yh on 2016/5/23.
 */
public class SaveLoggerPrinter implements ILogPrinter {
    private static SaveLoggerPrinter instance = new SaveLoggerPrinter();


    public static SaveLoggerPrinter instance() {
        return instance;
    }

    protected final String getNameFromTrace(StackTraceElement[] traceElements, int place) {
        StringBuffer taskName = new StringBuffer();
        if (traceElements != null && traceElements.length > place) {
            StackTraceElement traceElement = traceElements[place];
            taskName.append(traceElement.getMethodName());
            taskName.append("(").append(traceElement.getFileName()).append(":").append(traceElement.getLineNumber()).append(")");
        }
        return taskName.toString();
    }


    @Override
    public void printI(String tag, String content, Object... args) {
        String realContent = getContent(content, args);
        Log.i(tag, realContent);
    }

    @Override
    public void printE(String tag, String content, Object... args) {
        String realContent = getContent(content, args);
        Log.e(tag, realContent);
    }

    @Override
    public void printD(String tag, String content, Object... args) {
        String realContent = getContent(content, args);
        Log.d(tag, realContent);
    }

    @Override
    public void printW(String tag, String content, Object... args) {
        String realContent = getContent(content, args);
        Log.w(tag, realContent);
    }

    public void printE(String tag, Throwable throwable, String content, Object... args) {
        String realContent = getContent(content, args);
        Log.e(tag, realContent, throwable);
    }

    private String getContent(String msg, Object... args) {
        try {
            String pre = getNameFromTrace(Thread.currentThread().getStackTrace(), 5);
            return pre + String.format(msg, args);
        } catch (Throwable throwable) {
            return msg;
        }
    }
}
