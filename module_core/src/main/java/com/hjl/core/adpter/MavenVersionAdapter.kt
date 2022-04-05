package com.hjl.core.adpter

import com.hjl.core.R
import com.hjl.core.databinding.CoreItemMavenVersionBinding
import com.hjl.core.net.bean.MavenItemBean
import com.hjl.jetpacklib.mvvm.recycleview.BaseRecyclerViewAdapter

/**
 * author: long
 * description please add a description here
 * Date: 2021/5/28
 */
class MavenVersionAdapter : BaseRecyclerViewAdapter<MavenItemBean.MavenVersionBean,CoreItemMavenVersionBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.core_item_maven_version
    }


    override fun bindData(
        binding: CoreItemMavenVersionBinding?,
        data: MavenItemBean.MavenVersionBean?
    ) {
        binding?.coreItemMavenArtifactTv?.text = data?.group
        binding?.coreItemMavenVersionTv?.text = data?.version
    }
}