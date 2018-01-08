package com.renny.simplebrowser.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.renny.simplebrowser.R;


public class GestureLayout extends RelativeLayout {

    private ViewDragHelper mViewDragHelper;

    private ImageView leftRefreshView, rightRefreshView, backHomeView;

    private Point mLeftPos, mRightPos, mHomePos;
    private GestureListener mGestureListener;
    @DrawableRes
    int leftDrawableId, rightDrawableId, bottomDrawableId;

    public GestureLayout(@NonNull Context context) {
        this(context, null);
    }

    public GestureLayout(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.GestureLayout);
        leftDrawableId = typedArray.getResourceId(R.styleable.GestureLayout_leftDrawable, R.drawable.bg_left);
        rightDrawableId = typedArray.getResourceId(R.styleable.GestureLayout_rightDrawable, R.drawable.bg_right);
        bottomDrawableId = typedArray.getResourceId(R.styleable.GestureLayout_bottomDrawable, R.drawable.bg_home);
        typedArray.recycle();
        mLeftPos = new Point();
        mRightPos = new Point();
        mHomePos = new Point();
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new GestureDragCallback());
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_RIGHT | ViewDragHelper.EDGE_BOTTOM);
    }

    private class GestureDragCallback extends ViewDragHelper.Callback {
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
                topBound = getHeight() - child.getHeight() * 2;
                bottomBound = getHeight();
            }
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int leftBound = 0;
            int rightBound = 0;
            if (child == leftRefreshView) {
                if (MotionX > child.getWidth() * 3 && dx < 0)
                    return child.getLeft();
                leftBound = -child.getWidth();
            } else if (child == rightRefreshView) {
                if (MotionX < getWidth() - child.getWidth() * 3 && dx > 0)
                    return child.getLeft();
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
                } else if (changedView == backHomeView) {
                    if (backHomeView.getTop() == getHeight() - 2 * backHomeView.getHeight()) {
                        backHomeView.setSelected(true);
                        mGestureListener.onViewMaxPositionArrive(ViewDragHelper.EDGE_BOTTOM, backHomeView);
                    } else {
                        backHomeView.setSelected(false);
                    }
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
                    if (backHomeView.getTop() == getHeight() - 2 * backHomeView.getHeight()) {
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
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    int MotionX = 0, MotionY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MotionX = (int) event.getX();
        MotionY = (int) event.getY();
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
            LayoutParams lpLeft = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpLeft.addRule(CENTER_VERTICAL);
            addView(leftRefreshView, lpLeft);
            leftRefreshView.setBackgroundResource(leftDrawableId);
        }
        if (rightRefreshView == null) {
            rightRefreshView = new ImageView(getContext());
            LayoutParams lpRight = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpRight.addRule(CENTER_VERTICAL);
            addView(rightRefreshView, lpRight);
            rightRefreshView.setBackgroundResource(rightDrawableId);
        }
        if (backHomeView == null) {
            backHomeView = new ImageView(getContext());
            LayoutParams lpHome = new LayoutParams(dip2px(30), dip2px(30));
            lpHome.addRule(CENTER_HORIZONTAL);
            addView(backHomeView, lpHome);
            backHomeView.setBackgroundResource(bottomDrawableId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                backHomeView.setElevation(10);
            }
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