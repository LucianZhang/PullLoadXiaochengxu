package com.renny.simplebrowser.globe.task;

/**
 *
 */
public abstract class ITaskWithResult<Result> {


    public abstract Result onBackground() throws Exception;

    public void onException(Exception e) {

    }

    public void onComplete(Result result) {

    }

}
