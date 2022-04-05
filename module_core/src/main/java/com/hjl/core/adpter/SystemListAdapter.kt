package com.hjl.core.adpter

import android.view.ViewGroup
import com.google.android.flexbox.FlexboxLayoutManager
import com.hjl.core.R
import com.hjl.core.databinding.CoreItemHomeSystemBinding
import com.hjl.core.net.bean.SystemListBean
import com.hjl.jetpacklib.mvvm.recycleview.BaseRecyclerViewAdapter
import com.hjl.jetpacklib.mvvm.recycleview.BaseViewHolder

/**
 * Author : long
 * Description :
 * Date : 2021/5/6
 */
class SystemListAdapter : BaseRecyclerViewAdapter<SystemListBean,CoreItemHomeSystemBinding>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<CoreItemHomeSystemBinding> {
        return super.onCreateViewHolder(parent, viewType).also { it.setIsRecyclable(false) }
    }

    override fun bindData(
        binding: CoreItemHomeSystemBinding?,
        data: SystemListBean?
    ) {
        binding?.let {
            val detailAdapter = SystemArticleAdapter(data!!)
            detailAdapter.setNewData(data.children)

            it.coreItemSystemTv.text = data.name
            it.coreItemSystemRv.apply {
                layoutManager = FlexboxLayoutManager(context)
                adapter = detailAdapter
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.core_item_home_system
    }
}