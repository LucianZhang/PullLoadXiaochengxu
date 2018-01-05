package com.renny.simplebrowser.widget.pullextend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Renny on 2018/1/2.
 */

public class ExpendPoint extends View {

    float percent;
    float maxRadius = 15;
    float maxDist = 60;
    Paint mPaint;

    public ExpendPoint(Context context) {
        this(context, null);
    }

    public ExpendPoint(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpendPoint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
    }


    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    public void setMaxDist(float maxDist) {
        this.maxDist = maxDist;
    }

    public void setPercent(float percent) {
        if (percent != this.percent) {
            this.percent = percent;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        maxRadius = getHeight() / 2f;
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        if (percent <= 0.5f) {
            mPaint.setAlpha(255);
            float radius = percent * 2 * maxRadius;
            canvas.drawCircle(centerX, centerY, radius, mPaint);
        } else {
            float afterPercent = (percent - 0.5f) / 0.5f;
            float radius = maxRadius - maxRadius / 2 * afterPercent;

            canvas.drawCircle(centerX, centerY, radius, mPaint);
            canvas.drawCircle(centerX - afterPercent * maxDist, centerY, maxRadius / 2, mPaint);
            canvas.drawCircle(centerX + afterPercent * maxDist, centerY, maxRadius / 2, mPaint);
        }
    }
}
