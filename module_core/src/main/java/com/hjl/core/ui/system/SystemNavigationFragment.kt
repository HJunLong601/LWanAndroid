package com.hjl.core.ui.system

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjl.core.R
import com.hjl.core.adpter.NaviListAdapter
import com.hjl.core.databinding.CoreFragmentNavigationBinding
import com.hjl.core.viewmodel.HomeViewModel
import com.hjl.commonlib.extend.addDivider
import com.hjl.jetpacklib.mvvm.view.BaseMVVMFragment2

/**
 * author: long
 * description 主页 - 体系 - 导航
 * Date: 2021/5/12
 */
class SystemNavigationFragment : BaseMVVMFragment2<CoreFragmentNavigationBinding,HomeViewModel>(){

    lateinit var naviAdapter : NaviListAdapter

    override fun initLayoutResID(): Int {
        return R.layout.core_fragment_navigation
    }

    override fun initData() {
        naviAdapter = NaviListAdapter()
    }

    override fun initView() {
        showLoading()
        binding.coreFragmentNavigationRv.apply {
            adapter = naviAdapter
            addDivider()
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun loadData() {
        viewModel.getNaviList()
        viewModel.naviListData.observe(this, Observer {
            if (it.isNotEmpty()){
                naviAdapter.setNewData(it)
                showComplete()
            }
        })
    }
}