package com.renny.simplebrowser.business.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.renny.simplebrowser.business.log.Logs;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by Renny on 2018/1/8.
 */

public class X5WebViewClient extends WebViewClient {

    private Context mContext;

    public X5WebViewClient(Context context) {
        mContext = context;
    }

    /**
     * 防止加载网页时调起系统浏览器
     */
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            view.loadUrl(url);
            return true;
        } else {
            return jumpScheme(url);
        }
    }

    private boolean jumpScheme(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("referer", url);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Logs.base.e("您所打开的第三方App未安装！");
            return false;
        }

        return true;
    }
}
