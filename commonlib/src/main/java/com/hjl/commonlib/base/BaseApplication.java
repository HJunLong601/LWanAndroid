package com.hjl.commonlib.base;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hjl.commonlib.BuildConfig;
import com.hjl.commonlib.utils.LogUtils;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.lang.reflect.Method;
import java.util.HashMap;


public class BaseApplication extends Application {

    protected static Handler mHandler;
    protected static Handler mWorkHandler;

    private static BaseApplication application;

    private String TAG = getClass().getSimpleName();

//    private RefWatcher refWatcher;
//
//    public static RefWatcher getRefWatcher(Context context) {
//        BaseApplication application = (BaseApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }

    private String[] modules = new String[]{
            "com.hjl.module_minus.MinusApplication"
    };



    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        initHandler();


//        refWatcher = LeakCanary.install(this);

        initX5Core();
        initARouter();
        initModule(modules);

    }

    private void initHandler() {
        mHandler = new Handler(Looper.getMainLooper());
        HandlerThread handlerThread = new HandlerThread("applicationWorker");
        handlerThread.start();
        mWorkHandler = new Handler(handlerThread.getLooper());
    }

    private void initARouter() {
        if (BuildConfig.DEBUG){  // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }

        ARouter.init(this);
    }

    private void initX5Core(){

       try {
           // 在调用TBS初始化、创建WebView之前进行如下配置
           HashMap map = new HashMap();
           map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
           map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
           QbSdk.initTbsSettings(map);

           QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
               @Override
               public void onCoreInitFinished() {
                   LogUtils.i(TAG,"TBS core init Finish");
               }

               @Override
               public void onViewInitFinished(boolean b) {
                   LogUtils.i(TAG,"TBS view init result :" + b);
               }
           });
       }catch (Exception e){
           LogUtils.i(TAG,"TBS Init Exception:" + e.getMessage());
       }
    }

    private void initModule(String[] modules){

        for (String module : modules){
            try {
                Class cls = Class.forName(module);
                Method method = cls.getMethod("initApp",Application.class);
                method.invoke(cls.newInstance(),this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static BaseApplication getApplication(){
        return application;
    }

    public static void runOnUIThread(Runnable runnable){
        mHandler.post(runnable);
    }

    public static void runOnUIThread(Runnable runnable,long delay){
        mHandler.postDelayed(runnable,delay);
    }

    public static void run(Runnable runnable){
        mWorkHandler.post(runnable);
    }

    public static void run(Runnable runnable,long delay){
        mWorkHandler.postDelayed(runnable,delay);
    }



}
