package com.renny.simplebrowser.view.widget.pullrefresh;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renny.simplebrowser.R;


/**
 * 这个类封装了下拉刷新的布局
 *
 * @author chunsoft
 * @since 2015-8-26
 */
public class RotateLoadingLayout extends LoadingLayout {
    /**
     * 旋转动画的时间
     */
    static final int ROTATION_ANIMATION_DURATION = 1200;
    /**
     * 动画插值
     */
    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    /**
     * Header的容器
     */
    private RelativeLayout mHeaderContainer;
    /**
     * 箭头图片
     */
    private ImageView mArrowImageView;

    /**
     * 最后更新时间的TextView
     */
    private TextView mHeaderTimeView;
    /**
     * 最后更新时间的标题
     */
    private TextView mHeaderTimeViewTitle;
    /**
     * 旋转的动画
     */
    private Animation mRotateAnimation;

    /**
     * 构造方法
     *
     * @param context context
     */
    public RotateLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public RotateLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        mHeaderContainer = findViewById(R.id.pull_to_refresh_header_content);
        mArrowImageView = findViewById(R.id.pull_to_refresh_header_arrow);
        float pivotValue = 0.5f;
        float toDegree = 720.0f;
        mRotateAnimation = new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateAnimation.setFillAfter(true);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        return LayoutInflater.from(context).inflate(R.layout.refresh_header, null);
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        mHeaderTimeViewTitle.setVisibility(TextUtils.isEmpty(label) ? INVISIBLE : VISIBLE);
        mHeaderTimeView.setText(label);
    }

    @Override
    public int getContentSize() {
        if (null != mHeaderContainer) {
            return mHeaderContainer.getHeight();
        }
        return (int) (getResources().getDisplayMetrics().density * 60);
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        resetRotation();
    }

    @Override
    protected void onReleaseToRefresh() {
    }

    @Override
    protected void onPullToRefresh() {
    }

    @Override
    protected void onRefreshing() {
        resetRotation();
        mArrowImageView.startAnimation(mRotateAnimation);
    }

    @Override
    public void onPull(int  offset) {
        float scale = Math.abs(offset) / (float) getContentSize();
        float angle = scale * 180f; // SUPPRESS CHECKSTYLE
        mArrowImageView.setRotation(angle);
    }

    /**
     * 重置动画
     */
    private void resetRotation() {
        mArrowImageView.clearAnimation();
        mArrowImageView.setRotation(0);
    }
}
