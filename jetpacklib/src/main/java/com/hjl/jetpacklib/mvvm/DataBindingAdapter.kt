package com.hjl.jetpacklib.mvvm

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData


/**
 * @author: long
 * @description please add a description here
 * @Date: 2020/6/4
 */


//@BindingAdapter("app:banner_data")
//fun loadBanner(view : Banner<HomeBannerBean,ImageBannerAdapter>,dataList : List<HomeBannerBean>?){
//    if (dataList.isNullOrEmpty()) return
//    view.setDatas(dataList)
//}
//

@BindingAdapter("focusImageDrawable")
fun bindFocusLiveData(view : View,liveData: MutableLiveData<Boolean>){
    view.setOnFocusChangeListener { v, hasFocus ->
        liveData.postValue(hasFocus)
    }
}

@SuppressLint("ResourceType")
@BindingAdapter("loadDrawable")
fun ImageView.setImageTint(@ColorInt color: Int) {
    setImageResource(color)
}