package com.renny.simplebrowser.widget.PullExtend;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.renny.simplebrowser.widget.PullExtend.IExtendLayout.State;


/**
 * 这个实现了下拉刷新和上拉加载更多的功能
 *
 * @author Li Hong
 * @since 2013-7-29
 */
public class PullExtendLayout extends LinearLayout implements IPullToExtend {

    /**
     * 回滚的时间
     */
    private static final int SCROLL_DURATION = 150;
    /**
     * 阻尼系数
     */
    private float offsetRadio = 1.0f;

    /**
     * 上一次移动的点
     */
    private float mLastMotionY = -1;
    /**
     * 下拉刷新和加载更多的监听器
     */
    private OnRefreshListener mRefreshListener;
    /**
     * 下拉刷新的布局
     */
    private ExtendLayout mHeaderLayout;

    /**
     * 上拉加载更多的布局
     */
    private ExtendLayout mFooterLayout;
    /**
     * 列表开始显示的高度
     */
    private int mHeaderHeight;
    /**
     * 列表的高度
     */
    private int headerListHeight;
    /**
     * FooterView的高度
     */
    private int mFooterHeight;
    /**
     * 列表的高度
     */
    private int footerListHeight;

    /**
     * 下拉刷新是否可用
     */
    private boolean mPullRefreshEnabled = true;
    /**
     * 上拉加载是否可用
     */
    private boolean mPullLoadEnabled = true;

    /**
     * 是否截断touch事件
     */
    private boolean mInterceptEventEnable = true;

    /**
     * 表示是否消费了touch事件，如果是，则不调用父类的onTouchEvent方法
     */
    private boolean mIsHandledTouchEvent = false;
    /**
     * 移动点的保护范围值
     */
    private int mTouchSlop;
    /**
     * 下拉的状态
     */
    private State mPullDownState = State.NONE;
    /**
     * 上拉的状态
     */
    private State mPullUpState = State.NONE;
    /**
     * 可以下拉刷新的View
     */
    View mRefreshableView;
    /**
     * 平滑滚动的Runnable
     */
    private SmoothScrollRunnable mSmoothScrollRunnable;


