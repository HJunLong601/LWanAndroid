package com.hjl.commonlib.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.hjl.commonlib.R;
import com.hjl.commonlib.base.BaseApplication;

/**
 * author: long
 * description 圆形进度条
 * Date: 2020/9/11
 */
public class ProgressWaveView extends View {

    private String TAG = "ProgressWaveView";

    int DEFAULT_WIDTH = 200;
    int DEFAULT_HEIGHT = 200;

    // 默认高度
    private int WAVE_HEIGHT = 15;
    private int WAVE_LENGTH = 0;

    private Paint mPaint;
    private Paint mTextPaint;
    private Path mCirclePath;
    private Path mWavePath;

    Rect textRect;

    private int mWidth;
    private int mHeight;

    private int currOffsetX;
    private ValueAnimator mXOffsetAnimator;


    public ProgressWaveView(Context context) {
        super(context);
        init();
    }

    public ProgressWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(waveColor);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);

        mCirclePath = new Path();
        mWavePath = new Path();
        textRect = new Rect();
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

        WAVE_HEIGHT = Math.min(WAVE_HEIGHT,mHeight);
        WAVE_LENGTH = (int) (mWidth * 1.0);
        startAnim();
    }

    private void startAnim(){
        mXOffsetAnimator = ValueAnimator.ofInt(0,WAVE_LENGTH);
        mXOffsetAnimator.setDuration(1500);
        mXOffsetAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        mXOffsetAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mXOffsetAnimator.setInterpolator(new LinearInterpolator());
        mXOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currOffsetX = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mXOffsetAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(waveColor);

        // 初始化波浪路径
        mWavePath.reset();
        mWavePath.moveTo(-WAVE_LENGTH + currOffsetX,mHeight * (1 - currentProgress));
//        Log.d(TAG, "currentProgress: " + currentProgress);
        for (int i = -WAVE_LENGTH;i < mWidth + WAVE_LENGTH;i = i + WAVE_LENGTH){
            mWavePath.rQuadTo(WAVE_LENGTH / 4, -WAVE_HEIGHT, WAVE_LENGTH / 2, 0);
            mWavePath.rQuadTo(WAVE_LENGTH / 4, WAVE_HEIGHT, WAVE_LENGTH / 2, 0);
        }
        // 闭合起来
        mWavePath.lineTo(mWidth,mHeight);
        mWavePath.lineTo(0,mHeight);
        mWavePath.close();

        // 裁剪绘制区域为圆形内 绘制波浪路径和蓝色字体
        mCirclePath.reset();
        mCirclePath.addCircle(mWidth/2,mHeight/2,Math.min(mWidth/2,mHeight/2) - 5, Path.Direction.CW);

        canvas.save();
        canvas.clipPath(mCirclePath, Region.Op.INTERSECT);
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(mWavePath,mPaint);

        drawText(canvas,textColor);
        canvas.restore();

        // 裁剪绘制区域为波浪区域 绘制波浪下白色文字
        canvas.save();
        canvas.clipPath(mWavePath, Region.Op.INTERSECT);
        drawText(canvas,Color.WHITE);
        canvas.restore();

        // 绘制外圈
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mCirclePath,mPaint);
    }

    private void drawText(Canvas canvas,int color) {
//        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
//        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        mTextPaint.setColor(color);
        mTextPaint.setTextSize(mWidth/2);
        mTextPaint.getTextBounds(centerText,0,1,textRect);
        canvas.drawText(centerText,mWidth/4,textRect.height() + (mHeight - textRect.height()) /2,mTextPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mXOffsetAnimator != null){
            mXOffsetAnimator.cancel();
            mXOffsetAnimator.removeAllUpdateListeners();
            mXOffsetAnimator.removeAllListeners();
            mXOffsetAnimator = null;
        }
        clearAnimation();
    }


    /*****************************************************************************
     **********************  外部方法 *********************************************
     *****************************************************************************/
    private String centerText = BaseApplication.getApplication().getString(R.string.loong);
    private int waveColor = Color.parseColor("#04B5FD");
    private int textColor = Color.parseColor("#04B5FD");
    private float currentProgress = 0.50F;


    /**
     * 设置中间文字
     * @param text
     */
    public void setCenterText(String text){
        if (text == null || text.length() > 1){
            Log.e(TAG, "setCenterText Error: text must not be null and limit length to 1 ");
            return;
        }
        centerText = text;
    }

    /**
     * 上半部分字体颜色
     * @param color
     */
    public void setTextColor(@ColorInt int color){
        textColor = color;
    }

    /**
     * 波浪颜色
     * @param color
     */
    public void setWaveColor(@ColorInt int color){
        waveColor = color;
    }

    /**
     * 百分比进度条
     * @param percent 0.00 - 1.00
     */
    public void setProgress(float percent){
        if (percent < 0.00){
            currentProgress = 0.00f;
        }else if (percent > 1.00){
            currentProgress = 1.00f;
        }else {
            currentProgress = percent;
        }
    }

    public void setWaveHeight(int waveHeight){
        WAVE_HEIGHT = waveHeight;
    }
}

