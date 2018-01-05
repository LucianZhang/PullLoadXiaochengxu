package com.renny.simplebrowser.business.helper;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.LinkedList;
import java.util.List;

/**
 * Use: 软键盘开启与关闭状态的监听
 *
 * @author: Created by diaowj on 2017/12/12 14:04.
 * E-mail：317907323@qq.com
 */
public class SoftKeyboardStateWatcher implements ViewTreeObserver.OnGlobalLayoutListener {

    public interface SoftKeyboardStateListener {
        void onSoftKeyboardOpened(int keyboardHeightInPx);

        void onSoftKeyboardClosed();
    }

    private final List<SoftKeyboardStateListener> listeners = new LinkedList<SoftKeyboardStateListener>();
    private final View activityRootView;
    private int lastSoftKeyboardHeightInPx;
    private boolean isSoftKeyboardOpened;
    private Context mContext;

    //使用时用这个构造方法
    public SoftKeyboardStateWatcher(View activityRootView, Context context) {
        this(activityRootView, false);
        this.mContext = context;
    }

    private SoftKeyboardStateWatcher(View activityRootView) {
        this(activityRootView, false);
    }

    private SoftKeyboardStateWatcher(View activityRootView, boolean isSoftKeyboardOpened) {
        this.activityRootView = activityRootView;
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
       /*
       如果是这种方式的话，软键盘必须设置为android:windowSoftInputMode=”adjustResize” 才有效
       int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
        if (!isSoftKeyboardOpened && heightDiff > dpToPx(NewsApplication.getContext(), 200)) { // if more than 200 dp, it's probably a keyboard...
            isSoftKeyboardOpened = true;
            notifyOnSoftKeyboardOpened(heightDiff);
        }else if(isSoftKeyboardOpened && heightDiff < dpToPx(NewsApplication.getContext(), 200)){
            isSoftKeyboardOpened = false;
            notifyOnSoftKeyboardClosed();
        }*/
        //下面这种方式则对软键盘没有设置要求
        final Rect r = new Rect();
        //r will be populated with the coordinates of your view that area still visible.
        activityRootView.getWindowVisibleDisplayFrame(r);
        final int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
        if (!isSoftKeyboardOpened && heightDiff > dpToPx(mContext, 200)) { // if more than 100 pixels, its probably a keyboard...
            isSoftKeyboardOpened = true;
            notifyOnSoftKeyboardOpened(heightDiff);
        } else if (isSoftKeyboardOpened && heightDiff < dpToPx(mContext, 200)) {
            isSoftKeyboardOpened = false;
            notifyOnSoftKeyboardClosed();
        }
    }

    public void setIsSoftKeyboardOpened(boolean isSoftKeyboardOpened) {
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
    }

    public boolean isSoftKeyboardOpened() {
        return isSoftKeyboardOpened;
    }

    /**
     * Default value is zero {@code 0}.
     *
     * @return last saved keyboard height in px
     */
    public int getLastSoftKeyboardHeightInPx() {
        return lastSoftKeyboardHeightInPx;
    }

    public void addSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        listeners.add(listener);
    }

    public void removeSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        listeners.remove(listener);
    }

    private void notifyOnSoftKeyboardOpened(int keyboardHeightInPx) {
        this.lastSoftKeyboardHeightInPx = keyboardHeightInPx;
        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardOpened(keyboardHeightInPx);
            }
        }
    }

    private void notifyOnSoftKeyboardClosed() {
        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardClosed();
            }
        }
    }

    private static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }
}
