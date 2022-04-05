package com.hjl.core.customview;

import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;


/**
 * created by long on 2019/11/6
 */
public class NoConflictViewPager extends ViewPager {

    private String TAG = "NoConflictViewPager";
    private float mDownPosX;
    private float mDownPosY;
    private float mTouchSlop;
    boolean isIntercept = false;

    private int mLastX;
    private int mLastY;

    public NoConflictViewPager(@NonNull Context context) {
        super(context);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public NoConflictViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//        boolean intercepted = false;
//        int x = (int) ev.getX();
//        int y = (int) ev.getY();
//
//        switch (ev.getAction()){
//
//            case MotionEvent.ACTION_DOWN:
//                intercepted = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                int deltaX = x - mLastX;
//                int deltaY = y - mLastY;
//                int index= getCurrentItem();
//                if (index == 0 && Math.abs(deltaX) > Math.abs(deltaY)){
//                    intercepted = true;
//                }else {
//                    intercepted = false;
//                }
//
//
//                break;
//
//            case MotionEvent.ACTION_UP:
//                intercepted = false;
//                break;
//
//        }
//
//
//        mLastX = x;
//        mLastY = y;
//
//        return intercepted;
//    }

        @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();

        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownPosX = x;
                mDownPosY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);
                int i = getCurrentItem();
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (i == 0 && deltaX > deltaY) { // 拦截左右滑动
                    return true;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        Log.d(TAG, "onTouchEvent: ");
        
        return super.onTouchEvent(ev);
    }
}
