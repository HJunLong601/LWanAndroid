package com.hjl.core.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.hjl.commonlib.constant.Constant
import com.hjl.commonlib.customview.CircleProgressButton
import com.hjl.commonlib.utils.DensityUtil
import com.hjl.commonlib.utils.LogUtils
import com.hjl.commonlib.utils.StatusBarUtil
import com.hjl.core.R
import com.hjl.core.databinding.CoreActivitySimpleWebBinding

import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import java.util.*

open class SimpleWebActivity : AppCompatActivity() {


    lateinit var progressBar : CircleProgressButton
    lateinit var binding: CoreActivitySimpleWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.core_activity_simple_web
        )


        val url = intent.getStringExtra(Constant.INTENT_KEY01)
        LogUtils.i("load url:$url")

        progressBar = CircleProgressButton(this)
        progressBar.setEnableWelt(true)
        progressBar.setOnClickListener {
            if (binding.coreSimpleWb.canGoBack()){
                binding.coreSimpleWb.goBack()
            }else{
                finish()
            }
        }
        val layoutParams = FrameLayout.LayoutParams(DensityUtil.dp2px(50F),DensityUtil.dp2px(50F))
        layoutParams.apply {
            this.gravity = (Gravity.BOTTOM or Gravity.START)
            this.bottomMargin = DensityUtil.dp2px(55F)
            this.marginStart = DensityUtil.dp2px(20F)
        }

        window.addContentView(progressBar,layoutParams)
        binding.coreSimpleWb.webChromeClient = SimpleWebViewClient()
        binding.coreSimpleWb.loadUrl(url)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (binding.coreSimpleWb.canGoBack()){
                binding.coreSimpleWb.goBack()
                return true
            }
        }


        return super.onKeyDown(keyCode, event)
    }

    inner class SimpleWebViewClient : WebChromeClient() {

        override fun onProgressChanged(p0: WebView?, p1: Int) {
            progressBar.currentProgress = p1.toFloat()
            super.onProgressChanged(p0, p1)
        }

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
        return R.color.common_white
    }

    companion object{

        fun loadUrl(activity: Activity,url : String){
            val intent  = Intent(activity,SimpleWebActivity::class.java)
            intent.putExtra(Constant.INTENT_KEY01,url)
            activity.startActivity(intent)
        }

    }


}
