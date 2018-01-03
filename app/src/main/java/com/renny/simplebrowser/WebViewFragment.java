package com.renny.simplebrowser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.renny.simplebrowser.base.BaseFragment;
import com.renny.simplebrowser.widget.pullrefresh.PullToRefreshBase;
import com.renny.simplebrowser.widget.pullrefresh.PullToRefreshWebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;



public class WebViewFragment extends BaseFragment {

    private WebView mWebView;
    private String mUrl;
    private OnReceivedListener onReceivedTitleListener;

    @Override
    protected int getLayoutId() {
        return R.layout.webview_item;
    }


    @Override
    protected void initParams(Bundle bundle) {
        mUrl = bundle.getString("url");
    }


    public WebView getWebView() {
        return mWebView;
    }

    public void afterViewBind(View rootView, Bundle savedInstanceState) {
        final PullToRefreshWebView pullToRefreshWebView = rootView.findViewById(R.id.refreshLayout);
        mWebView = pullToRefreshWebView.getRefreshableView();
        pullToRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<WebView> refreshView) {
                mWebView.reload();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<WebView> refreshView) {

            }
        });
        pullToRefreshWebView.setPullLoadEnabled(false);
        WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setAllowFileAccess(true);
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView webView, String title) {
                super.onReceivedTitle(webView, title);
                if (onReceivedTitleListener != null) {
                    onReceivedTitleListener.onReceivedTitle(title);

                }
            }
        };
        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                pullToRefreshWebView.onPullDownRefreshComplete();

            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                pullToRefreshWebView.onPullDownRefreshComplete();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    webView.loadUrl(url);
                    return true;
                } else {
                    return jumpScheme(url);
                }
            }
        };
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        mWebView.loadUrl(mUrl);
    }

    private boolean jumpScheme(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("referer", url);
            getActivity().startActivity(intent);
        } catch (Exception e) {
            Log.e("xxx", "您所打开的第三方App未安装！");
            return false;
        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof OnReceivedListener) {
            onReceivedTitleListener = (OnReceivedListener) context;
        }
        super.onAttach(context);
    }

    public interface OnReceivedListener {
        void onReceivedTitle(String title);
    }
}
