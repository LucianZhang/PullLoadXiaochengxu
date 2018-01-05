package com.renny.simplebrowser.widget.PullExtend;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.renny.simplebrowser.R;



public class ExtendListFooter extends ExtendLayout {

    float containerHeight = dip2px(60);
    float listHeight = dip2px(120);
    boolean arrivedListHeight = false;
    private RecyclerView mRecyclerView;

    /**
     * 圆点
     */
    private ExpendPoint mExpendPoint;

    public ExtendListFooter(Context context) {
        super(context);

    }


    public ExtendListFooter(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void bindView(View container) {
        super.bindView(container);
        mRecyclerView = findViewById(R.id.list);
        mExpendPoint = findViewById(R.id.expend_point);
    }


    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        return LayoutInflater.from(context).inflate(R.layout.extend_footer, null);
    }


    @Override
    public int getContentSize() {
        return (int) (containerHeight);
    }

    @Override
    public int getListSize() {
        return (int) (listHeight);
    }


    @Override
    protected void onReset() {
        mExpendPoint.setAlpha(1);
        mExpendPoint.setTranslationY(0);
        mExpendPoint.setVisibility(VISIBLE);
        mRecyclerView.setTranslationY(0);
        arrivedListHeight = false;
    }

    @Override
    protected void onReleaseToRefresh() {
    }

    @Override
    protected void onPullToRefresh() {

    }

    @Override
    protected void onArrivedListHeight() {
        arrivedListHeight = true;
    }

    @Override
    protected void onRefreshing() {
    }

    @Override
    public void onPull(int offset) {
        if (!arrivedListHeight) {
            mExpendPoint.setVisibility(VISIBLE);
            int moreOffset = Math.abs(offset) - (int) containerHeight;
            float percent = Math.abs(offset) / containerHeight;
            if (percent <= 1.0f) {
                mExpendPoint.setPercent(percent);
                mExpendPoint.setTranslationY(Math.abs(offset) / 2 - mExpendPoint.getHeight() / 2);
                mRecyclerView.setTranslationY(containerHeight);
            } else {
                float subPercent = (moreOffset) / (listHeight - containerHeight);
                subPercent = Math.min(1.0f, subPercent);
                mExpendPoint.setTranslationY((int) containerHeight / 2 - mExpendPoint.getHeight() / 2 - (int) containerHeight * subPercent / 2);
                mExpendPoint.setPercent(1.0f);
                float alpha = (1 - subPercent * 2);
                mExpendPoint.setAlpha(Math.max(alpha, 0));
                mRecyclerView.setTranslationY((1 - subPercent) * containerHeight);
            }
        }
        if (Math.abs(offset) >= listHeight) {
            mExpendPoint.setVisibility(INVISIBLE);
            mRecyclerView.setTranslationY((Math.abs(offset) - listHeight) / 2);
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * dp转px
     */
    public float dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

}
