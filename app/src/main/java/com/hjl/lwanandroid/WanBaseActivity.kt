package com.hjl.lwanandroid

import android.content.Context
import com.hjl.commonlib.base.SkinBaseActivity
import com.hjl.language.MultiLanguage

abstract class WanBaseActivity : SkinBaseActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MultiLanguage.attachBaseContext(newBase!!))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        MultiLanguage.onRequestPermissionsResult(this)
    }
}
