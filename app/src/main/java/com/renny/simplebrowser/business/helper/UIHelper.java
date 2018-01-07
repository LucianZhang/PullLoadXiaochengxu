package com.renny.simplebrowser.business.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.renny.simplebrowser.App;

import java.util.List;

/**
 * @author Created by xinzai on 2015/5/14.
 */
public class UIHelper {
    /**
     * TODO(在使用时ListView item必须不包含RelativeLayout(layout中必须重写measure()))
     * <p>
     * TODO(谨慎使用)
     **/
    @Deprecated
    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
    }

    public static int getViewMeasuredHeight(TextView tv) {
        tv.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return tv.getMeasuredHeight();

    }

    /**
     * dp转px
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * dp转px
     */
    public static int dip2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static boolean hasEmpty(List<TextView> edits) {
        for (TextView editText : edits) {
            if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasEmpty(TextView... edits) {
        for (TextView editText : edits) {
            if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasEmpty(ImageView[] edits) {
        for (ImageView imageView : edits) {
            if (TextUtils.isEmpty(imageView.getTag().toString().trim())) {
                return true;
            }
        }
        return false;
    }


    public static void setRightDrawable(TextView textView, int draw) {
        Drawable drawable = UIHelper.getDrawable(draw);
        try {
            assert drawable != null;
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, null, drawable, null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void setTopDrawable(TextView textView, int draw) {
        Drawable drawable = UIHelper.getDrawable(draw);
        try {
            assert drawable != null;
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, drawable, null, null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void setLeftDrawable(TextView textView, int draw) {
        Drawable drawable = UIHelper.getDrawable(draw);
        try {
            assert drawable != null;
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void setLeftDrawable(TextView textView, Drawable drawable) {
        try {
            assert drawable != null;
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /**
     * 资源ID获取String
     */
    public static String getString(int stringId) {
        Context context = App.getContext();
        if (context != null) {
            return context.getString(stringId);
        }
        return " ";
    }

    public static String getString(int stringId, Object... formatArgs) {
        Context context = App.getContext();
        if (context != null) {
            return context.getString(stringId, formatArgs);
        }
        return "";
    }

    /**
     * 获取尺寸
     */
    public static int getDimension(@DimenRes int dimenRes) {
        return (int) App.getContext().getResources().getDimension(dimenRes);
    }

    /**
     * 获取颜色
     */
    public static int getColor(@ColorRes int color) {
        return ContextCompat.getColor(App.getContext(), color);
    }

    /**
     * 字符串转16进制整数
     */
    public static int getColor(String color) {
        if (TextUtils.isEmpty(color) || !Validator.checkColor(color)) {
            return 0;
        }
        return Color.parseColor(color);
    }

    /**
     * 字符串转16进制整数,带默认值
     */
    public static int getColor(String color, String defaultColor) {
        if (TextUtils.isEmpty(color) || !Validator.checkColor(color)) {
            return getColor(defaultColor);
        }
        return Color.parseColor(color);
    }

    public static int getColor(String color, @ColorRes int defaultColor) {
        if (TextUtils.isEmpty(color) || !Validator.checkColor(color)) {
            return getColor(defaultColor);
        }
        return Color.parseColor(color);
    }


    /**
     * 获取Drawable
     */
    public static Drawable getDrawable(int drawable) {
        return ContextCompat.getDrawable(App.getContext(), drawable);
    }

    public static View inflaterLayout(Context context, @LayoutRes int layoutRes) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(layoutRes, null);
    }

    /**
     * HTML颜色
     */
    public static String setHtmlColor(String color, String content) {
        if (TextUtils.isEmpty(color)) return content;
        return String.format(Htmls.color, color, content);
    }





    /**
     * 圆角Drawable
     *
     * @param radius 圆角
     * @param color  填充颜色
     */
    public static GradientDrawable getShapeDrawable(int radius, @ColorInt int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(radius);
        return gd;
    }


}
