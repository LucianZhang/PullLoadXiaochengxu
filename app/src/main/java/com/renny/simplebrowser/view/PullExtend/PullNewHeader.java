package com.renny.simplebrowser.view.PullExtend;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.renny.simplebrowser.R;


/**
 * 这个类封装了下拉刷新的布局
 *
 * @author chunsoft
 * @since 2015-8-26
 */
public class PullNewHeader extends ExtendLayout {


    float ContainerHeight = dip2px(60);
    float TotalHeight = dip2px(160);
    float ListHeight = dip2px(80);

    private RecyclerView mRecyclerView;
    /**
     * Header的容器
     */
    private RelativeLayout mHeaderContainer;
    /**
     * 箭头图片
     */
    boolean arrived = false;

    ExpendPoint mExpendPoint;

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullNewHeader(Context context) {
        super(context);
        init(context);
    }


    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullNewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        mRecyclerView = findViewById(R.id.list);
        mExpendPoint = findViewById(R.id.expend_point);
        mHeaderContainer = findViewById(R.id.pull_to_refresh_header_content);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        return LayoutInflater.from(context).inflate(R.layout.extend_header, null);
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {

    }


    @Override
    public int getContentSize() {
        return (int) (ListHeight);
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        mExpendPoint.setAlpha(1);
        mRecyclerView.setTranslationY(-ContainerHeight);
    }

    @Override
    protected void onReleaseToRefresh() {
    }

    @Override
    protected void onPullToRefresh() {

    }

    @Override
    protected void onRefreshing() {
    }

    @Override
    public void onPull(int offset) {
        float percent = Math.abs(offset) / ContainerHeight;
        int moreOffset = Math.abs(offset) - (int) ContainerHeight;
        if (percent <= 1.0f) {
            mExpendPoint.setPercent(percent);
            mExpendPoint.setTranslationY(-Math.abs(offset) / 2 + mExpendPoint.getHeight() / 2);
        } else {
            float subPercent = (moreOffset) / (ListHeight - ContainerHeight);
            subPercent = Math.min(1.0f, subPercent);
            mExpendPoint.setTranslationY(-(int) ContainerHeight / 2 + mExpendPoint.getHeight() / 2 + (int) ContainerHeight * subPercent / 2);
            mExpendPoint.setPercent(1.0f);
            float alpha = (1 - subPercent * 2);
            mExpendPoint.setAlpha(Math.max(alpha, 0));
            mRecyclerView.setTranslationY(-(1 - subPercent) * ContainerHeight);
            Log.d("vvvv", subPercent + "");
        }
        if (Math.abs(offset) > ListHeight) {
            mRecyclerView.setTranslationY(-(Math.abs(offset) - ListHeight) / 2);
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
