package com.renny.simplebrowser.globe.http.bean;


import com.renny.simplebrowser.globe.http.reponse.IResult;

import java.lang.reflect.Type;

public class Result<T> implements IResult<T> {
    private T result;
    private String resultDes;
    private String code;
    private boolean success;
    private Type resultType;
    private String json;

    public static Result fail(String code, String msg) {
        Result result = new Result();
        result.result = result;
        result.success = false;
        result.code = code;

        result.resultDes = msg;
        return result;
    }


    public static Result success(Object body) {
        Result result = new Result();
        result.result = body;
        result.success = true;
        result.code = "";
        return result;
    }

    public Result() {

    }

    public Result(T result, boolean success, String code, String resultDes, Type resultType) {
        this.result = result;
        this.success = success;
        this.code = code;
        this.resultDes = resultDes;
        this.resultType = resultType;
    }
    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String json() {
        return json;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public T data() {
        return result;
    }

    @Override
    public String msg() {
        return resultDes;
    }

    @Override
    public boolean success() {
        return success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Type getResultType() {
        return resultType;
    }

    public void setResultType(Type resultType) {
        this.resultType = resultType;
    }

    public String getJson() {
        return json;
    }



}
