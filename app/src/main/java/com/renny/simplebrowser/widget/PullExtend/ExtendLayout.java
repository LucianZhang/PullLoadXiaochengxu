package com.renny.simplebrowser.widget.PullExtend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 这个类定义了Header和Footer的共通行为
 *
 * @author Li Hong
 * @since 2013-8-16
 */
public abstract class ExtendLayout extends FrameLayout implements IExtendLayout {

    /**
     * 当前的状态
     */
    private State mCurState = State.NONE;
    /**
     * 前一个状态
     */
    private State mPreState = State.NONE;


    public ExtendLayout(Context context) {
        this(context, null);
    }


    public ExtendLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ExtendLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        View container = createLoadingView(context, attrs);
        if (null == container) {
            throw new NullPointerException("Loading view can not be null.");
        }
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        addView(container, params);
        bindView(container);
    }

    protected void bindView(View container) {

    }

    @Override
    public void setState(State state) {
        if (mCurState != state) {
            mPreState = mCurState;
            mCurState = state;
            onStateChanged(state, mPreState);
        }
    }

    @Override
    public State getState() {
        return mCurState;
    }

    @Override
    public void onPull(int offset) {

    }

    /**
     * 得到前一个状态
     *
     * @return 状态
     */
    protected State getPreState() {
        return mPreState;
    }

    /**
     * 当状态改变时调用
     *
     * @param curState 当前状态
     * @param oldState 老的状态
     */
    protected void onStateChanged(State curState, State oldState) {
        switch (curState) {
            case RESET:
                onReset();
                break;

            case beyondListHeight:
                onReleaseToRefresh();
                break;

            case PULL_TO_REFRESH:
                onPullToRefresh();
                break;

            case startShowList:
                onRefreshing();
                break;

            case arrivedListHeight:
                onArrivedListHeight();
                break;

            default:
                break;
        }
    }

    /**
     * 当状态设置为{@link State#RESET}时调用
     */
    protected void onReset() {

    }

    /**
     * 当状态设置为{@link State#PULL_TO_REFRESH}时调用
     */
    protected void onPullToRefresh() {

    }

    /**
     * 当状态设置为{@link State#beyondListHeight}时调用
     */
    protected void onReleaseToRefresh() {

    }

    /**
     * 当状态设置为{@link State#startShowList}时调用
     */
    protected void onRefreshing() {

    }

    /**
     * 当状态设置为{@link State#arrivedListHeight}时调用
     */
    protected void onArrivedListHeight() {

    }

    /**
     * 得到当前Layout的内容大小，它将作为一个刷新的临界点
     *
     * @return 高度
     */
    public abstract int getContentSize();

    public abstract int getListSize();

    /**
     * 创建Loading的View
     *
     * @param context context
     * @param attrs   attrs
     * @return Loading的View
     */
    protected abstract View createLoadingView(Context context, AttributeSet attrs);
}
