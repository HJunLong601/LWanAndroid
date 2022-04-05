package com.hjl.jetpacklib.mvvm.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.hjl.commonlib.R
import com.hjl.commonlib.utils.StatusBarUtil

/**
 * Description Activity 基类
 * Date 2020/3/2 16:24
 * created by long
 */
abstract class BaseActivity<VDB : ViewDataBinding> : AppCompatActivity() {

    protected val TAG: String = this.javaClass.simpleName

    protected lateinit var binding : VDB

    lateinit var mContext: Activity

    abstract fun initData()

    abstract fun initView()

    abstract fun initLoad()

    abstract fun getLayoutId() : Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,getLayoutId())
        binding.lifecycleOwner = this
        mContext = this

        initData()
        initView()
        initLoad()
        setStatusBar()

    }

    override fun onResume() {
        super.onResume()
//        do nothing , override for test transform
    }

    override fun onDestroy() {
        super.onDestroy()
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
     * 是否改变状态栏文字颜色为黑色，默认为黑色
     */
    protected open fun isUseBlackFontWithStatusBar(): Boolean {
        return true
    }

    /**
     * 是否设置成透明状态栏，即就是全屏模式
     */
    protected open fun isUseFullScreenMode(): Boolean {
        return false
    }

    /**
     * 更改状态栏颜色，只有非全屏模式下有效
     */
    protected open fun getStatusBarColor(): Int {
        return R.color.common_base_theme_color
    }

}