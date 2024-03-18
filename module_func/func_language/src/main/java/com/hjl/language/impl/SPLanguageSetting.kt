package com.hjl.language.impl

import android.content.Context
import android.content.SharedPreferences
import com.hjl.language.ILanguageSetting
import com.hjl.language.MultiLanguage
import java.util.Locale

/**
 * Author : long
 * Description :
 * Date : 2024/3/8
 */
class SPLanguageSetting : ILanguageSetting {

    lateinit var sp: SharedPreferences

    override fun init(context: Context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    override fun saveLanguageSetting(data: String) {
        sp.edit().putString(SP_KEY_LANGUAGE, data).commit()
    }

    override fun getLanguageSetting(): String = sp.getString(SP_KEY_LANGUAGE, LANGUAGE_SYSTEM)!!

    override fun getSetUpdLocale(): Locale {
        return when (getLanguageSetting()) {
            LANGUAGE_CHINESE -> Locale.SIMPLIFIED_CHINESE
            LANGUAGE_ENGLISH -> Locale.US
            else -> MultiLanguage.getSystemLocale()
        }
    }

    companion object {
        const val SP_NAME = "sp_multi_language"
        const val SP_KEY_LANGUAGE = "sp_key_language_name"

        const val LANGUAGE_SYSTEM = "language_system"
        const val LANGUAGE_CHINESE = "language_chinese"
        const val LANGUAGE_ENGLISH = "language_english"
    }


}