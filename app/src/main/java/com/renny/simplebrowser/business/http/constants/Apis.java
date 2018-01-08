package com.renny.simplebrowser.business.http.constants;


import com.renny.simplebrowser.globe.http.request.Api;

/**
 * Created by LuckyCrystal on 2017/10/25.
 */

public interface Apis {

    Api uploadBuildNum = Api.POST("build_version/android_axd_build.json", String.class).setIHttpCell(HttpCells.Img);
    Api searchSuggestion = Api.GET("http://suggestion.baidu.com/su").setIHttpCell(HttpCells.search);
}
