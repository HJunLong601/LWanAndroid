package com.hjl.commonlib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjl.commonlib.R;
import com.hjl.commonlib.base.BaseApplication;

import java.lang.reflect.Method;
import java.util.Arrays;


/**
 * author: long
 * description please add a description here
 * Date: 2021/3/11
 */
public class LoggerManager {

    private Context mContext;
    private WindowManager.LayoutParams wmParams;
    private static WindowManager wManager;
    private static LoggerManager instance;
    private boolean hasPermission = true;

    private View mView;
    private View mLineView;
    private TextView mLogTv;

    private TextView mTitleTv;
    private TextView mTipTv;
    private ImageView mTitleIv;
    private ImageView mCloseIv;
    private ScrollView mScrollView;
    private LinearLayout mTitleLl;
    private LinearLayout mBottomLl;


    private static String TAG = "ward";

    private int REQUEST_OVERLAY = 0x0001;

    private int MIN_WIDTH_SIZE;
    private int MIN_HEIGHT_SIZE;


    public static synchronized LoggerManager getInstance(Context context){
        if (instance == null) instance = new LoggerManager(context);
        return instance;
    }

    boolean shouldShowTip = false;

    private LoggerManager(Context context){
        mContext = context;

        // tip 只在首次展示
        shouldShowTip = getTipTag() == 0;

        MIN_WIDTH_SIZE = dip2px(200);
        MIN_HEIGHT_SIZE = dip2px(230);
        initWindow();
        initView();
//        Log.e(TAG, "PermissionLoggerManager,show tip:" + shouldShowTip );
    }


    /**
     *
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initView(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.common_layout_floating_log, null);
        Log.i(TAG, "initView: " + mView);
        mLogTv = mView.findViewById(R.id.floating_log_tv);
        mTitleIv = mView.findViewById(R.id.floating_title_iv);
        mTitleTv = mView.findViewById(R.id.floating_title_tv);
        mTipTv = mView.findViewById(R.id.floating_tip_tv);
        mScrollView = mView.findViewById(R.id.floating_sv);
        mLineView = mView.findViewById(R.id.floating_line);
        mCloseIv = mView.findViewById(R.id.floating_close_iv);
        mTitleLl = mView.findViewById(R.id.floating_title_ll);
        mBottomLl = mView.findViewById(R.id.floating_bottom_ll);

        if (!shouldShowTip) mTipTv.setVisibility(View.GONE);
        mTitleTv.setText(title);
        mTitleIv.setOnClickListener(v -> {
            Log.e(TAG, "show tip:" + shouldShowTip);
            if (mScrollView.getVisibility() == View.VISIBLE){
                mScrollView.setVisibility(View.GONE);
                mBottomLl.setVisibility(View.GONE);
                mLineView.setVisibility(View.GONE);
                mTipTv.setVisibility(View.GONE);

                wmParams.width = MIN_WIDTH_SIZE;
                wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                wManager.updateViewLayout(mView,wmParams);
            }else {
                mScrollView.setVisibility(View.VISIBLE);
                mBottomLl.setVisibility(View.VISIBLE);
                mLineView.setVisibility(View.VISIBLE);

                if (shouldShowTip) mTipTv.setVisibility(View.VISIBLE);

                wmParams.width = MIN_WIDTH_SIZE;
                wmParams.height = MIN_HEIGHT_SIZE;
                wManager.updateViewLayout(mView,wmParams);
            }
        });

        mCloseIv.setOnClickListener(v -> {
            clearLogView();
        });

        mCloseIv.setOnLongClickListener(v -> {
            removeLogView();
            return true;
        });

        mView.setAlpha(0.9F);

        mTitleLl.setOnTouchListener(moveWithFingerListener);

        /**
         * 动态调节大小
         */
        mBottomLl.setOnTouchListener((v, event) -> {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mBottomViewDownXInView = (int) event.getX();
                    mBottomViewDownYInView = (int) event.getY();
                    mBottomViewDownXInScreen = (int) event.getRawX();
                    mBottomViewDownYInScreen = (int) event.getRawY();


                    width = mView.getWidth();
                    height = mView.getHeight();
                    Log.i(TAG, "ACTION_DOWN: " + Arrays.asList(width,height));
                    if (mBottomViewDownXInView <= mBottomLl.getWidth()/5){

                        isDragLeft = true;
                        dragLeftX = wmParams.x;
                        return true;
                    }else if (mBottomViewDownXInView >= (mBottomLl.getWidth() - mBottomLl.getWidth()/5)){
                        isDragLeft = false;
                        return true;
                    }else {
                        return false;
                    }
                case MotionEvent.ACTION_MOVE:
                    int dWidth,dHeight;

                    if (isDragLeft){
                        dWidth = (int) (mBottomViewDownXInScreen - event.getRawX());
                        dHeight = (int) (event.getRawY() - mBottomViewDownYInScreen);
                    }else {
                        dWidth = (int) (event.getRawX() - mBottomViewDownXInScreen);
                        dHeight = (int) (event.getRawY() - mBottomViewDownYInScreen);
                    }

                    updateViewSize(dWidth,dHeight,event);
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "ACTION_UP: ");

                    break;
            }
            return true;
        });
    }

    String title;

    @SuppressLint("WrongConstant")
    private void initWindow(){

        wManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && commonROMPermissionCheck(mContext)) {
            title = BaseApplication.getApplication().getString(R.string.glbl_fltng_frm);
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            title = BaseApplication.getApplication().getString(R.string.pplctn_fltng_frm);
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
            ToastUtil.show(mContext, BaseApplication.getApplication().getString(R.string.pn_th_fltng_bx_prms));
        }else {
            title = BaseApplication.getApplication().getString(R.string.glbl_fltng_frm);
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        wmParams.format= PixelFormat.TRANSLUCENT;  // 去除黑色背景
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 去除焦点 避免遮挡掉后面的点击事件
        wmParams.width = MIN_WIDTH_SIZE;
        wmParams.height = MIN_HEIGHT_SIZE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 必须设置!!!! 不设置会导致跟随移动异常
        wmParams.x = 200;
        wmParams.y = 200;
    }

    int mDownXInView;
    int mDownYInView;

    boolean hasAddView;

    int mBottomViewDownXInView;
    int mBottomViewDownYInView;
    int mBottomViewDownXInScreen;
    int mBottomViewDownYInScreen;

    int dragLeftX;
    boolean isDragLeft = false;

    /**
     * 跟随移动
     */
    View.OnTouchListener moveWithFingerListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mDownXInView = (int) event.getX();
                    mDownYInView = (int) event.getY();
