package com.renny.simplebrowser.globe.http.reponse;


/**
 */
public interface IResult<T> {
    String code();

    T data();

    String msg();

    boolean success();

    String json();

}
