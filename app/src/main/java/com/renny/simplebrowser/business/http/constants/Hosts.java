package com.renny.simplebrowser.business.http.constants;


import com.renny.simplebrowser.globe.http.request.IHost;

/**
 * Created by yh on 2016/8/23.
 */
public interface Hosts {

    IHost defaults = new IHost() {
        @Override
        public String getHost() {
            return "http://config.pinyin.sogou.com/";
        }
    };

    IHost jenkins = new IHost() {
        @Override
        public String getHost() {
            return "http://192.168.4.21:9090/";
        }
    };


}
