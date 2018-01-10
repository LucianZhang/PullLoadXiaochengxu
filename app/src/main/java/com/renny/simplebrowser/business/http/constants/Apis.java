package com.renny.simplebrowser.business.http.constants;


import com.renny.simplebrowser.globe.http.request.Api;
import com.renny.simplebrowser.view.bean.SuggestionHost;

/**
 * Created by LuckyCrystal on 2017/10/25.
 */

public interface Apis {

    Api searchSuggestion = Api.GET("http://suggestion.baidu.com/su").setIHttpCell(HttpCells.search);
    Api host = Api.GET("suggestHost.json", SuggestionHost.class).setIHttpCell(HttpCells.mine);
}
