package com.hjl.core.utils

import android.app.Activity
import android.view.View
import androidx.databinding.BindingAdapter
import com.hjl.core.ui.SimpleWebActivity

/**
 * author: long
 * description please add a description here
 * Date: 2021/4/19
 */


@BindingAdapter("app:linkUrl")
fun jumpUrl(view: View, url : String){
    view.setOnClickListener { SimpleWebActivity.loadUrl(view.context as Activity,url)}
}