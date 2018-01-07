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
import com.renny.simplebrowser.business.db.dao.BookMarkDao;
import com.renny.simplebrowser.business.db.entity.BookMark;
import com.renny.simplebrowser.business.log.Logs;
import com.renny.simplebrowser.listener.GoPageListener;
import com.renny.simplebrowser.widget.GestureLayout;
import com.tencent.smtt.sdk.WebBackForwardList;
import com.tencent.smtt.sdk.WebView;

import java.util.List;

/**
 * Created by Renny on 2018/1/2.
 */
public class WebViewActivity extends BaseActivity implements WebViewFragment.OnReceivedListener {
    WebViewFragment webViewFragment;
    HomePageFragment mHomePageFragment;
    TextView titleView;
    ImageView mark;
    View bottomBar;
    GestureLayout mGestureLayout;
    FragmentManager mFragmentManager;
    private boolean isOnHomePage = false;
    private boolean fromBack = false;
    private long mExitTime = 0;
    String url, title;
    BookMarkDao mMarkDao;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        titleView = findViewById(R.id.title);
        bottomBar = findViewById(R.id.bottom_bar);
        mGestureLayout = findViewById(R.id.gesture_layout);
        mark = findViewById(R.id.mark);
        mark.setOnClickListener(this);
    }


    @Override
    protected void afterViewBind(Bundle savedInstanceState) {
        super.afterViewBind(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        mMarkDao = new BookMarkDao();
        List<BookMark> markList = mMarkDao.queryForAll();
        if (markList == null || markList.isEmpty()) {
            mMarkDao.addMark(new BookMark("我的掘金主页", "https://juejin.im/user/5795bb80d342d30059f14b1c"));
            mMarkDao.addMark(new BookMark("GitHub地址", "https://github.com/renjianan/SimpleBrowser"));
            mMarkDao.addMark(new BookMark("百度", "https://www.baidu.com/"));
        }
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.mark:
                if (!TextUtils.isEmpty(url)) {
                    if (mark.isSelected()) {
                        mMarkDao.delete(url);
                        mark.setSelected(false);
                    } else {
                        mMarkDao.addMark(new BookMark(title, url));
                        mark.setSelected(true);
                    }
                    mHomePageFragment.refreshMarklist();
                }
                break;
        }
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
            onReceivedTitle(webView.getUrl(), webView.getTitle());
        }
    }

    private void goHomePage() {
        if (mHomePageFragment == null) {
            mHomePageFragment = new HomePageFragment();
            mHomePageFragment.setGoPageListener(new GoPageListener() {
                @Override
                public void onGoPage(String url) {
                    if (!TextUtils.isEmpty(url)) {
                        goWebView(url);
                    } else {
                        goHomePage();
                    }
                }
            });
            mFragmentManager.beginTransaction().add(R.id.container, mHomePageFragment).commit();
        } else {
            mFragmentManager.beginTransaction().replace(R.id.container,
                    mHomePageFragment).commit();
        }
        titleView.setText("主页");
        mark.setVisibility(View.INVISIBLE);
        isOnHomePage = true;
    }


    public void goSearchPage() {
        startActivityForResult(new Intent(WebViewActivity.this, SearchActivity.class), 123);
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
        Logs.base.d("onReceivedTitle:  " + title + "   " + url);
        mark.setVisibility(View.VISIBLE);
        this.url = url;
        this.title = title;
        mark.setSelected(mMarkDao.query(url));
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && data != null) {
            String result = data.getStringExtra("url");
            if (!TextUtils.isEmpty(result)) {
                goWebView(result);
            }
        }
    }


}
