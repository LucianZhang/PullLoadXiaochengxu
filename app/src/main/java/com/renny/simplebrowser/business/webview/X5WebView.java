package com.renny.simplebrowser.business.webview;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Renny on 2018/1/8.
 */

public class X5WebView extends WebView {
    public X5WebView(Context context) {
        this(context, null);
    }

    public X5WebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public X5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        WebSettings setting = getSettings();
        setting.setGeolocationEnabled(true);
        setting.setJavaScriptEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setAppCacheMaxSize(1024 * 1024 * 10);
        setting.setAppCacheEnabled(true);
        setting.setSupportZoom(true);
        setting.setTextSize(WebSettings.TextSize.NORMAL);
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        setting.setAppCachePath(appCachePath);
        setting.setAllowFileAccess(true);
        setting.setSavePassword(false);
        setting.setJavaScriptCanOpenWindowsAutomatically(false);
        // 自适应屏幕
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
       /* setOnLongClickListener(new View.OnLongClickListener() {

            public boolean onLongClick(View v) {
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
                if (null == result)
                    return false;
                int type = result.getType();
                switch (type) {
                    case WebView.HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                        break;
                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                        break;
                    case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                        break;
                    case WebView.HitTestResult.GEO_TYPE: // 　地图类型
                        break;
                    case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                        break;
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                    case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项 }
                        return true;
                    case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                        break;

                }
                return false;

            }

        });*/
    }
}
