package com.example.viswanathankp.colorslider;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by viswanathan.kp on 10/12/16.
 */

public class ColorSlider extends View {

    private Bitmap bitmap;

    private static final boolean ORIENTATION_HORIZONTAL = true;
    private static final boolean ORIENTATION_VERTICAL = false;

    private static final boolean ORIENTATION_DEFAULT = ORIENTATION_HORIZONTAL;

    private int mBarThickness;

    private int mBarLength;
    private int mPreferredBarLength;

    private int mBarPointerRadius;

    private int mBarPointerHaloRadius;

    private int mBarPointerPosition;

    private Paint mBarPaint;

    private Paint mBarPointerPaint;

    private Paint mBarPointerHaloPaint;

    private static final int[] COLORS =new int[] { 0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF,
            0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF};

    private RectF mBarRect = new RectF();

    private boolean mIsPointerMoving;

    private int mColor;

    private boolean mOrientation;

    private OnColorChangedListener onColorChangedListener;

    public interface OnColorChangedListener{
        void OnColorChanged(int color);
    }

    public void setOnColorChangedListener(OnColorChangedListener listener){
        onColorChangedListener = listener;
    }

    public OnColorChangedListener getOnColorChangedListener(){
        return this.onColorChangedListener;
    }

    public ColorSlider(Context context) {
        super(context);
        init(null,0);
    }

    public ColorSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ColorSlider(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.setDrawingCacheEnabled(true);
        final TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.ColorBars, defStyle, 0);
        final Resources b = getContext().getResources();

        mBarThickness = a.getDimensionPixelSize(
                R.styleable.ColorBars_bar_thickness,
                b.getDimensionPixelSize(R.dimen.bar_thickness));
        mBarLength = a.getDimensionPixelSize(R.styleable.ColorBars_bar_length,
                b.getDimensionPixelSize(R.dimen.bar_length));
        mPreferredBarLength = mBarLength;
        mBarPointerRadius = a.getDimensionPixelSize(
                R.styleable.ColorBars_bar_pointer_radius,
                b.getDimensionPixelSize(R.dimen.bar_pointer_radius));
        mBarPointerHaloRadius = a.getDimensionPixelSize(
                R.styleable.ColorBars_bar_pointer_halo_radius,
                b.getDimensionPixelSize(R.dimen.bar_pointer_halo_radius));
        mOrientation = a.getBoolean(
                R.styleable.ColorBars_bar_orientation_horizontal, ORIENTATION_DEFAULT);

        a.recycle();

        mBarPaint = new Paint();
        mBarPaint.setStyle(Paint.Style.FILL);

        mBarPointerPosition = mBarLength + mBarPointerHaloRadius;

        mBarPointerHaloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarPointerHaloPaint.setColor(Color.GRAY);

        mBarPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarPointerPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int intrinsicSize = mPreferredBarLength
                + (mBarPointerHaloRadius * 2);

        // Used to decide orientation
        int measureSpec;
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            measureSpec = widthMeasureSpec;
        }
        else {
            measureSpec = heightMeasureSpec;
        }
        int lengthMode = View.MeasureSpec.getMode(measureSpec);
        int lengthSize = View.MeasureSpec.getSize(measureSpec);

        int length;
        if (lengthMode == View.MeasureSpec.EXACTLY) {
            length = lengthSize;
        }
        else if (lengthMode == View.MeasureSpec.AT_MOST) {
            length = Math.min(intrinsicSize, lengthSize);
        }
        else {
            length = intrinsicSize;
        }

        int barPointerHaloRadiusx2 = mBarPointerHaloRadius * 2;
        mBarLength = length - barPointerHaloRadiusx2;
        if(mOrientation == ORIENTATION_VERTICAL) {
            setMeasuredDimension(barPointerHaloRadiusx2,
                    (mBarLength + barPointerHaloRadiusx2));
        }
        else {
            setMeasuredDimension((mBarLength + barPointerHaloRadiusx2),
                    barPointerHaloRadiusx2);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // creates view based on orientation
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            mBarLength = w - (mBarPointerHaloRadius * 2);
            mBarRect.set(mBarPointerHaloRadius,
                    (mBarPointerHaloRadius - (mBarThickness / 2)),
                    (mBarLength + (mBarPointerHaloRadius)),
                    (mBarPointerHaloRadius + (mBarThickness / 2)));
        }
        else {
            mBarLength = h - (mBarPointerHaloRadius * 2);
            mBarRect.set((mBarPointerHaloRadius - (mBarThickness / 2)),
                    mBarPointerHaloRadius,
                    (mBarPointerHaloRadius + (mBarThickness / 2)),
                    (mBarLength + (mBarPointerHaloRadius)));
        }

        LinearGradient test = new LinearGradient(0.f, 0.f, w, h,
                COLORS,
                null, Shader.TileMode.CLAMP);

        mBarPaint.setShader(test);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw the bar.
        canvas.drawRect(mBarRect, mBarPaint);

        // calculate the center of the pointer.
        int cX, cY;
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            cX = mBarPointerPosition;
            cY = mBarPointerHaloRadius;
        }
        else {
            cX = mBarPointerHaloRadius;
            cY = mBarPointerPosition;
        }

        // draws the pointer halo.
        canvas.drawCircle(cX, cY, mBarPointerHaloRadius, mBarPointerHaloPaint);
        // draws the pointer.
        canvas.drawCircle(cX, cY, mBarPointerRadius, mBarPointerPaint);

        setPointerCenterColor(cX,cY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);

        float dimen;
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            dimen = event.getX();
        }
        else {
            dimen = event.getY();
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsPointerMoving = true;
                // Check whether the user pressed on (or near) the pointer
                if (dimen >= (mBarPointerHaloRadius)
                        && dimen <= (mBarPointerHaloRadius + mBarLength)) {
                    mBarPointerPosition = Math.round(dimen);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsPointerMoving) {
                    // Move the the pointer on the bar.
                    if (dimen >= mBarPointerHaloRadius
                            && dimen <= (mBarPointerHaloRadius + mBarLength)) {
                        mBarPointerPosition = Math.round(dimen);
                        invalidate();
                    } else if (dimen < mBarPointerHaloRadius) {
                        mBarPointerPosition = mBarPointerHaloRadius;
                        invalidate();
                    } else if (dimen > (mBarPointerHaloRadius + mBarLength)) {
                        mBarPointerPosition = mBarPointerHaloRadius + mBarLength;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsPointerMoving = false;
                break;
        }
        return true;
    }

    private void setPointerCenterColor(int x,int y){
        bitmap = this.getDrawingCache(true);
        mColor = bitmap.getPixel(x,y);
        mBarPointerPaint.setColor(mColor);

        if(onColorChangedListener!=null){
            onColorChangedListener.OnColorChanged(getColor());
        }
    }

    public int getColor() {
        return mColor;
    }
}
