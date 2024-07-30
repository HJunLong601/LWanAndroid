package com.hjl.lwanandroid

import android.R
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.view.ContextThemeWrapper
import com.hjl.commonlib.base.BaseApplication
import com.hjl.commonlib.base.ResourceManager
import com.hjl.commonlib.utils.LogUtils
import com.hjl.language.MultiLanguage
import com.hjl.language.impl.SPLanguageSetting
import com.hjl.lwanandroid.skin.SkinResourceAcquirer
import com.hjl.module_base.datbase.WanDatabase.Companion.getInstance
import com.jeremyliao.liveeventbus.core.LiveEventBusCore
import dagger.hilt.android.HiltAndroidApp
import skin.support.SkinCompatManager
import skin.support.app.SkinAppCompatViewInflater
import skin.support.app.SkinCardViewInflater
import skin.support.constraint.app.SkinConstraintViewInflater
import skin.support.design.app.SkinMaterialViewInflater


@HiltAndroidApp
class WanApplication : BaseApplication() {
    override fun attachBaseContext(base: Context) {
        MultiLanguage.init(base, SPLanguageSetting())

        val attachBaseContext = MultiLanguage.attachBaseContext(base)
        val configuration = attachBaseContext.resources.configuration

        val wrappedContext: ContextThemeWrapper =
            object : ContextThemeWrapper(attachBaseContext, R.style.Theme) {
                override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
                    overrideConfiguration?.setTo(configuration)
                    super.applyOverrideConfiguration(overrideConfiguration)
                }
            }

        super.attachBaseContext(wrappedContext)
    }

    override fun onCreate() {
        // 设置换肤支持
        ResourceManager.getInstance().setiResourceAcquirer(SkinResourceAcquirer())
        super.onCreate()
        LogUtils.i("onCreate")
        initSkinSupport()
        initLiveEventBus()
        getInstance(this)
    }

    private fun initSkinSupport() {
        SkinCompatManager.withoutActivity(this)
            .addInflater(SkinAppCompatViewInflater()) // 基础控件换肤初始化
            .addInflater(SkinMaterialViewInflater()) // material design 控件换肤初始化[可选]
            .addInflater(SkinConstraintViewInflater()) // ConstraintLayout 控件换肤初始化[可选]
            .addInflater(SkinCardViewInflater()) // CardView v7 控件换肤初始化[可选]
            //                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
            //                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
            .loadSkin()
    }

    private fun initLiveEventBus() {
        LiveEventBusCore.get().config()
            .lifecycleObserverAlwaysActive(true)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        MultiLanguage.attachBaseContext(this)
    }
}
