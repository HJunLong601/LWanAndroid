package com.hjl.jetpacklib.mvvm.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

/**
 * Compose 二级页通用标题栏，统一处理沉浸式状态栏占位和返回按钮样式。
 */
@Composable
fun BaseComposeTitleBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF04B5FD),
    contentColor: Color = Color.White,
    immersive: Boolean = true
) {
    val containerModifier = if (immersive) {
        modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .statusBarsPadding()
            .height(48.dp)
    } else {
        modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .height(48.dp)
    }

    Box(modifier = containerModifier) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(48.dp)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.common_icon_back),
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            color = contentColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal
        )
    }
}
