package com.hjl.language

import android.content.Context
import java.util.Locale

/**
 * Author : long
 * Description : 语言设置保存的接口类
 * Date : 2024/3/7
 */
interface ILanguageSetting {

    fun init(context: Context)

    fun saveLanguageSetting(data: String)

    fun getLanguageSetting(): String

    fun getSetUpdLocale(): Locale


}