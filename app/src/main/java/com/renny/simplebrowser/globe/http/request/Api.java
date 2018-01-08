package com.renny.simplebrowser.globe.http.request;


import com.renny.simplebrowser.business.http.constants.Hosts;
import com.renny.simplebrowser.globe.http.IHttpCell;
import com.renny.simplebrowser.globe.http.parambuilder.IParamBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yh on 2016/5/5.
 */
public final class Api implements IApi {
    private String path;
    private RequestMethod requestMethod;
    private Type type;
    private String defaultParams;
    private ParamType paramType;
    private ContentType contentType;
    private IParamBuilder paramBuilder;
    private IHost host = Hosts.defaults;
    private String url;
    private IHttpCell mIHttpCell;
    private Map<String, Object> otherHeaderParams = new HashMap<>();
    /**
     * 默认缓存
     */
    private boolean enbleCache = true;
    /**
     * 是否需要原始串
     */
    private boolean needJson;


    private boolean needTokenError = true; //是否触发异地登陆统一拦截

    private Api() {
    }

    public IHost getHost() {
        return host;
    }

    @Override
    public ParamType getParamType() {
        return paramType;
    }

    @Override
    public RequestMethod getMethod() {
        return requestMethod;
    }

    @Override
    public Type getResultType() {
        return type;
    }

    @Override
    public ContentType getContentType() {
        return contentType;
    }

    @Override
    public String getDefaultParams() {
        return defaultParams;
    }

    @Override
    public Map<String, Object> getOtherHeaderParams() {
        return otherHeaderParams;
    }

    @Override
    public boolean enableCache() {
        return enbleCache;
    }

    public IHttpCell getIHttpCell() {
        return mIHttpCell;
    }

    public boolean isNeedJson() {
        return needJson;
    }


    public boolean isNeedTokenError() {
        return needTokenError;
    }

    @Override
    public String getUrl() {
        if (url == null) {
            if (path.startsWith("http://") || path.startsWith("https://")) {
                url = path;
            } else {
                return (host.getHost() + path).intern();
            }
        }
        return url;
    }

    /**
     * 取得项目的构建器
     *
     * @return
     */
    public IParamBuilder getParamBuilder() {
        return paramBuilder;
    }


    /********************************
     * setter  begin
     *******************************/
    public Api setHost(IHost host) {
        this.host = host;
        return this;
    }

    public Api setParamBuilder(IParamBuilder paramBuilder) {
        this.paramBuilder = paramBuilder;
        return this;
    }

    public Api setType(Type type) {
        this.type = type;
        return this;
    }

    public Api setIHttpCell(IHttpCell iHttpCell) {
        this.mIHttpCell = iHttpCell;
        if (mIHttpCell != null) {
            host = mIHttpCell.getHost();
        }
        return this;
    }

    public Api setNeedJson(boolean needJson) {
        this.needJson = needJson;
        return this;
    }

    public Api setEnbleCache(boolean enbleCache) {
        this.enbleCache = enbleCache;
        return this;
    }


    public Api setNeedTokenError(boolean needTokenError) {
        this.needTokenError = needTokenError;
        return this;
    }

    public Api setOtherHeaderParams(Map<String, Object> headerParams) {
        this.otherHeaderParams = headerParams;
        return this;
    }

    /********************************
     * setter  end
     *******************************/


    public static Api GET(String path) {
        Api api = new Api();
        api.path = path;
        api.requestMethod = RequestMethod.Get;
        api.paramType = ParamType.normal;
        api.contentType = ContentType.APP_FORM_URLENCODED;
        return api;
    }

    public static Api POST(String path) {
        Api api = new Api();
        api.path = path;
        api.requestMethod = RequestMethod.Post;
        api.paramType = ParamType.normal;
        api.contentType = ContentType.APP_FORM_URLENCODED;
        return api;
    }


    public static Api POST(String path, Type type) {
        Api api = new Api();
        api.type = type;
        api.path = path;
        api.requestMethod = RequestMethod.Post;
        api.paramType = ParamType.normal;
        api.contentType = ContentType.APP_FORM_URLENCODED;
        return api;
    }

    public static Api POSTJSON(String path, Type type) {
        Api api = new Api();
        api.type = type;
        api.path = path;
        api.requestMethod = RequestMethod.Post;
        api.paramType = ParamType.json;
        api.contentType = ContentType.APP_JSON;
        return api;
    }

    public static Api POSTFILE(String path, Type type) {
        Api api = new Api();
        api.type = type;
        api.path = path;
        api.requestMethod = RequestMethod.Post;
        api.paramType = ParamType.file;
        api.contentType = ContentType.APP_OCTET_STREAM;
        return api;
    }
}
