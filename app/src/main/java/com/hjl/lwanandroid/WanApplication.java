package com.hjl.lwanandroid;

import com.hjl.commonlib.base.BaseApplication;
import com.hjl.commonlib.base.ResourceManager;
import com.hjl.lwanandroid.skin.SkinResourceAcquirer;
import com.hjl.module_base.datbase.WanDatabase;
import com.jeremyliao.liveeventbus.core.LiveEventBusCore;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

public class WanApplication extends BaseApplication {

    @Override
    public void onCreate() {

        // 设置换肤支持
        ResourceManager.getInstance().setiResourceAcquirer(new SkinResourceAcquirer());

        super.onCreate();

        initSkinSupport();
        initLiveEventBus();
        WanDatabase.Companion.getInstance(this);
    }

    private void initSkinSupport() {
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinAppCompatViewInflater())     // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
//                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
//                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
    }

    private void initLiveEventBus(){
        LiveEventBusCore.get().config()
                .lifecycleObserverAlwaysActive(true);
    }
}
