package com.example.sunanimation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by 阿正 on 2017/9/14 0014.
 */

public class SunView extends View {
    private int mCircleColor;
    private int mFontColor;
    private int mFontSize;
    private int mCricleRadius;
    private int mCurrentCricleColor;

    private Paint mPaint;
    private RectF mRectF;
    private WindowManager wm;
    private int mWidth;
    private int mTop;


    public SunView(Context context) {
        this(context,null);
    }

    public SunView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SunView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intView(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCricle(canvas);
    }

    private void drawCricle(Canvas canvas) {
        mRectF = new RectF(mWidth/2 - mCricleRadius, mTop, mWidth/2 + mCricleRadius, 2*mCricleRadius + mTop);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        mPaint.setColor(mCircleColor);
        canvas.drawArc(mRectF,180,180,true,mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, mWidth/2 - mCricleRadius, mTop, mWidth/2 + mCricleRadius, 2*mCricleRadius + mTop);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mWidth = wm.getDefaultDisplay().getWidth();
        mTop = wm.getDefaultDisplay().getHeight() / 6;
    }

    private void intView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SunAnimationView);
        mCircleColor = array.getColor(R.styleable.SunAnimationView_circle_color,
                getContext().getResources().getColor(R.color.blue));
        mCurrentCricleColor = array.getColor(R.styleable.SunAnimationView_current_color,
                getContext().getResources().getColor(R.color.yellow));
        mCricleRadius = array.getInteger(R.styleable.SunAnimationView_circle_radius,
                200);
        mFontSize = (int) array.getDimension(R.styleable.SunAnimationView_font_size,20);
        mFontColor = array.getColor(R.styleable.SunAnimationView_font_color,
                getContext().getResources().getColor(R.color.red));
        array.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }
}
