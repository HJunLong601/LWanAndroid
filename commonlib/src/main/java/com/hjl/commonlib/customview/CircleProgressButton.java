package com.hjl.commonlib.customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.hjl.commonlib.R;
import com.hjl.commonlib.utils.DensityUtil;
import com.hjl.commonlib.utils.LogUtils;

import java.util.Arrays;


/**
 * author: long
 * description please add a description here
 * Date: 2020/8/3
 */
public class CircleProgressButton extends View {


    private int mWidth;
    private int mHeight;

    int DEFAULT_WIDTH = DensityUtil.dp2px(45);
    int DEFAULT_HEIGHT = DensityUtil.dp2px(45);

    private Paint mPaint;
    private Bitmap mCenterBitmap;
    private int PADDING_SIZE = DensityUtil.dp2px(4);


    float mDownXInView;
    float mDownYInView;
    float mDownXInScreen;
    float mDownYInScreen;

    private boolean enableWelt = false;
    private float currentProgress = 0;
    private float maxProgress = 100;

    RectF circleRect;
    Rect mBitmapRect;
    Rect mDestRect;

    float screenWidth;
    float screenHeight;

    public CircleProgressButton(Context context) {
        super(context);
        init();
    }

    public CircleProgressButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgressButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mCenterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.view_icon_arrow_back_black);

        // 关闭硬件加速（view层面）
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        circleRect = new RectF();
        mBitmapRect = new Rect();
        mDestRect = new Rect();

        screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        }else if (widthMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(DEFAULT_WIDTH,heightSize);
        }else if (heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSize,DEFAULT_HEIGHT);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHeight = Math.min(w,h);
        mWidth = Math.min(w,h);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int radius = mWidth/2 - PADDING_SIZE;

        // 画背景
        mPaint.setColor(0xFFF5F7FA);
        mPaint.setStyle(Paint.Style.FILL);

        // 设置阴影
        mPaint.setShadowLayer(5,3,3,Color.GRAY);
        canvas.drawCircle(mWidth/2,mHeight/2,radius,mPaint);

        // 画进度条
        mPaint.setShadowLayer(0,0,0,0);
        mPaint.setColor(0xFF04B5FD);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DensityUtil.dp2px(2));
        circleRect.set(PADDING_SIZE,PADDING_SIZE,mWidth - PADDING_SIZE,mWidth - PADDING_SIZE);
        canvas.drawArc(circleRect,-90,getSweepAngle(),false,mPaint);

        // 画返回键
        if (mCenterBitmap != null){
            mPaint.setFilterBitmap(true);
            mBitmapRect.set(0,0,mCenterBitmap.getWidth(),mCenterBitmap.getHeight());
            mDestRect.set(PADDING_SIZE,PADDING_SIZE,mWidth - PADDING_SIZE,mHeight - PADDING_SIZE);
//        Rect mDestRect = new Rect(radius/5 + PADDING_SIZE,radius/5 + PADDING_SIZE,radius * 8/5,radius * 8/5);
            canvas.drawBitmap(mCenterBitmap,mBitmapRect,mDestRect,mPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownXInView = event.getX();
                mDownYInView = event.getY();
                mDownXInScreen = event.getRawX();
                mDownYInScreen = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getRawX() - mWidth;
                float y = event.getRawY() - mHeight;
                setX(Math.max(x,0));
                setY(Math.max(y,0));
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                float dx = Math.abs(event.getRawX() - mDownXInScreen);
                float dy = Math.abs(event.getRawY() - mDownYInScreen);

                // 点击事件
                if (dx < 10 && dy < 10) performClick();

                // 贴边
                doWelt();

                break;
        }

        return true;
    }

    private float getSweepAngle(){

        float angle = currentProgress / maxProgress * 360;

        if (angle == 360) return 0;

        return angle;
    }

    private void doWelt(){

        if (!enableWelt) return;

        boolean isLeft = getX() < screenWidth/2;

        LogUtils.i("isLeft:" + isLeft);


        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this,
                "x",
                getX(),
                isLeft ? DensityUtil.dp2px(10) : screenWidth - DensityUtil.dp2px(10) - mWidth
        );

        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(0);
        objectAnimator.start();


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        clearAnimation();
    }

    public void setCurrentProgress(float currentProgress) {
        this.currentProgress = currentProgress;
        invalidate();
    }

    public float getCurrentProgress() {
        return currentProgress;
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setEnableWelt(boolean enableWelt) {
        this.enableWelt = enableWelt;
    }
}
