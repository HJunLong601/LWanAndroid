package com.hjl.core.adpter

import android.view.ViewGroup
import com.google.android.flexbox.FlexboxLayoutManager
import com.hjl.core.R
import com.hjl.core.databinding.CoreItemHomeSystemBinding
import com.hjl.core.net.bean.NavigationListBean
import com.hjl.jetpacklib.mvvm.recycleview.BaseRecyclerViewAdapter
import com.hjl.jetpacklib.mvvm.recycleview.BaseViewHolder

/**
 * author: long
 * description please add a description here
 * Date: 2021/5/12
 */
class NaviListAdapter : BaseRecyclerViewAdapter<NavigationListBean, CoreItemHomeSystemBinding>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<CoreItemHomeSystemBinding> {
        return super.onCreateViewHolder(parent, viewType).also { it.setIsRecyclable(false) }
    }

    override fun bindData(
        binding: CoreItemHomeSystemBinding?,
        data: NavigationListBean?
    ) {
        binding?.let {
            val naviListAdapter = NaviArticleAdapter()
            naviListAdapter.setNewData(data?.articles)

            it.coreItemSystemTv.text = data?.name
            it.coreItemSystemRv.apply {
                adapter = naviListAdapter
                layoutManager = FlexboxLayoutManager(context)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.core_item_home_system
    }
}