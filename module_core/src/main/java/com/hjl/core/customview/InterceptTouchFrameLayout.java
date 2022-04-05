package com.hjl.core.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

/**
 * Author : long
 * Description :
 * Date : 2020/8/1
 */
public class InterceptTouchFrameLayout extends FrameLayout {

    private float mDownPosX;
    private float mDownPosY;

    private boolean isIntercept = false;



    private OnInterceptStateChangeListener listener;

    public void setViewPager2(ViewPager2 viewPager2) {
        this.viewPager2 = viewPager2;
    }

    private ViewPager2 viewPager2;

    public InterceptTouchFrameLayout(@NonNull Context context) {
        super(context);
    }

    public InterceptTouchFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptTouchFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int x = (int) ev.getX();
        int y = (int) ev.getY();
        boolean intercepted = false;

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownPosX = x;
                mDownPosY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);

                // 拦截左右滑动
                if (viewPager2.getCurrentItem() == 0 && deltaX > deltaY && mDownPosX - x <  0) {
                    listener.onInterceptStateChange(true);
                }else {
                    listener.onInterceptStateChange(false);
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    public void setListener(OnInterceptStateChangeListener listener) {
        this.listener = listener;
    }

    public interface OnInterceptStateChangeListener{
        void onInterceptStateChange(boolean isIntercept);
    }

}
