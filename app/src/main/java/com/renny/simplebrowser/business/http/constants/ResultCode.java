package com.renny.simplebrowser.business.http.constants;

/**
 * Created by yh on 2016/5/9.
 */
public interface ResultCode {
    /**
     * 登录异常
     */
    String success = "200";
    /**
     * 登录异常
     */
    String TOKEN_ERROR = "-1999";
    /**
     * 接口提示APP升级
     */
    String ERROR_1998 = "-1998";

    /**
     * 服务器返回数据异常
     */
    String ERROR_SERVER_DATA_ERROR = "-2";

    String ERROR_SERVER_DATA_Empty = "-3";

    /**
     * 本地网络异常
     */
    String LOCAL_EXCEPTION = "-1";
    /**
     * 本地网络异常
     */
    String IO_EXCEPTION = "-1";
    /**
     * 本地不提示错误
     */
    String LOCAL_NONE = "-5";
    /**
     * 本地网络异常
     */
    String LOCAL_NET_EXCEPTION = "-222";
    /**
     * 网络未打开
     */
    String LOCAL_NET_NOT_AVAILABLE = "-333";


}
