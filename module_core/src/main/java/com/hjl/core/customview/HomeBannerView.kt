package com.hjl.core.customview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.hjl.core.adpter.ImageBannerAdapter
import com.hjl.core.net.bean.HomeBannerBean
import com.youth.banner.Banner

class HomeBannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Banner<HomeBannerBean, ImageBannerAdapter>(context, attrs, defStyleAttr) {

    override fun requestChildFocus(child: View?, focused: View?) {
    }

    override fun requestChildRectangleOnScreen(child: View, rectangle: Rect, immediate: Boolean): Boolean {
        return false
    }
}
