package com.hjl.jetpacklib.mvvm.recycleview

import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.hjl.jetpacklib.R


/**
 * author: long
 * description 通用的简单文本适配器
 * Date: 2021/10/21
 */
class SimpleTextAdapter<DB : ViewDataBinding?>(val customLayout : Int = R.layout.base_item_simple_text) : BaseRecyclerViewAdapter<String,DB>() {
    override fun bindData(binding: DB, data: String?) {
        val root = binding?.root?.findViewById<TextView>(R.id.base_simple_text_tv)
        root?.text = data
    }

    override fun getLayoutId(): Int {
        return customLayout
    }
}