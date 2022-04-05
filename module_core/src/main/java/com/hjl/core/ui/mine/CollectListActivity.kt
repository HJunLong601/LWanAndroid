package com.hjl.core.ui.mine

import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjl.core.R
import com.hjl.core.adpter.CollectListAdapter
import com.hjl.core.databinding.CoreActivityCollectListBinding
import com.hjl.core.repository.paging.CollectArticlePagingSource
import com.hjl.commonlib.extend.addDivider
import com.hjl.jetpacklib.mvvm.view.BaseActivity

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CollectListActivity : BaseActivity<CoreActivityCollectListBinding>() {


    private lateinit var mAdapter : CollectListAdapter


    override fun getLayoutId(): Int {
        return R.layout.core_activity_collect_list
    }

    override fun initData() {

    }

    override fun initLoad() {

    }

    override fun initView() {
        binding.baseTitleLayout.titleCenterTv.text = "我的收藏"
        binding.baseTitleLayout.titleLeftIv.setOnClickListener { finish() }
        mAdapter = CollectListAdapter()
        binding.coreCollectRv.apply {
            adapter = mAdapter;
            layoutManager = LinearLayoutManager(context)
            addDivider()
        }

        lifecycleScope.launch(Dispatchers.IO){
            Pager(pagingSourceFactory = {
                CollectArticlePagingSource()
            },
                config = PagingConfig(pageSize = 5),initialKey = 0
            ).flow.collect {
                mAdapter.submitData(it)
            }
        }
    }
}