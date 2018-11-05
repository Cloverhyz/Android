package com.example.kangningj.myapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.kangningj.myapplication.R;
import com.example.kangningj.myapplication.utils.PixelUtil;

/**
 * TODO: document your custom view class.
 */
public class RainbowBar extends View {

    //progress bar color
    int barColor = Color.parseColor("#1E88E5");
    //every bar segment width
    int wSpace = PixelUtil.dip2px(getContext(),80);
    //every bar segment height
    int hSpace = PixelUtil.dip2px(getContext(),4);
    //space among bars
    int space = PixelUtil.dip2px(getContext(),10);
    Paint mPaint;

    float startX = 0;
    float delta = 10f;

    public RainbowBar(Context context) {
        super(context, null);
    }

    public RainbowBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RainbowBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.rainbowbar, 0, 0);
        wSpace = t.getDimensionPixelSize(R.styleable.rainbowbar_rainbowbar_hspace, wSpace);
        hSpace = t.getDimensionPixelOffset(R.styleable.rainbowbar_rainbowbar_vspace, hSpace);
        barColor = t.getColor(R.styleable.rainbowbar_rainbowbar_color, barColor);
        t.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(barColor);
        mPaint.setStrokeWidth(hSpace);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float sw = this.getMeasuredWidth();
        if (startX >= sw + (wSpace + space) - (sw % (wSpace + space))) {
            startX = 0;
        } else {
            startX += 5;
        }
        float start = startX;
        while (start < sw) {
            canvas.drawLine(start, 5, start + wSpace, 5, mPaint);
            start += (wSpace + space);
        }
        start = startX - space - wSpace;
        while (start >= -wSpace) {
            barColor = Color.parseColor("#FF0000");
            mPaint.setColor(barColor);
            canvas.drawLine(start, 5, start + wSpace, 5, mPaint);
            start -= (wSpace + space);
        }
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    public static int getDefaultSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }
}
