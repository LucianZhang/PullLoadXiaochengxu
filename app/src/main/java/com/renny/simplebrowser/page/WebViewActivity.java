package com.renny.simplebrowser.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.ViewDragHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.renny.simplebrowser.R;
import com.renny.simplebrowser.base.BaseActivity;
import com.renny.simplebrowser.business.sp.SPHelper;
import com.renny.simplebrowser.widget.GestureLayout;
import com.tencent.smtt.sdk.WebBackForwardList;
import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends BaseActivity implements WebViewFragment.OnReceivedListener {
    WebViewFragment webViewFragment;
    HomePageFragment mHomePageFragment;
    TextView titleView;
    ImageView mark;
    GestureLayout mGestureLayout;
    FragmentManager mFragmentManager;
    private boolean isOnHomePage = false;
    private boolean fromBack = false;
    private long mExitTime = 0;
    String url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        titleView = findViewById(R.id.title);
        mGestureLayout = findViewById(R.id.gesture_layout);
        mark = findViewById(R.id.mark);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> markList = SPHelper.getBean("MARK_LIST", List.class);
                if (markList == null) {
                    markList = new ArrayList<>();
                }
                if (!TextUtils.isEmpty(url)) {
                    mark.setVisibility(View.VISIBLE);
                    if (mark.isSelected()) {
                        markList.remove(url);
                        mark.setSelected(false);
                    } else {
                        markList.add(url);
                        SPHelper.putBean("MARK_LIST", markList);
                        mark.setSelected(true);
                    }
                } else {
                    mark.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void afterViewBind(Bundle savedInstanceState) {
        super.afterViewBind(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        goHomePage();
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
                WebBackForwardList list = webView.copyBackForwardList();
                int size = list.getSize();
                if (edgeFlags == ViewDragHelper.EDGE_LEFT) {
                    return webView.canGoBack() || !isOnHomePage || fromBack;
                } else if (edgeFlags == ViewDragHelper.EDGE_RIGHT) {
                    if (isOnHomePage) {
                        return size > 0;
                    } else {
                        return webView.canGoForward();
                    }
                } else if (edgeFlags == ViewDragHelper.EDGE_BOTTOM) {
                    return !isOnHomePage;
                }
                return false;
            }

            @Override
            public void onViewMaxPositionReleased(int edgeFlags, ImageView view) {
                if (edgeFlags == ViewDragHelper.EDGE_LEFT) {
                    returnLastPage();
                } else if (edgeFlags == ViewDragHelper.EDGE_RIGHT) {
                    if (isOnHomePage) {
                        goWebView(null);
                    } else {
                        goNextPage();
                    }
                } else if (edgeFlags == ViewDragHelper.EDGE_BOTTOM) {
                    fromBack = true;
                    goHomePage();
                }
            }

            @Override
            public void onViewMaxPositionArrive(int edgeFlags, ImageView view) {

            }
        });
    }


    private void goWebView(String url) {
        if (webViewFragment == null || !TextUtils.isEmpty(url)) {
            webViewFragment = new WebViewFragment();
            Bundle args = new Bundle();
            args.putString("url", url);
            webViewFragment.setArguments(args);
        }
        mFragmentManager.beginTransaction().replace(R.id.container,
                webViewFragment).commit();
        isOnHomePage = false;
        fromBack = false;
        WebView webView = webViewFragment.getWebView();
        if (webView != null) {
            setTitle(webView.getTitle());
        }
        mark.setVisibility(View.INVISIBLE);
    }

    private void goHomePage() {
        if (mHomePageFragment == null) {
            mHomePageFragment = new HomePageFragment();
            mHomePageFragment.setGoPageListener(new HomePageFragment.goPageListener() {
                @Override
                public void onGopage(String url) {
                    goWebView(url);
                }
            });
            mFragmentManager.beginTransaction().add(R.id.container, mHomePageFragment).commit();
        } else {
            mFragmentManager.beginTransaction().replace(R.id.container,
                    mHomePageFragment).commit();
        }
        onReceivedTitle("", "主页");
        mark.setVisibility(View.INVISIBLE);
        isOnHomePage = true;
    }

    private void returnLastPage() {
        if (fromBack && isOnHomePage) {
            goWebView(null);
        } else {
            WebView webView = webViewFragment.getWebView();
            if (webView.canGoBack()) {
                webView.goBack();
                onReceivedTitle(webView.getUrl(), webView.getTitle());
            } else {
                goHomePage();
            }
        }
    }

    private void goNextPage() {
        WebView webView = webViewFragment.getWebView();
        if (webView.canGoForward()) {
            webView.goForward();
            onReceivedTitle(webView.getUrl(), webView.getTitle());
        }
    }

    @Override
    public void onBackPressed() {
        if (!isOnHomePage) {
            WebView webView = webViewFragment.getWebView();
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                goHomePage();
            }
        } else {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
        }
    }

    @Override
    public void onReceivedTitle(String url, String title) {
        List<String> markList = SPHelper.getBean("MARK_LIST", List.class);
        if (markList == null) {
            markList = new ArrayList<>();
        }
        if (!TextUtils.isEmpty(url)) {
            mark.setVisibility(View.VISIBLE);
            mark.setSelected(markList.contains(url));
            this.url = url;
        } else {
            mark.setSelected(false);
            mark.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
    }

}
