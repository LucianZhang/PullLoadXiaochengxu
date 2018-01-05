package com.renny.simplebrowser.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Renny on 2018/1/4.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ArrayList<View> outView = new ArrayList<View>();
                getWindow().getDecorView().findViewsWithText(outView, "QQ浏览器", View.FIND_VIEWS_WITH_TEXT);
                if (outView.size() > 0) {
                    outView.get(0).setVisibility(View.GONE);
                }
            }
        });
        bindView(savedInstanceState);
        afterViewBind(savedInstanceState);
    }

    protected void bindView(Bundle savedInstanceState) {

    }
    protected void afterViewBind(Bundle savedInstanceState) {

    }

    protected abstract int getLayoutId();

    @Override
    public void onClick(View v) {

    }
}
