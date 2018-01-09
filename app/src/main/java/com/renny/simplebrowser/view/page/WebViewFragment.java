package com.renny.simplebrowser.view.page;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.business.base.BaseFragment;
import com.renny.simplebrowser.business.toast.ToastHelper;
import com.renny.simplebrowser.business.webview.X5WebChromeClient;
import com.renny.simplebrowser.business.webview.X5WebView;
import com.renny.simplebrowser.business.webview.X5WebViewClient;
import com.renny.simplebrowser.view.widget.pullrefresh.PullToRefreshBase;
import com.renny.simplebrowser.view.widget.pullrefresh.PullToRefreshWebView;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;


public class WebViewFragment extends BaseFragment {

    private X5WebView mWebView;
    private String mUrl;
    private OnReceivedListener onReceivedTitleListener;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_webview;
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
        List<String> list = new ArrayList<>();
        list.add("Item1");
        list.add("Item2");
        list.add("APIWeb");

        pullToRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<X5WebView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<X5WebView> refreshView) {
                mWebView.reload();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<X5WebView> refreshView) {

            }
        });
        pullToRefreshWebView.setPullLoadEnabled(false);
        WebChromeClient webChromeClient = new X5WebChromeClient(getActivity()) {
            @Override
            public void onReceivedTitle(WebView webView, String title) {
                super.onReceivedTitle(webView, title);
                if (onReceivedTitleListener != null) {
                    onReceivedTitleListener.onReceivedTitle(webView.getUrl(), title);

                }
            }

            //获取图标
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }
        };

        WebViewClient webViewClient = new X5WebViewClient(getActivity()) {

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
        };
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        mWebView.loadUrl(mUrl);
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                getActivity().startActivity(intent);
            }
        });
    }

    private void donwnload(String ingUrl) {
        if (URLUtil.isValidUrl(ingUrl)) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(ingUrl));
            request.allowScanningByMediaScanner();
            //设置图片的保存路径
            request.setDestinationInExternalFilesDir(getActivity(), "/img", "/a.png");
            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);
            ToastHelper.makeToast("下载成功");
        } else {
            ToastHelper.makeToast("下载失败");
        }
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof OnReceivedListener) {
            onReceivedTitleListener = (OnReceivedListener) context;
        }
        super.onAttach(context);
    }

    public interface OnReceivedListener {
        void onReceivedTitle(String url, String title);
    }
}
