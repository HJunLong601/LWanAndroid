package com.hjl.commonlib.extend

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjl.commonlib.R
import com.hjl.commonlib.base.ResourceManager
import com.hjl.commonlib.utils.DensityUtil
import com.hjl.commonlib.utils.RecycleViewGridDivider
import com.hjl.commonlib.utils.RecycleViewVerticalDivider
import com.hjl.commonlib.utils.ToastUtil


/**
 * author: long
 * description please add a description here
 * Date: 2020/9/17
 */

val Any.KTAG : String
get() = javaClass.simpleName

fun String.showToast(){
    ToastUtil.show(this)
}

fun RecyclerView.addDivider(paddingLeft : Float = 0f,paddingRight : Float = 0F,showEnd : Boolean = true,
                            dividerHeight : Int = DensityUtil.dp2px(1f),
                            dividerColor : Int = ResourceManager.getInstance().getColor(context,R.color.common_divider_line_color)
){

    if (layoutManager is GridLayoutManager){
        addGridDivider(dividerHeight,dividerColor)
        return
    }

    addItemDecoration(
            RecycleViewVerticalDivider(context,
                dividerHeight,
                dividerColor,
                DensityUtil.dp2px(paddingLeft),
                DensityUtil.dp2px(paddingRight),
                showEnd
            )
    )
}

fun RecyclerView.addGridDivider(dividerHeight : Int = DensityUtil.dp2px(1f),
                                dividerColor : Int = ResourceManager.getInstance().getColor(context,R.color.common_divider_line_color),
                                showEnd: Boolean = true
){
    val divider = RecycleViewGridDivider(context,dividerHeight,dividerColor)
    divider.setShowEnd(showEnd)
    addItemDecoration(
        divider
    )
}

