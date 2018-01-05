package com.renny.simplebrowser.business.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 用途：输入键盘相关
 * 作者：Created by diaowj on 2016/08/03 14:26
 * 邮箱：diaowj@aixuedai.com
 */
public class KeyboardUtils {

    /**
     * 动态隐藏软键盘
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 动态隐藏软键盘
     */
    public static void hideSoftInput(Context context, EditText edit) {
        edit.clearFocus();
        if (context != null) {
            InputMethodManager inputManger = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        }
    }

    /**
     * 动态显示软键盘
     */
    public static void showSoftInput(final Context context, final EditText edit) {
        if (context == null) {
            return;
        }
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.postDelayed(new Runnable() {
            @Override
            public void run() {
                edit.requestFocus();
                InputMethodManager inputManger = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManger.showSoftInput(edit, 0);
            }
        }, 200);
    }

    /**
     * 切换键盘显示与否状态
     */
    public static void toggleSoftInput(Context context, EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 监听键盘显示隐藏
     * @param activity
     * @param rootView mRootLayout为xml布局文件中的顶层布局（根布局）
     * @param listener
     */
    public static void keyboardStateWatcher(Activity activity, View rootView, SoftKeyboardStateWatcher.SoftKeyboardStateListener listener) {
        new SoftKeyboardStateWatcher(rootView, activity).addSoftKeyboardStateListener(listener);
    }
}
