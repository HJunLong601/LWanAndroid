package com.hjl.core.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.hjl.commonlib.base.ResourceManager;
import com.hjl.commonlib.utils.DensityUtil;
import com.hjl.core.R;

/**
 * author: long
 * description 贝塞尔曲线测试 底边海浪效果
 * Date: 2020/8/10
 */
public class WaveRippleView extends View {

    private Paint mPaint;
    private Path mPath;

    private int mWidth;
    private int mHeight;

    // 默认高度
    private int WAVE_HEIGHT = DensityUtil.dp2px(20);
    private int WAVE_LENGTH = 0;

    private int currOffsetX;

    private ValueAnimator mXOffsetAnimator;

    public WaveRippleView(Context context) {
        super(context);

        init();
    }

    public WaveRippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveRippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(ResourceManager.getInstance().getColor(getContext(), R.color.common_base_theme_color));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(12);
        mPaint.setAntiAlias(true);
        mPath = new Path();
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;

        WAVE_HEIGHT = Math.min(WAVE_HEIGHT,mHeight);
        WAVE_LENGTH = mWidth;
        if (mXOffsetAnimator != null) {
            mXOffsetAnimator.cancel();
            mXOffsetAnimator = null;
        }
        startAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();
        mPath.moveTo(-WAVE_LENGTH + currOffsetX,Math.max(mHeight - 2*WAVE_HEIGHT,mHeight/2));
        for (int i = -WAVE_LENGTH;i < mWidth + WAVE_LENGTH;i = i + WAVE_LENGTH){
            mPath.rQuadTo(WAVE_LENGTH / 4, -WAVE_HEIGHT, WAVE_LENGTH / 2, 0);
            mPath.rQuadTo(WAVE_LENGTH / 4, WAVE_HEIGHT, WAVE_LENGTH / 2, 0);
        }

        // 闭合起来
        mPath.lineTo(mWidth,mHeight);
        mPath.lineTo(0,mHeight);
        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
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
}
