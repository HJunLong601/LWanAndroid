package com.hjl.commonlib.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class NoScrollViewPager extends ViewPager {

    private boolean noScroll = false;
    private String TAG = "NoScrollViewPager";

    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs, boolean noScroll) {
        super(context, attrs);
        this.noScroll = noScroll;
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        Log.d(TAG, "onTouchEvent: " + arg0.getAction());
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {


        Log.d(TAG, "onInterceptTouchEvent: " + arg0.getAction());
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }
}
