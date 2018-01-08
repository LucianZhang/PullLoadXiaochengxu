package com.renny.simplebrowser.business.webview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Renny on 2018/1/8.
 */

public class X5WebChromeClient extends WebChromeClient {
    private Context mContext;
    private View myVideoView;
    private View myNormalView;
    IX5WebChromeClient.CustomViewCallback callback;

    public X5WebChromeClient(Context context) {
        mContext = context;
    }

    @Override
    public void onHideCustomView() {
        if (callback != null) {
            callback.onCustomViewHidden();
            callback = null;
        }
        if (myVideoView != null) {
            ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
            viewGroup.removeView(myVideoView);
            viewGroup.addView(myNormalView);
        }
    }



    @Override
    public void onReceivedTitle(WebView arg0, final String arg1) {
        super.onReceivedTitle(arg0, arg1);
        Log.i("app", "webpage title is " + arg1);

    }
}
