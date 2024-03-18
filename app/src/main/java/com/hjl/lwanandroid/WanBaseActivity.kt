package com.hjl.lwanandroid

import android.content.Context
import com.hjl.language.MultiLanguage
import com.hjl.lwanandroid.skin.SkinBaseActivity

abstract class WanBaseActivity : SkinBaseActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MultiLanguage.attachBaseContext(newBase!!))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        MultiLanguage.onRequestPermissionsResult(this)
    }
}