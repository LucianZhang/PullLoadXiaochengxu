package com.renny.simplebrowser.business.log;


/**
 * Created by yh on 2016/4/14.
 */
public interface ILogger {

    void d(String content, Object... args);

    void e(String content, Object... args);

    void e(Throwable throwable);

    void e(Throwable throwable, String content, Object... args);

    void i(String content, Object... args);

    void w(String content, Object... args);

}
