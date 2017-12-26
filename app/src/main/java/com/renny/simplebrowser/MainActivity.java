package com.renny.simplebrowser;

import android.os.Bundle;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebBackForwardList;
import com.tencent.smtt.sdk.WebView;

public class MainActivity extends AppCompatActivity implements WebViewFragment.OnReceivedListener {
    private final String WEB_TAG = "simple_web";
    WebViewFragment webViewFragment;
    TextView titleView;
    GestureLayout mGestureLayout;
    private int pageCount = 0;
    private String homePage = "https://juejin.im/user/5795bb80d342d30059f14b1c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleView = findViewById(R.id.title);
        mGestureLayout = findViewById(R.id.gesture_layout);
        mGestureLayout.setGestureListener(new GestureLayout.GestureListener() {
            @Override
            public boolean dragStartedEnable(int edgeFlags, ImageView view) {
                if (webViewFragment == null) {
                    return false;
                }
                WebView webView = webViewFragment.getWebView();
                if (webView == null) {
                    return false;
                }
                if (edgeFlags == ViewDragHelper.EDGE_LEFT) {
                    return webView.canGoBack();
                } else if (edgeFlags == ViewDragHelper.EDGE_RIGHT) {
                    return webView.canGoForward();
                } else if (edgeFlags == ViewDragHelper.EDGE_BOTTOM) {
                    return !(TextUtils.equals(webView.getUrl(), homePage) || pageCount == 1);
                }
                return false;
            }

            @Override
            public void onViewMaxPositionReleased(int edgeFlags, ImageView view) {
                if (edgeFlags == ViewDragHelper.EDGE_LEFT) {
                    backPreviousPage();
                } else if (edgeFlags == ViewDragHelper.EDGE_RIGHT) {
                    goNextPage();
                } else if (edgeFlags == ViewDragHelper.EDGE_BOTTOM) {
                    goHomePage();
                }
                //Log.d(WEB_TAG, "Released: " + edgeFlags);

            }

            @Override
            public void onViewMaxPositionArrive(int edgeFlags, ImageView view) {
                if (edgeFlags == ViewDragHelper.EDGE_LEFT) {

                } else if (edgeFlags == ViewDragHelper.EDGE_RIGHT) {

                } else if (edgeFlags == ViewDragHelper.EDGE_BOTTOM) {

                }
            }
        });
        if (savedInstanceState == null) {
            webViewFragment = new WebViewFragment();
            Bundle args = new Bundle();
            args.putString("url", homePage);
            webViewFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    webViewFragment, WEB_TAG).commit();
        } else {
            webViewFragment = (WebViewFragment) getSupportFragmentManager()
                    .findFragmentByTag(WEB_TAG);
        }

    }

    private void goNextPage() {
        WebView webView = webViewFragment.getWebView();

        webView.goForward();

    }

    private void goHomePage() {
        WebView webView = webViewFragment.getWebView();
        webView.loadUrl(homePage);
    }

    private void backPreviousPage() {
        WebView webView = webViewFragment.getWebView();
        webView.goBack();
        pageCount--;
    }

    @Override
    public void onBackPressed() {
        WebView webView = webViewFragment.getWebView();
        if (webView.canGoBack()) {
            int backStep = -1;
            WebBackForwardList list = webView.copyBackForwardList();
            int size = list.getSize();
            if (size >= 2) {
                if (list.getCurrentIndex() + 1 + backStep > 0) {//未超出历史范围则回退
                    webView.goBackOrForward(backStep);
                    pageCount--;
                    return;
                }
            }
        }
        finish();
    }

    @Override
    public void onReceivedTitle(String title) {
        titleView.setText(title);
    }

    @Override
    public void onPageFinish(WebView webView,String url) {
        WebBackForwardList list = webView.copyBackForwardList();
        boolean isFromBack = (list.getCurrentIndex() != list.getSize() - 1);
        if (!isFromBack) {
            pageCount++;
        }
        Log.d(WEB_TAG, pageCount + "   " + list.getSize() + "  " + url);
    }

}
