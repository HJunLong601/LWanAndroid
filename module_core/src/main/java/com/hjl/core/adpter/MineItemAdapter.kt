package com.hjl.core.adpter


import com.hjl.core.R
import com.hjl.core.databinding.CoreItemMineBinding
import com.hjl.core.net.bean.MineItemBean

import com.hjl.jetpacklib.mvvm.recycleview.BaseRecyclerViewAdapter

/**
 * Author : long
 * Description :
 * Date : 2020/8/29
 */
class MineItemAdapter : BaseRecyclerViewAdapter<MineItemBean, CoreItemMineBinding>() {
    override fun bindData(binding: CoreItemMineBinding?, data: MineItemBean?) {
        binding?.data = data
    }

    override fun getLayoutId(): Int {
        return R.layout.core_item_mine
    }
}