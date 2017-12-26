package com.renny.simplebrowser;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Renny on 2017/12/25.
 */

public class VDHLayout extends RelativeLayout {

    private View leftRefreshView, rightRefreshView, backHomeView;

    private Point mLeftPos, mRightPos, mHomePos;

    public VDHLayout(@NonNull Context context) {
        this(context, null);
    }

    public VDHLayout(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VDHLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLeftPos = new Point();
        mRightPos = new Point();
        mHomePos = new Point();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (leftRefreshView == null) {
            leftRefreshView = new View(getContext());
            RelativeLayout.LayoutParams lpLeft = new RelativeLayout.LayoutParams(80, 40);
            lpLeft.addRule(CENTER_VERTICAL);
            addView(leftRefreshView, lpLeft);
            leftRefreshView.setBackgroundResource(R.color.colorAccent);
        }
        if (rightRefreshView == null) {
            rightRefreshView = new View(getContext());
            RelativeLayout.LayoutParams lpRight = new RelativeLayout.LayoutParams(80, 40);
            lpRight.addRule(CENTER_VERTICAL);
            addView(rightRefreshView, lpRight);
            rightRefreshView.setBackgroundResource(R.color.colorAccent);
        }
        if (backHomeView == null) {
            backHomeView = new View(getContext());
            RelativeLayout.LayoutParams lpHome = new RelativeLayout.LayoutParams(40, 80);
            lpHome.addRule(CENTER_HORIZONTAL);
            addView(backHomeView, lpHome);
            backHomeView.setBackgroundResource(R.color.colorAccent);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        leftRefreshView.layout(-leftRefreshView.getWidth(), leftRefreshView.getTop(), 0, leftRefreshView.getBottom());
        mLeftPos.x = leftRefreshView.getLeft();
        mLeftPos.y = leftRefreshView.getTop();

        rightRefreshView.layout(getWidth(), rightRefreshView.getTop(), getWidth() + rightRefreshView.getWidth(), rightRefreshView.getBottom());
        mRightPos.x = rightRefreshView.getLeft();
        mRightPos.y = rightRefreshView.getTop();

        backHomeView.layout(backHomeView.getLeft(), getHeight(), backHomeView.getRight(), getHeight() + backHomeView.getHeight());
        mHomePos.x = backHomeView.getLeft();
        mHomePos.y = backHomeView.getTop();
    }

    /**
     * dp转px
     */
    public int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}