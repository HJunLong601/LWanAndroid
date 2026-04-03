package com.hjl.jetpacklib.mvvm.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import com.hjl.commonlib.R
import com.hjl.commonlib.base.SkinBaseActivity
import com.hjl.commonlib.utils.StatusBarUtil

/**
 * Compose 页面基类，保持和现有 Activity 一样的基类链路与公共行为。
 */
abstract class BaseComposeActivity : SkinBaseActivity() {

    protected lateinit var mContext: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setStatusBar()
    }

    protected open fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isUseFullScreenMode()) {
                StatusBarUtil.transparencyBar(this)
            } else {
                StatusBarUtil.setStatusBarColor(this, getStatusBarColor())
            }
            if (isUseBlackFontWithStatusBar()) {
                StatusBarUtil.setLightStatusBar(this, true, isUseFullScreenMode())
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setStatusBar(window)
        }
    }

    /**
     * 是否改变状态栏文字颜色为黑色，默认为黑色。
     */
    protected open fun isUseBlackFontWithStatusBar(): Boolean {
        return true
    }

    /**
     * 是否设置成透明状态栏，即全屏模式。
     */
    protected open fun isUseFullScreenMode(): Boolean {
        return false
    }

    /**
     * 更改状态栏颜色，只有非全屏模式下有效。
     */
    protected open fun getStatusBarColor(): Int {
        return R.color.common_white
    }
}
