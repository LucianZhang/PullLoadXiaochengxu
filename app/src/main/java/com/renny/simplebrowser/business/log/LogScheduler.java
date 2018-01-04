package com.renny.simplebrowser.business.log;

import android.support.annotation.NonNull;

import com.renny.simplebrowser.business.log.impl.SimpleLogControl;
import com.renny.simplebrowser.business.log.impl.SimpleLogPrinter;


public class LogScheduler implements ILogger {
    private static ILogPrinter defaultPrinter = new SimpleLogPrinter();
    private static ILogControl defaultLogControl = new SimpleLogControl();
    /**
     * tag前缀
     */
    private static String tagPre = null;
    private String tag = "default";
    private boolean enbleD = true;
    private boolean enbleE = true;
    private boolean enbleI = true;
    private boolean enbleW = true;

    private ILogPrinter iLogPrinter;

    public static void setDefaultPrinter(@NonNull ILogPrinter logPrinter) {
        if (logPrinter != null) {
            defaultPrinter = logPrinter;
        }
    }

    public static void setDefaultLogControl(@NonNull ILogControl logControl) {
        if (logControl != null) {
            defaultLogControl = logControl;
        }
    }

    /**
     * 设置前缀
     *
     * @param pre_
     */
    public static void setTagPre(String pre_) {
        tagPre = pre_;
    }

    private LogScheduler(@NonNull String tag, ILogPrinter iLogPrinter, ILogControl iLogControl) {
        this.iLogPrinter = iLogPrinter == null ? defaultPrinter : iLogPrinter;

        this.tag = tagPre != null ? tagPre + tag : tag;

        if (iLogControl == null) {
            iLogControl = defaultLogControl;
        }
        enbleD = iLogControl.enableD();
        enbleE = iLogControl.enableE();
        enbleI = iLogControl.enableI();
        enbleW = iLogControl.enableW();
    }

    public static LogScheduler instance(String tag, ILogPrinter iLogPrinter, ILogControl iLogControl) {
        LogScheduler logScheduler = new LogScheduler(tag, iLogPrinter, iLogControl);
        return logScheduler;
    }

    public static LogScheduler instance(String tag, ILogPrinter iLogPrinter) {
        LogScheduler logScheduler = new LogScheduler(tag, iLogPrinter, null);
        return logScheduler;
    }

    public static LogScheduler instance(String tag) {
        LogScheduler logScheduler = new LogScheduler(tag,  SaveLoggerPrinter.instance(), null);
        return logScheduler;
    }

    public LogScheduler setLogPrinter(ILogPrinter logPrinter) {
        iLogPrinter = logPrinter;
        return this;
    }


    public void d(String content, Object... args) {
        if (enbleD) {
            iLogPrinter.printD(tag, content, args);
        }
    }

    public void e(String content, Object... args) {
        if (enbleE) {
            iLogPrinter.printE(tag, content, args);
        }
    }

    public void e(Throwable throwable) {
        if (enbleE) {
            iLogPrinter.printE(tag, throwable, throwable.getMessage());
        }
    }

    public void e(Throwable throwable, String content, Object... args) {
        if (enbleE) {
            iLogPrinter.printE(tag, throwable, content, args);
        }
    }

    public void i(String content, Object... args) {
        if (enbleI) {
            iLogPrinter.printI(tag, content, args);
        }
    }

    public void w(String content, Object... args) {
        if (enbleW) {
            iLogPrinter.printW(tag, content, args);
        }
    }
}
