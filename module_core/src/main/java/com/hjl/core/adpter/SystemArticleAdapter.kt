package com.hjl.core.adpter

import com.hjl.core.R
import com.hjl.core.databinding.CoreItemSystemArticleDetailBinding
import com.hjl.core.net.bean.SystemListBean
import com.hjl.core.ui.system.SystemArticleDetailActivity
import com.hjl.jetpacklib.mvvm.recycleview.BaseRecyclerViewAdapter

/**
 * Author : long
 * Description :
 * Date : 2021/5/6
 */
class SystemArticleAdapter(private val parentArticleBean : SystemListBean) : BaseRecyclerViewAdapter<SystemListBean,CoreItemSystemArticleDetailBinding>() {

    override fun bindData(binding: CoreItemSystemArticleDetailBinding?, data: SystemListBean?, index : Int) {
        binding?.data = data?.name
        binding?.root?.setOnClickListener {
            SystemArticleDetailActivity.startSystemArticleDetailActivity(mContext,parentArticleBean,index)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.core_item_system_article_detail
    }

    override fun bindData(binding: CoreItemSystemArticleDetailBinding?, data: SystemListBean?) {

    }
}