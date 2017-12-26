package com.renny.simplebrowser;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Renny on 2017/12/25.
 */

public class GestureLayout extends RelativeLayout {

    private ViewDragHelper mViewDragHelper;

    private ImageView leftRefreshView, rightRefreshView, backHomeView;

    private Point mLeftPos, mRightPos, mHomePos;
    private GestureListener mGestureListener;

    public GestureLayout(@NonNull Context context) {
        this(context, null);
    }

    public GestureLayout(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLeftPos = new Point();
        mRightPos = new Point();
        mHomePos = new Point();
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //mEdgeTrackerView禁止直接移动
                return false;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int topBound = 0;
                int bottomBound = 0;
                if (child == leftRefreshView) {
                    return mLeftPos.y;
                } else if (child == rightRefreshView) {
                    return mRightPos.y;
                } else if (child == backHomeView) {
                    topBound = getHeight() - child.getHeight();
                    bottomBound = getHeight();
                }
                return Math.min(Math.max(top, topBound), bottomBound);
            }

            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int leftBound = 0;
                int rightBound = 0;
                if (child == leftRefreshView) {
                    leftBound = -child.getWidth();
                } else if (child == rightRefreshView) {
                    leftBound = getWidth() - child.getWidth();
                    rightBound = getWidth();
                } else if (child == backHomeView) {
                    return mHomePos.x;
                }
                return Math.min(Math.max(left, leftBound), rightBound);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (mGestureListener != null) {
                    if (changedView == leftRefreshView && leftRefreshView.getLeft() == 0) {
                        mGestureListener.onViewMaxPositionArrive(ViewDragHelper.EDGE_LEFT, leftRefreshView);
                    } else if (changedView == rightRefreshView && rightRefreshView.getRight() == getWidth()) {
                        mGestureListener.onViewMaxPositionArrive(ViewDragHelper.EDGE_RIGHT, rightRefreshView);
                    } else if (changedView == backHomeView && backHomeView.getBottom() == getHeight()) {
                        mGestureListener.onViewMaxPositionArrive(ViewDragHelper.EDGE_BOTTOM, backHomeView);
                    }
                }
            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //mAutoBackView手指释放时可以自动回去
                if (mGestureListener != null) {

                    if (releasedChild == leftRefreshView) {
                        if (leftRefreshView.getLeft() == 0) {
                            mGestureListener.onViewMaxPositionReleased(ViewDragHelper.EDGE_LEFT, leftRefreshView);
                        }
                        mViewDragHelper.settleCapturedViewAt(mLeftPos.x, mLeftPos.y);
                        invalidate();
                    } else if (releasedChild == rightRefreshView) {
                        if (rightRefreshView.getRight() == getWidth()) {
                            mGestureListener.onViewMaxPositionReleased(ViewDragHelper.EDGE_RIGHT, rightRefreshView);
                        }
                        mViewDragHelper.settleCapturedViewAt(mRightPos.x, mRightPos.y);
                        invalidate();
                    } else if (releasedChild == backHomeView) {
                        if (backHomeView.getBottom() == getHeight()) {
                            mGestureListener.onViewMaxPositionReleased(ViewDragHelper.EDGE_BOTTOM, backHomeView);
                        }
                        mViewDragHelper.settleCapturedViewAt(mHomePos.x, mHomePos.y);
                        invalidate();
                    }
                }
            }

            //在边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                if (mGestureListener != null) {
                    ImageView dragView = null;
                    if (edgeFlags == ViewDragHelper.EDGE_LEFT) {
                        dragView = leftRefreshView;
                    } else if (edgeFlags == ViewDragHelper.EDGE_RIGHT) {
                        dragView = rightRefreshView;
                    } else if (edgeFlags == ViewDragHelper.EDGE_BOTTOM) {
                        dragView = backHomeView;
                    }
                    if (dragView != null && mGestureListener.dragStartedEnable(edgeFlags, dragView)) {
                        mViewDragHelper.captureChildView(dragView, pointerId);
                    }
                }
            }
        });
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_RIGHT | ViewDragHelper.EDGE_BOTTOM);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (leftRefreshView == null) {
            leftRefreshView = new ImageView(getContext());
            RelativeLayout.LayoutParams lpLeft = new RelativeLayout.LayoutParams(80, 40);
            lpLeft.addRule(CENTER_VERTICAL);
            addView(leftRefreshView, lpLeft);
            leftRefreshView.setBackgroundResource(R.color.colorAccent);
        }
        if (rightRefreshView == null) {
            rightRefreshView = new ImageView(getContext());
            RelativeLayout.LayoutParams lpRight = new RelativeLayout.LayoutParams(80, 40);
            lpRight.addRule(CENTER_VERTICAL);
            addView(rightRefreshView, lpRight);
            rightRefreshView.setBackgroundResource(R.color.colorAccent);
        }
        if (backHomeView == null) {
            backHomeView = new ImageView(getContext());
            RelativeLayout.LayoutParams lpHome = new RelativeLayout.LayoutParams(40, 80);
            lpHome.addRule(CENTER_HORIZONTAL);
            addView(backHomeView, lpHome);
            backHomeView.setBackgroundResource(R.color.colorAccent);
        }
    }

    public void setGestureListener(GestureListener gestureListener) {
        mGestureListener = gestureListener;
    }

    public interface GestureListener {

        boolean dragStartedEnable(int edgeFlags, ImageView view);

        void onViewMaxPositionReleased(int edgeFlags, ImageView view);

        void onViewMaxPositionArrive(int edgeFlags, ImageView view);
    }

    /**
     * dp转px
     */
    public int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}