package com.exblr.rangeseekbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liamwang on 2017/3/3.
 */

public class RangeSeekBar extends View {

    private int mThumbSize;
    private float[] mScales;
    private int mScaleTextSize;
    private int mScaleTextPaddingTop;
    private int mScaleTextColor;
    private int mLineColorNormal, mLineColorSelection;
    private float mLimitMin, mLimitMax;
    private float mMin, mMax;

    private float lastX;
    private int minX, maxX, startX, endX, lineY;
    private boolean isMinPressed, isMaxPressed;

    private Rect startRect, endRect;
    private Paint linePaint, scaleTextPaint;
    private Bitmap thumbBitmap;

    private OnScaleTextFormat mOnScaleTextFormat;
    private OnRangeChanged mOnRangeChanged;

    public RangeSeekBar(Context context) {
        this(context, null);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mThumbSize = dpToPx(context, 40);
        mScaleTextSize = spToPx(context, 13);
        mScaleTextColor = 0xFF777777;
        mScaleTextPaddingTop = dpToPx(context, 10);
        mLineColorNormal = 0xFFCCCCCC;
        mLineColorSelection = 0xFFFC511C;

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(10);
        scaleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scaleTextPaint.setColor(mScaleTextColor);
        scaleTextPaint.setTextSize(mScaleTextSize);

        thumbBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_seek_thumb_normal);
    }

    public void setOnScaleTextFormat(OnScaleTextFormat onScaleTextFormat) {
        mOnScaleTextFormat = onScaleTextFormat;
    }

    public void setOnRangeChanged(OnRangeChanged onRangeChanged) {
        mOnRangeChanged = onRangeChanged;
    }

    public void setThumbSize(int thumbSize) {
        mThumbSize = thumbSize;
    }

    public void setScales(float[] scales) {
        mScales = scales;
        mLimitMin = scales[0];
        mLimitMax = scales[scales.length - 1];
        mMin = mLimitMin;
        mMax = mLimitMax;
    }

    public void setScaleTextPaddingTop(int scaleTextPaddingTop) {
        mScaleTextPaddingTop = scaleTextPaddingTop;
    }

    public void setScaleTextSize(int scaleTextSize) {
        mScaleTextSize = scaleTextSize;
    }

    public void setScaleTextColor(int scaleTextColor) {
        mScaleTextColor = scaleTextColor;
    }

    public void setLineColorNormal(int lineColorNormal) {
        mLineColorNormal = lineColorNormal;
    }

    public void setLineColorSelection(int lineColorSelection) {
        mLineColorSelection = lineColorSelection;
    }

    public float[] getScales() {
        return mScales;
    }

    public float getMax() {
        return mMax;
    }

    public float getMin() {
        return mMin;
    }

    public float getLimitMax() {
        return mLimitMax;
    }

    public float getLimitMin() {
        return mLimitMin;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mScales == null || mScales.length < 2) {
            throw new IllegalArgumentException("需要先调用 setScales 方法设置刻度，其数量不能少于2");
        }

        startX = getPaddingLeft() + mThumbSize / 2;
        endX = getWidth() - getPaddingRight() - mThumbSize / 2;
        minX = startX;
        maxX = endX;
        lineY = getPaddingTop() + mThumbSize / 2;

        startRect = new Rect(startX - mThumbSize / 2, 0, startX + mThumbSize / 2, mThumbSize);
        endRect = new Rect(endX - mThumbSize / 2, 0, endX + mThumbSize / 2, mThumbSize);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST) {
            int viewHeight = getPaddingTop() + mThumbSize + mScaleTextPaddingTop + mScaleTextSize + getPaddingBottom();
            heightSize = Math.min(viewHeight, heightSize);
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw seek bar.
        linePaint.setColor(mLineColorNormal);
        canvas.drawLine(minX, lineY, maxX, lineY, linePaint);
        linePaint.setColor(mLineColorSelection);
        canvas.drawLine(startX, lineY, endX, lineY, linePaint);

        // Draw thumbs.
        startRect.set(startX - mThumbSize / 2, 0, startX + mThumbSize / 2, mThumbSize);
        endRect.set(endX - mThumbSize / 2, 0, endX + mThumbSize / 2, mThumbSize);
        canvas.drawBitmap(thumbBitmap, null, startRect, null);
        canvas.drawBitmap(thumbBitmap, null, endRect, null);

        // Draw scale texts.
        for (int i = 0; i < mScales.length; i++) {
            String text = mOnScaleTextFormat == null ? mScales[i] + "" : mOnScaleTextFormat.format(mScales[i], i);
            float x = minX + (maxX - minX) * (mScales[i] - mLimitMin) / (mLimitMax - mLimitMin) - scaleTextPaint.measureText(text) / 2;
            float y = startRect.bottom + mScaleTextPaddingTop;
            canvas.drawText(text, x, y, scaleTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (x > startRect.left && x < startRect.right && y > startRect.top && y < startRect.bottom && !isMaxPressed) {
                    isMinPressed = true;
                }
                if (x > endRect.left && x < endRect.right && y > endRect.top && y < endRect.bottom && !isMinPressed) {
                    isMaxPressed = true;
                }
                lastX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                float offsetX = event.getX() - lastX;
                if (isMinPressed) {
                    startX += offsetX;
                    if (startX < minX) {
                        startX = minX;
                    }
                    if (startX > maxX) {
                        startX = maxX;
                    }
                    mMin = mLimitMin + (mLimitMax - mLimitMin) * (startX - minX) / (maxX - minX);
                    invalidate();
                }
                if (isMaxPressed) {
                    endX += offsetX;
                    if (endX > maxX) {
                        endX = maxX;
                    }
                    if (endX < minX) {
                        endX = minX;
                    }
                    mMax = mLimitMin + (mLimitMax - mLimitMin) * (endX - minX) / (maxX - minX);
                    invalidate();
                }
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isMinPressed = false;
                isMaxPressed = false;
        }
        if (isMaxPressed || isMinPressed) {
            getParent().requestDisallowInterceptTouchEvent(true);
            if (mOnRangeChanged != null) {
                mOnRangeChanged.onChange(Math.min(mMin, mMax), Math.max(mMin, mMax));
            }
        }
        return super.onTouchEvent(event);
    }

    public static int dpToPx(Context context, int dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    public static int spToPx(Context context, int sp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    public interface OnScaleTextFormat {
        String format(float value, int index);
    }

    public interface OnRangeChanged {
        void onChange(float min, float max);
    }
}
