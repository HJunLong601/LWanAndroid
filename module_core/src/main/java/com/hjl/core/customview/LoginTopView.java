package com.hjl.core.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hjl.commonlib.base.ResourceManager;
import com.hjl.commonlib.utils.DensityUtil;
import com.hjl.core.R;


/**
 * author: long
 * description please add a description here
 * Date: 2020/8/12
 */
public class LoginTopView extends View {

    private Paint mPaint;
    private Path mPath;

    private int mWidth;
    private int mHeight;

    private int CONTROL_HEIGHT = DensityUtil.dp2px(100);

    public LoginTopView(Context context) {
        super(context);

        init();
    }

    public LoginTopView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public LoginTopView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;

        CONTROL_HEIGHT = Math.min(mHeight,CONTROL_HEIGHT);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();
        mPath.moveTo(0,mHeight);

        mPath.quadTo(mWidth/2,mHeight-CONTROL_HEIGHT,mWidth,mHeight);

        mPath.lineTo(mWidth,0);
        mPath.lineTo(0,0);

        canvas.drawPath(mPath,mPaint);

    }

}