    /**
     * 构造方法
     *
     * @param context context
     */
    public PullExtendLayout(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullExtendLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullExtendLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount == 2) {
            if (getChildAt(0) instanceof ExtendLayout) {
                mHeaderLayout = (ExtendLayout) getChildAt(0);
                mRefreshableView = getChildAt(1);
            } else {
                mRefreshableView = getChildAt(0);
                mFooterLayout = (ExtendLayout) getChildAt(1);
            }
        } else if (childCount == 3) {
            if (getChildAt(0) instanceof ExtendLayout) {
                mHeaderLayout = (ExtendLayout) getChildAt(0);
            }
            mRefreshableView = getChildAt(1);
            mFooterLayout = (ExtendLayout) getChildAt(2);
        } else {
            throw new IllegalStateException("布局异常，最多三个，最少一个");
        }
        if (mRefreshableView == null) {
            throw new IllegalStateException("布局异常，一定要有内容布局");
        }
        // mRefreshableView.setClickable(true);需要自己设置
        init(getContext());
    }

    public void setOffsetRadio(float offsetRadio) {
        this.offsetRadio = offsetRadio;
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        ViewGroup.LayoutParams layoutParams = mRefreshableView.getLayoutParams();
        layoutParams.height = 10;
        mRefreshableView.setLayoutParams(layoutParams);
        // 得到Header的高度，这个高度需要用这种方式得到，在onLayout方法里面得到的高度始终是0
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refreshLoadingViewsSize();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /**
     * 初始化padding，我们根据header和footer的高度来设置top padding和bottom padding
     */
    private void refreshLoadingViewsSize() {
        // 得到header和footer的内容高度，它将会作为拖动刷新的一个临界值，如果拖动距离大于这个高度
        // 然后再松开手，就会触发刷新操作
        int headerHeight = (null != mHeaderLayout) ? mHeaderLayout.getContentSize() : 0;
        headerListHeight = (null != mHeaderLayout) ? mHeaderLayout.getListSize() : 0;
        int footerHeight = (null != mFooterLayout) ? mFooterLayout.getContentSize() : 0;
        footerListHeight = (null != mFooterLayout) ? mFooterLayout.getListSize() : 0;

        if (headerHeight < 0) {
            headerHeight = 0;
        }

        if (footerHeight < 0) {
            footerHeight = 0;
        }

        mHeaderHeight = headerHeight;
        mFooterHeight = footerHeight;

        // 这里得到Header和Footer的高度，设置的padding的top和bottom就应该是header和footer的高度
        // 因为header和footer是完全看不见的
        headerHeight = (null != mHeaderLayout) ? mHeaderLayout.getMeasuredHeight() : 0;
        footerHeight = (null != mFooterLayout) ? mFooterLayout.getMeasuredHeight() : 0;

        int pLeft = getPaddingLeft();
        int pTop = -headerHeight;
        int pRight = getPaddingRight();
        int pBottom = -footerHeight;
        setPadding(pLeft, pTop, pRight, pBottom);
    }

    @Override
    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        refreshLoadingViewsSize();
        // 设置刷新View的大小
        refreshRefreshableViewSize(w, h);
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    /**
     * 计算刷新View的大小
     *
     * @param width  当前容器的宽度
     * @param height 当前容器的宽度
     */
    protected void refreshRefreshableViewSize(int width, int height) {
        LayoutParams lp = (LayoutParams) mRefreshableView.getLayoutParams();
        if (lp.height != height) {
            lp.height = height;
            mRefreshableView.requestLayout();
        }
    }

    @Override
    public final boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isInterceptTouchEventEnabled()) {
            return false;
        }
        if (!isPullLoadEnabled() && !isPullRefreshEnabled()) {
            return false;
        }
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsHandledTouchEvent = false;
            return false;
        }
        if (action != MotionEvent.ACTION_DOWN && mIsHandledTouchEvent) {
            return true;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = event.getY();
                mIsHandledTouchEvent = false;
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = event.getY() - mLastMotionY;
                Log.d("deltaY", deltaY + " ");
                final float absDiff = Math.abs(deltaY);
                // 位移差大于mTouchSlop，这是为了防止快速拖动引发刷新
                if ((absDiff > mTouchSlop)) {
                    mLastMotionY = event.getY();
                    // 第一个显示出来，Header已经显示或拉下
                    if (isPullRefreshEnabled() || isPullLoadEnabled()) {
                        // 1，Math.abs(getScrollY()) > 0：表示当前滑动的偏移量的绝对值大于0，表示当前HeaderView滑出来了或完全
                        // 不可见，存在这样一种case，当正在刷新时并且RefreshableView已经滑到顶部，向上滑动，那么我们期望的结果是
                        // 依然能向上滑动，直到HeaderView完全不可见
                        // 2，deltaY > 0.5f：表示下拉的值大于0.5f
                        mIsHandledTouchEvent = (Math.abs(getScrollYValue()) > 0 || deltaY > 0.5f || deltaY < -0.5f);
                        // 如果截断事件，我们则仍然把这个事件交给刷新View去处理，典型的情况是让ListView/GridView将按下
                        // Child的Selector隐藏
                        if (mIsHandledTouchEvent) {
                            mRefreshableView.onTouchEvent(event);
                        }
                    }
                }
                break;

            default:
                break;
        }
        return mIsHandledTouchEvent;
    }


    @Override
    public final boolean onTouchEvent(MotionEvent ev) {
        boolean handled = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = ev.getY();
                mIsHandledTouchEvent = false;
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getY() - mLastMotionY;
                mLastMotionY = ev.getY();
                Log.d("llll", deltaY + "");
                if (isPullRefreshEnabled() && isReadyForPullDown(deltaY)) {
                    pullHeaderLayout(deltaY / offsetRadio);
                    handled = true;
                    if (null != mFooterLayout && 0 != mFooterHeight) {
                        mFooterLayout.setState(State.RESET);
                    }
                } else if (isPullLoadEnabled() && isReadyForPullUp(deltaY)) {
                    pullFooterLayout(deltaY / offsetRadio);
                    handled = true;
                    if (null != mHeaderLayout && 0 != mHeaderHeight) {
                        mHeaderLayout.setState(State.RESET);

                    }
                } else {
                    mIsHandledTouchEvent = false;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsHandledTouchEvent) {
                    mIsHandledTouchEvent = false;
                    // 当第一个显示出来时
                    if (isReadyForPullDown(0)) {
                        resetHeaderLayout();
                    } else if (isReadyForPullUp(0)) {
                        resetFooterLayout();
                    }
                }
                break;

            default:
                break;
        }
        return handled;
    }

    @Override
    public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
        mPullRefreshEnabled = pullRefreshEnabled;
    }

    @Override
    public void setPullLoadEnabled(boolean pullLoadEnabled) {
        mPullLoadEnabled = pullLoadEnabled;
    }


    @Override
    public boolean isPullRefreshEnabled() {
        return mPullRefreshEnabled && (null != mHeaderLayout);
    }

    @Override
    public boolean isPullLoadEnabled() {
        return mPullLoadEnabled && (null != mFooterLayout);
    }


    @Override
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }

    @Override
    public void onPullDownRefreshComplete() {
        if (isPullRefreshing()) {
            changedState(State.RESET, true);
            // 回滚动有一个时间，我们在回滚完成后再设置状态为normal
            // 在将ExtendLayout的状态设置为normal之前，我们应该禁止
            // 截断Touch事件，因为设里有一个post状态，如果有post的Runnable
            // 未被执行时，用户再一次发起下拉刷新，如果正在刷新时，这个Runnable
            // 再次被执行到，那么就会把正在刷新的状态改为正常状态，这就不符合期望
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setInterceptTouchEventEnabled(true);
                    mHeaderLayout.setState(State.RESET);
                }
            }, getSmoothScrollDuration());

            resetHeaderLayout();
            setInterceptTouchEventEnabled(false);
        }
    }

    @Override
    public void onPullUpRefreshComplete() {
        if (isPullLoading()) {
            mPullUpState = State.RESET;
            changedState(State.RESET, false);

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setInterceptTouchEventEnabled(true);
                    mFooterLayout.setState(State.RESET);
                }
            }, getSmoothScrollDuration());

            resetFooterLayout();
            setInterceptTouchEventEnabled(false);
        }
    }


    @Override
    public ExtendLayout getHeaderExtendLayout() {
        return mHeaderLayout;
    }

    @Override
    public ExtendLayout getFooterExtendLayout() {
        return mFooterLayout;
    }


    /**
     * 开始刷新，通常用于调用者主动刷新，典型的情况是进入界面，开始主动刷新，这个刷新并不是由用户拉动引起的
     *
     * @param smoothScroll 表示是否有平滑滚动，true表示平滑滚动，false表示无平滑滚动
     * @param delayMillis  延迟时间
     */
    public void doPullRefreshing(final boolean smoothScroll, final long delayMillis) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                int newScrollValue = -mHeaderHeight;
                int duration = smoothScroll ? SCROLL_DURATION : 0;

                smoothScrollTo(newScrollValue, duration, 0);
            }
        }, delayMillis);
    }


    /**
     * 判断刷新的View是否滑动到顶部
     *
     * @return true表示已经滑动到顶部，否则false
     */
    protected boolean isReadyForPullDown(float deltaY) {
        return getScrollYValue() < 0 || (getScrollYValue() == 0 && deltaY > 0);
    }

    /**
     * 判断刷新的View是否滑动到底
     *
     * @return true表示已经滑动到底部，否则false
     */
    protected boolean isReadyForPullUp(float deltaY) {
        return getScrollYValue() > 0 || (getScrollYValue() == 0 && deltaY < 0);
    }


    /**
     * 得到平滑滚动的时间，派生类可以重写这个方法来控件滚动时间
     *
     * @return 返回值时间为毫秒
     */
    protected long getSmoothScrollDuration() {
        return SCROLL_DURATION;
    }


    /**
     * 拉动Header Layout时调用
     *
     * @param delta 移动的距离
     */
    protected void pullHeaderLayout(float delta) {
        // 向上滑动，并且当前scrollY为0时，不滑动
        int oldScrollY = getScrollYValue();
        if (delta < 0 && (oldScrollY - delta) >= 0) {
            setScrollTo(0, 0);
            if (null != mHeaderLayout && 0 != mHeaderHeight) {
                mHeaderLayout.setState(State.RESET);
                mHeaderLayout.onPull(0);
            }
            return;
        }
        // 向下滑动布局
        setScrollBy(0, -(int) delta);
        // 未处于刷新状态，更新箭头
        int scrollY = Math.abs(getScrollYValue());
        if (null != mHeaderLayout && 0 != mHeaderHeight) {
            if (scrollY >= headerListHeight) {
                mHeaderLayout.setState(State.arrivedListHeight);
                setOffsetRadio(1.5f);
            } else {
                setOffsetRadio(1.0f);
            }
            mHeaderLayout.onPull(scrollY);
        }
    }

    /**
     * 拉Footer时调用
     *
     * @param delta 移动的距离
     */
    protected void pullFooterLayout(float delta) {
        int oldScrollY = getScrollYValue();
        if (delta > 0 && (oldScrollY - delta) <= 0) {
            setScrollTo(0, 0);
            if (null != mFooterLayout && 0 != mFooterHeight) {
                mFooterLayout.setState(State.RESET);
                mFooterLayout.onPull(0);
            }
            return;
        }
        setScrollBy(0, -(int) delta);
        int scrollY = Math.abs(getScrollYValue());
        if (null != mFooterLayout && 0 != mFooterHeight) {
            if (scrollY > footerListHeight) {
                mHeaderLayout.setState(State.arrivedListHeight);
                setOffsetRadio(1.5f);
            } else {
                setOffsetRadio(1.0f);
            }
            mFooterLayout.onPull(Math.abs(getScrollYValue()));
        }
    }

    /**
     * 重置header
     */
    protected void resetHeaderLayout() {
        final int scrollY = Math.abs(getScrollYValue());
        if (scrollY < mHeaderHeight) {
            smoothScrollTo(0);
        } else if (scrollY >= mHeaderHeight) {
            smoothScrollTo(-headerListHeight);
        }
    }


    /**
     * 重置footer
     */
    protected void resetFooterLayout() {
        int scrollY = Math.abs(getScrollYValue());
        if (scrollY < mFooterHeight) {
            smoothScrollTo(0);
        } else if (scrollY >= mFooterHeight) {
            smoothScrollTo(footerListHeight);
        }
    }

    /**
     * 判断是否正在下拉刷新
     *
     * @return true正在刷新，否则false
     */
    protected boolean isPullRefreshing() {
        return (mPullDownState == State.startShowList);
    }

    /**
     * 是否正的上拉加载更多
     *
     * @return true正在加载更多，否则false
     */
    protected boolean isPullLoading() {
        return (mPullUpState == State.startShowList);
    }


    /**
     * 当状态发生变化时调用
     *
     * @param state      状态
     * @param isPullDown 是否向下
     */
    protected void changedState(State state, boolean isPullDown) {
        mPullUpState = state;
    }

    /**
     * 设置滚动位置
     *
     * @param x 滚动到的x位置
     * @param y 滚动到的y位置
     */
    private void setScrollTo(int x, int y) {
        scrollTo(x, y);
    }

    /**
     * 设置滚动的偏移
     *
     * @param x 滚动x位置
     * @param y 滚动y位置
     */
    private void setScrollBy(int x, int y) {
        scrollBy(x, y);
    }

    /**
     * 得到当前Y的滚动值
     *
     * @return 滚动值
     */
    private int getScrollYValue() {
        return getScrollY();
    }

    /**
     * 平滑滚动
     *
     * @param newScrollValue 滚动的值
     */
    private void smoothScrollTo(int newScrollValue) {
        smoothScrollTo(newScrollValue, getSmoothScrollDuration(), 0);
    }

    /**
     * 平滑滚动
     *
     * @param newScrollValue 滚动的值
     * @param duration       滚动时候
     * @param delayMillis    延迟时间，0代表不延迟
     */
    private void smoothScrollTo(int newScrollValue, long duration, long delayMillis) {
        if (null != mSmoothScrollRunnable) {
            mSmoothScrollRunnable.stop();
        }

        int oldScrollValue = this.getScrollYValue();
        boolean post = (oldScrollValue != newScrollValue);
        if (post) {
            mSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration);
        }

        if (post) {
            if (delayMillis > 0) {
                postDelayed(mSmoothScrollRunnable, delayMillis);
            } else {
                post(mSmoothScrollRunnable);
            }
        }
    }

    /**
     * 设置是否截断touch事件
     *
     * @param enabled true截断，false不截断
     */
    private void setInterceptTouchEventEnabled(boolean enabled) {
        mInterceptEventEnable = enabled;
    }

    /**
     * 标志是否截断touch事件
     *
     * @return true截断，false不截断
     */
    private boolean isInterceptTouchEventEnabled() {
        return mInterceptEventEnable;
    }

    /**
     * 实现了平滑滚动的Runnable
     *
     * @author Li Hong
     * @since 2013-8-22
     */
    final class SmoothScrollRunnable implements Runnable {
        /**
         * 动画效果
         */
        private final Interpolator mInterpolator;
        /**
         * 结束Y
         */
        private final int mScrollToY;
        /**
         * 开始Y
         */
        private final int mScrollFromY;
        /**
         * 滑动时间
         */
        private final long mDuration;
        /**
         * 是否继续运行
         */
        private boolean mContinueRunning = true;
        /**
         * 开始时刻
         */
        private long mStartTime = -1;
        /**
         * 当前Y
         */
        private int mCurrentY = -1;

        /**
         * 构造方法
         *
         * @param fromY    开始Y
         * @param toY      结束Y
         * @param duration 动画时间
         */
        public SmoothScrollRunnable(int fromY, int toY, long duration) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mDuration = duration;
            mInterpolator = new DecelerateInterpolator();
        }

        @Override
        public void run() {
            /**
             * If the duration is 0, we scroll the view to target y directly.
             */
            if (mDuration <= 0) {
                setScrollTo(0, mScrollToY);
                return;
            }

            /**
             * Only set mStartTime if this is the first time we're starting,
             * else actually calculate the Y delta
             */
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            } else {

                final long oneSecond = 1000;    // SUPPRESS CHECKSTYLE
                long normalizedTime = (oneSecond * (System.currentTimeMillis() - mStartTime)) / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, oneSecond), 0);

                final int deltaY = Math.round((mScrollFromY - mScrollToY)
                        * mInterpolator.getInterpolation(normalizedTime / (float) oneSecond));
                mCurrentY = mScrollFromY - deltaY;
                setScrollTo(0, mCurrentY);
                Log.d("setScrollTo", " " + mCurrentY);

                if (null != mHeaderLayout && 0 != mHeaderHeight) {
                    mHeaderLayout.onPull(Math.abs(mCurrentY));
                    if (mCurrentY == 0) {
                        mHeaderLayout.setState(State.RESET);
                    }
                    if (Math.abs(mCurrentY) == headerListHeight) {
                        mHeaderLayout.setState(State.arrivedListHeight);
                    }
                }
                if (null != mFooterLayout && 0 != mFooterHeight) {
                    mFooterLayout.onPull(Math.abs(mCurrentY));
                    if (mCurrentY == 0) {
                        mFooterLayout.setState(State.RESET);
                    }
                    if (Math.abs(mCurrentY) == footerListHeight) {
                        mFooterLayout.setState(State.arrivedListHeight);
                    }
                }
            }

            // If we're not at the target Y, keep going...
            if (mContinueRunning && mScrollToY != mCurrentY) {
                PullExtendLayout.this.postDelayed(this, 16);// SUPPRESS CHECKSTYLE
            }
        }

        /**
         * 停止滑动
         */
        public void stop() {
            mContinueRunning = false;
            removeCallbacks(this);
        }
    }
}
