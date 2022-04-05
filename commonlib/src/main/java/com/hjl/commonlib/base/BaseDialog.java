package com.hjl.commonlib.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hjl.commonlib.R;
import com.hjl.commonlib.utils.DensityUtil;
import com.hjl.commonlib.utils.LogUtils;


/**
 * author: long
 * description please add a description here
 * Date: 2020/7/14
 */
public abstract class BaseDialog extends Dialog {

    protected Context mContext;
    protected String TAG = getClass().getSimpleName();

    protected boolean isInterceptBackEvent = false;

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.SimpleTransparentDialog);
        mContext = context;
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        initView();
        initData();


    }

    protected void showWrapContentCenter(Window mWindow){
        showWrapContent(mWindow,Gravity.CENTER);
    }
    protected void showWrapContent(Window mWindow ,int gravity) {

        if (mWindow == null){
            LogUtils.e(TAG,"Window is NULL");
            return;
        }

        WindowManager.LayoutParams layoutParams = mWindow.getAttributes();
        layoutParams.gravity = gravity;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindow.getDecorView().setPadding(0, 0, 0, 0);

        mWindow.setAttributes(layoutParams);
    }
    protected void showFullScreenCenter(Window mWindow){
        showFullScreen(mWindow,Gravity.CENTER);
    }

    protected void showFullScreen(Window mWindow,int gravity) {

        if (mWindow == null){
            LogUtils.e(TAG,"Window is NULL");
            return;
        }

        WindowManager.LayoutParams layoutParams = mWindow.getAttributes();
        layoutParams.gravity = gravity;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWindow.getDecorView().setPadding(0, 0, 0, 0);

        mWindow.setAttributes(layoutParams);
    }

    protected void setShowSize(Window mWindow,int width,int height){
        setShowSize(mWindow,width, height,Gravity.CENTER);
    }

    protected void setShowSize(Window mWindow,int width,int height,int gravity){
        if (mWindow == null){
            LogUtils.e(TAG,"Window is NULL");
            return;
        }

        WindowManager.LayoutParams layoutParams = mWindow.getAttributes();
        layoutParams.gravity = gravity;
        layoutParams.width = width;
        layoutParams.height = height;
        mWindow.getDecorView().setPadding(0, 0, 0, 0);

        mWindow.setAttributes(layoutParams);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected void initView(){

    }

    protected void initData(){
        // 默认不可点击取消
        setCancelable(false);
    }


    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && isInterceptBackEvent){
            return true;
        }else if (keyCode == KeyEvent.KEYCODE_BACK){
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean isInterceptBackEvent() {
        return isInterceptBackEvent;
    }

    public void setInterceptBackEvent(boolean isInterceptBackEvent) {
        this.isInterceptBackEvent = isInterceptBackEvent;
    }
}
