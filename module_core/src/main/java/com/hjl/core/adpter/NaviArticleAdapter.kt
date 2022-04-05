package com.hjl.core.adpter

import android.app.Activity
import com.hjl.core.R
import com.hjl.core.databinding.CoreItemSystemArticleDetailBinding
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.net.bean.SystemListBean
import com.hjl.core.ui.SimpleWebActivity
import com.hjl.jetpacklib.mvvm.recycleview.BaseRecyclerViewAdapter

/**
 * author: long
 * description please add a description here
 * Date: 2021/5/12
 */
class NaviArticleAdapter : BaseRecyclerViewAdapter<HomeArticleBean.Article, CoreItemSystemArticleDetailBinding>() {

    override fun bindData(binding: CoreItemSystemArticleDetailBinding?, data: HomeArticleBean.Article?, index : Int) {
        binding?.data = data?.title
        binding?.root?.setOnClickListener {
            SimpleWebActivity.loadUrl(mContext as Activity,data!!.link)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.core_item_system_article_detail
    }

    override fun bindData(
        binding: CoreItemSystemArticleDetailBinding?,
        data: HomeArticleBean.Article?
    ) {

    }
}