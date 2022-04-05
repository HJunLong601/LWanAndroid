package com.hjl.module_core

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjl.commonlib.utils.LogUtils
import com.hjl.core.R
import com.hjl.core.databinding.ActivityPagingTestBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PagingTestActivity : AppCompatActivity() {

    lateinit var binding: ActivityPagingTestBinding

    private val viewModel by lazy { ViewModelProvider(this).get(PagingViewModel::class.java) }
    private val adapter  = TestPagingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_paging_test
        )
        binding.lifecycleOwner = this

        binding.testRv.layoutManager = LinearLayoutManager(this)
        binding.testRv.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO)  {
            viewModel.getPagingData().collect {
                LogUtils.i("get data")
                adapter.submitData(it)
            }
        }

        binding.testRefresh.setOnRefreshListener {
            LogUtils.i("refresh")
            adapter.refresh()
        }

        adapter.addLoadStateListener {
            when(it.refresh){
                is LoadState.Loading -> {

                }
                is LoadState.NotLoading -> {
                    binding.testRefresh.isRefreshing = false
                }
                is LoadState.Error -> {

                }
            }
        }

    }
}
