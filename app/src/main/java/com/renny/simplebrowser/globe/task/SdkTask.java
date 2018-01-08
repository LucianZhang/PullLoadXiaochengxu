package com.renny.simplebrowser.globe.task;

import android.os.AsyncTask;


/**
 * Created by LuckyCrystal on 2017/9/26.
 */

class SdkTask extends AsyncTask<Void, Integer, Void> {

    private ITaskBackground mITaskBackground;

    public SdkTask(ITaskBackground ITaskBackground) {
        mITaskBackground = ITaskBackground;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (mITaskBackground != null) {
            try {
                mITaskBackground.onBackground();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}