package com.hjl.core.adpter

import com.hjl.core.R
import com.hjl.core.databinding.CoreItemCommonlyWebBinding
import com.hjl.core.net.bean.CommonlyWebBean
import com.hjl.jetpacklib.mvvm.recycleview.BaseRecyclerViewAdapter

/**
 * Author : long
 * Description : 常用网站item
 * Date : 2021/5/2
 */
class HomeWebCommonlyAdapter : BaseRecyclerViewAdapter<CommonlyWebBean, CoreItemCommonlyWebBinding>()  {
    override fun bindData(binding: CoreItemCommonlyWebBinding?, data: CommonlyWebBean?) {
        binding?.data = data
    }

    override fun getLayoutId(): Int {
        return R.layout.core_item_commonly_web
    }
}