//                    Log.i(TAG, "ACTION_DOWN: " + Arrays.asList(mDownXInView,mDownYInView));
                    break;
                case MotionEvent.ACTION_MOVE:
                    wmParams.x = (int) (event.getRawX() - mDownXInView);
                    wmParams.y = (int) (event.getRawY() - mDownYInView);
//                    Log.i(TAG, "ACTION_MOVE: " + Arrays.asList(wmParams.x, wmParams.y));
                    updateViewPosition(); // 手指移动的时候更新小悬浮窗的位置
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
    };


    @SuppressLint("ClickableViewAccessibility")
    public void showLogView(){
        updateTipTag();
        addViewSafety();
    }

    public void removeLogView(){
        if (hasAddView){
            wManager.removeViewImmediate(mView);
            hasAddView = false;
        }
    }

    private void updateViewPosition() {
        wManager.updateViewLayout(mView, wmParams);
    }


    /**
     * 计算窗口大小
     */
    int width;
    int height;

    private void updateViewSize(int dWidth,int dHeight,MotionEvent event){

//        Log.i(TAG,"updateViewSize:" + Arrays.asList(dWidth,dHeight));

        int newWidth = Math.max(width + dWidth,MIN_WIDTH_SIZE);
        int newHeight = Math.max(height + dHeight,MIN_HEIGHT_SIZE);

//        if (Math.abs(wmParams.width - newWidth) < 5 && Math.abs(wmParams.height - newHeight) < 5){
//            Log.i(TAG, "ignore change: tiny changed size " + Arrays.asList(wmParams.width - newWidth,wmParams.height - newHeight));
//            return;
//        }

//        Log.i(TAG, "change wh: " + Arrays.asList(newWidth - wmParams.width ,newHeight - wmParams.height));
//        Log.i(TAG, "new wh: " + Arrays.asList(newWidth ,newHeight));


        if (isDragLeft){
//            这里不能用mBottomViewDownXInScreen 会有误差
//            int changeX = mBottomViewDownXInScreen - dWidth;
            int changeX = dragLeftX + width - newWidth;
            Log.i(TAG, "changeSize: " + Arrays.asList(changeX,Math.max(changeX, 0), wmParams.x));
            wmParams.x = Math.max(changeX, 0);
//            Log.i(TAG, "x right: " + Arrays.asList(wmParams.x + newWidth));
        }

        wmParams.width = newWidth;
        wmParams.height = newHeight;
        wManager.updateViewLayout(mView,wmParams);
    }



    public void clearLogView(){
        Log.i(TAG, "clearLogView");
        mLogTv.setText("");
    }

    public static void addLog(String log){
        instance.addLogInside(log);
    }

    private void addLogInside(String log){
        Log.e(TAG, "addLog: " + log);

        addViewSafety();

        CharSequence text = mLogTv.getText();
        if (!TextUtils.isEmpty(text)) text = text + "\n";
        String s = text + log;
        mLogTv.setText(s);
    }


    private void addViewSafety(){
        if (hasAddView)  return;

        try {
            wManager.addView(mView,wmParams);
            hasAddView = true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String SP_DEFAULT = "sp_default";
    private String SP_FIRST_SHOW_TIP = "sp_first_show_tip";

    private int getTipTag(){
        return mContext.getSharedPreferences("SP_DEFAULT",Context.MODE_PRIVATE).getInt(SP_FIRST_SHOW_TIP,0);
    }

    private void updateTipTag(){
        Log.i(TAG, "updateTipTag");
        mContext.getSharedPreferences("SP_DEFAULT",Context.MODE_PRIVATE).edit().putInt(SP_FIRST_SHOW_TIP,1).apply();
    }

    public void setTitle(String title){
        mTitleTv.setText(title);
    }

    /**
     * 动态请求悬浮窗权限
     */
    public void requestOverlayPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(mContext)){
                hasPermission = false;
                Toast.makeText(mContext,"请打开悬浮框权限以展示log日志",Toast.LENGTH_SHORT).show();
                String ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION";
                Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mContext.getPackageName()));
                ((Activity) mContext ).startActivityForResult(intent, REQUEST_OVERLAY);
            }
        }
    }

    @SuppressLint("WrongConstant")
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data){
        Log.i(TAG, "onActivityResult: " + Arrays.asList(requestCode,resultCode));
        if (requestCode == REQUEST_OVERLAY){
            if (commonROMPermissionCheck(activity)){
                removeLogView();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                }
                addViewSafety();
                addLogInside("悬浮框状态已更新，当前为全局悬浮框");
                setTitle(BaseApplication.getApplication().getString(R.string.glbl_fltng_frm));
            }
        }

    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //判断权限
    public static boolean commonROMPermissionCheck(Context context) {
        Boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class clazz = Settings.class;
                Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                result = (Boolean) canDrawOverlays.invoke(null, context);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
        return result;
    }

}
