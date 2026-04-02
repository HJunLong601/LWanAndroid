package com.hjl.lwanandroid

import android.R
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.view.ContextThemeWrapper
import com.hjl.commonlib.base.ActivityDelegate
import com.hjl.commonlib.base.ActivityDelegateRegistry
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
        ActivityDelegateRegistry.delegate = object : ActivityDelegate {
            override fun attachBaseContext(base: Context): Context {
                return MultiLanguage.attachBaseContext(base)
            }

            override fun onRequestPermissionsResult(activity: android.app.Activity) {
                MultiLanguage.onRequestPermissionsResult(activity)
            }
        }

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
        // 设置换肤资源获取器
        ResourceManager.getInstance().setiResourceAcquirer(SkinResourceAcquirer())
        super.onCreate()
        LogUtils.i("onCreate")
        initSkinSupport()
        initLiveEventBus()
        getInstance(this)
    }

    private fun initSkinSupport() {
        SkinCompatManager.withoutActivity(this)
            .addInflater(SkinAppCompatViewInflater()) // 基础控件换肤支持
            .addInflater(SkinMaterialViewInflater()) // Material 控件换肤支持
            .addInflater(SkinConstraintViewInflater()) // ConstraintLayout 换肤支持
            .addInflater(SkinCardViewInflater()) // CardView 换肤支持
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
