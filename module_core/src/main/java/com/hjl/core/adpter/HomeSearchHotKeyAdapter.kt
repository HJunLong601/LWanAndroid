package com.hjl.core.adpter


import com.hjl.core.R
import com.hjl.core.databinding.CoreItemHotSearchBinding
import com.hjl.core.net.bean.HomeSearchHotKeyBean
import com.hjl.jetpacklib.mvvm.recycleview.BaseRecyclerViewAdapter

/**
 * author: long
 * description please add a description here
 * Date: 2020/9/30
 */
class HomeSearchHotKeyAdapter : BaseRecyclerViewAdapter<HomeSearchHotKeyBean, CoreItemHotSearchBinding>() {
    override fun bindData(binding: CoreItemHotSearchBinding?, data: HomeSearchHotKeyBean?) {
        binding?.data = data
    }

    override fun getLayoutId(): Int {
        return R.layout.core_item_hot_search
    }
}