package com.renny.simplebrowser.globe.http.reponse;

import com.renny.simplebrowser.globe.http.bean.Result;
import com.renny.simplebrowser.globe.http.request.IApi;

import java.lang.reflect.Type;

/**
 * Created by LuckyCrystal on 2017/10/25.
 */

public interface IResultParse {

     Result parseResult(IApi iApi, String json, Type type);
}
