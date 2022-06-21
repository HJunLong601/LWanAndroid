package com.hjl.core.ui.system

import androidx.recyclerview.widget.LinearLayoutManager
import com.hjl.core.R
import com.hjl.core.adpter.SystemListAdapter
import com.hjl.core.databinding.CoreFragmentHomeSystemBinding
import com.hjl.core.viewmodel.HomeViewModel
import com.hjl.commonlib.extend.addDivider
import com.hjl.jetpacklib.mvvm.view.BaseMVVMFragment2

/**
 * Author : long
 * Description :
 * Date : 2021/5/6
 */
class SystemListFragment : BaseMVVMFragment2<CoreFragmentHomeSystemBinding, HomeViewModel>() {

    lateinit var systemArticleAdapter : SystemListAdapter

    override fun initLayoutResID(): Int {
        return R.layout.core_fragment_home_system
    }

    override fun initData() {

    }

    override fun initView() {
        showLoading()
        systemArticleAdapter = SystemListAdapter()
        binding.coreHomeSystemRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = systemArticleAdapter
            addDivider(paddingLeft = 8.0F,paddingRight = 8.0F)
        }

    }

    override fun loadData() {
        viewModel.systemListData.observe(this, {
            if (it.isNotEmpty()){
                systemArticleAdapter.setNewData(it)
                showComplete()
            }else{
                showEmpty()
            }
        })

        viewModel.getSystemList()
    }
}