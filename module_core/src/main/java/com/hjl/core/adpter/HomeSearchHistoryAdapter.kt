package com.hjl.core.adpter

import com.hjl.core.R
import com.hjl.core.databinding.CoreItemSearchHistoryBinding
import com.hjl.core.net.bean.SimpleStringBean
import com.hjl.jetpacklib.mvvm.recycleview.BaseRecyclerViewAdapter

/**
 * Author : long
 * Description :
 * Date : 2021/5/2
 */
class HomeSearchHistoryAdapter :
    BaseRecyclerViewAdapter<String, CoreItemSearchHistoryBinding>() {
    override fun bindData(binding: CoreItemSearchHistoryBinding?, data: String?) {
        binding?.itemHistoryTv?.text = data
        addChildClick(R.id.item_history_delete_iv)
    }

    override fun getLayoutId(): Int {
        return R.layout.core_item_search_history
    }
}