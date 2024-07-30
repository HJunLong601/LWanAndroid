package com.hjl.language

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.os.ConfigurationCompat
import java.util.Locale


/**
 * Author : long
 * Description : 多语言支持,适配可参考: https://juejin.cn/post/7046685955230531597#heading-5
 * Date : 2024/3/7
 */
public object MultiLanguage {

    const val TAG = "MultiLanguage"

    lateinit var languageSetting: ILanguageSetting

    fun init(context: Context, languageSetting: ILanguageSetting) {
        this.languageSetting = languageSetting
        languageSetting.init(context)
    }

    fun attachBaseContext(context: Context): Context {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            createConfigurationContext(context, languageSetting.getSetUpdLocale())
        } else {
            updateConfiguration(context, languageSetting.getSetUpdLocale());
        }
    }

    fun onRequestPermissionsResult(context: Context) {
        updateConfiguration(context.applicationContext, languageSetting.getSetUpdLocale())
    }

    // 注意此处不是Build.VERSION_CODES.N
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private fun createConfigurationContext(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration: Configuration = resources.configuration
        Log.i(TAG, "current Language locale = $locale")
        val localeList = LocaleList(locale)
        // 注意此处setLocales
        configuration.setLocales(localeList)
        return context.createConfigurationContext(configuration)
    }

    private fun updateConfiguration(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        Log.i(TAG, "updateConfiguration" + locale.language)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // apply locale 注意此处是setLocales
            configuration.setLocales(LocaleList(locale))
        } else {
            // updateConfiguration
            configuration.locale = locale
            val dm = resources.displayMetrics
            resources.updateConfiguration(configuration, dm)
        }
        return context
    }

    fun getSystemLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)!!
        } else {
            Locale.getDefault()
        }
    }

}