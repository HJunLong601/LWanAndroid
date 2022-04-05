package com.hjl.commonlib.utils;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hjl.commonlib.base.BaseApplication;

import java.lang.reflect.Field;


public class ToastUtil {

    private static Toast mToast = null;

    public static void show(Context context, String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        if (context != null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            hook(mToast);
            mToast.setText(text);
            //mToast.setGravity(Gravity.BOTTOM, 0, 0);
            mToast.show();
        }
    }

    public static void show(Context context, int resId) {
        if (context != null) {
            context = context.getApplicationContext();
            show(context, context.getString(resId));
        }
    }

    public static void show(String msg) {
        BaseApplication appContext = BaseApplication.getApplication();
        BaseApplication.runOnUIThread(() -> {
            Toast toast = Toast.makeText(appContext, msg, Toast.LENGTH_SHORT);
            hook(toast);
            toast.show();
        });
    }

    /**
     * 此方法会立即显示吐司内容
     */
    public static void showSingleToast(String msg) {
        BaseApplication appContext = BaseApplication.getApplication();
        if (mToast == null) {
            mToast = Toast.makeText(appContext, msg, Toast.LENGTH_SHORT);
            hook(mToast);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    private static Field sField_TN;
    private static Field sField_TN_Handler;

    static {
        try {
            sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (Exception e) {
        }
    }

    private static void hook(Toast toast) {


        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.N_MR1) return;

        try {
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWrapper(preHandler));
        } catch (Exception e) {
        }
    }



    public static class SafelyHandlerWrapper extends Handler {
        private Handler impl;

        public SafelyHandlerWrapper(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
//                Log.e("ward","catch:" + e.toString());
            }
        }

        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);//需要委托给原Handler执行
        }
    }

}
