package com.example.sunanimation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
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

    private float mCurrentTime;
    private float mAngle;
    private final static String START_TIME = "06:00";
    private final static String END_TIME = "20:00";
    private final static String  TAG = "SunView";

    private Bitmap mBitmap;
    private float mPositionX;
    private float mPositionY;
    private final static float mIconRadius = 20;

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

    public void setTime(String time){
        mCurrentTime = transTime(time);
        int start = transTime(START_TIME);
        int end = transTime(END_TIME);
        mAngle = (mCurrentTime - start)/ (end - start) * 180;
        Log.d(TAG,"mCurrentTime:" + mCurrentTime + " start:" + start + " end:" + end + " mAngle:" + mAngle);
        startAnimation(0,mAngle,2000);
    }

    private void startAnimation(float startAngle, float currentAngle, int duration) {
        ValueAnimator sunAnimator = ValueAnimator.ofFloat(startAngle, currentAngle);
        sunAnimator.setDuration(duration);
        sunAnimator.setTarget(currentAngle);
        sunAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                //每次要绘制的圆弧角度
                mAngle = (float) animation.getAnimatedValue();
                invalidateView();
            }

        });
        sunAnimator.start();
    }

    private void invalidateView() {
        mPositionX = mWidth / 2 - (float) (mCricleRadius * Math.cos(mAngle * Math.PI / 180)) - mIconRadius;
        mPositionY =  mTop + mCricleRadius - (float) (mCricleRadius * Math.sin(mAngle * Math.PI / 180) + 15);
        Log.d(TAG,"mPositionX:" + mPositionX + " mPositionY" + mPositionY);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画圆
        drawCricle(canvas);
        canvas.save();
        //画文字
        drawText(canvas);
        //画太阳
        drawSun(canvas);
        //画太阳走过的圆弧
        drawOld(canvas);

        super.onDraw(canvas);
    }

    private void drawOld(Canvas canvas) {
        mPaint.setColor(mCurrentCricleColor);
        canvas.drawArc(mRectF,180,mAngle,false,mPaint);
    }

    private void drawSun(Canvas canvas) {
        canvas.drawBitmap(mBitmap,mPositionX,mPositionY,mPaint);
    }

    private void drawText(Canvas canvas) {
        mPaint.setColor(mFontColor);
        mPaint.setTextSize(mFontSize);
        canvas.drawText(START_TIME,mWidth/2 - mCricleRadius,mTop + mCricleRadius + 50,mPaint);
        canvas.drawText(END_TIME,mWidth/2 + mCricleRadius - 150,mTop + mCricleRadius + 50,mPaint);
    }

    private void drawCricle(Canvas canvas) {
        mRectF = new RectF(mWidth/2 - mCricleRadius, mTop, mWidth/2 + mCricleRadius, 2*mCricleRadius + mTop);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true); //防抖动
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
        mPositionX = mWidth/2 - mCricleRadius - mIconRadius;
        mPositionY = mCricleRadius + mTop - mIconRadius;
    }

    private void intView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SunAnimationView);
        mCircleColor = array.getColor(R.styleable.SunAnimationView_circle_color,
                getContext().getResources().getColor(R.color.blue));
        mCurrentCricleColor = array.getColor(R.styleable.SunAnimationView_current_color,
                getContext().getResources().getColor(R.color.red));
        mCricleRadius = array.getInteger(R.styleable.SunAnimationView_circle_radius,
                200);
        mFontSize = (int) array.getDimension(R.styleable.SunAnimationView_font_size,20);
        mFontColor = array.getColor(R.styleable.SunAnimationView_font_color,
                getContext().getResources().getColor(R.color.red));
        array.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //压缩图片
        BitmapFactory.Options newOpts =  new  BitmapFactory.Options();
        newOpts.inSampleSize = 3;
        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sun,newOpts);
    }

    //将时间转换为分钟
    private int transTime(String time){
        int value = 0;
        if (time == null){
            return value;
        }
        String[] s = time.split(":");
        int hour = Integer.parseInt(s[0]);
        int minute = Integer.parseInt(s[1]);
        value = hour * 60 + minute;
        return value;
    }
}
