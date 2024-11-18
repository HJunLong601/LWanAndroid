package com.hjl.core.ui.main


import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.hjl.commonlib.base.BaseApplication
import com.hjl.commonlib.extend.addDivider
import com.hjl.commonlib.extend.hideKeyboard
import com.hjl.core.R
import com.hjl.core.adpter.ArticleAdapter
import com.hjl.core.adpter.HomeSearchHistoryAdapter
import com.hjl.core.adpter.HomeSearchHotKeyAdapter
import com.hjl.core.adpter.HomeWebCommonlyAdapter
import com.hjl.core.databinding.CoreActivityHomeSearchBinding
import com.hjl.core.utils.Constant
import com.hjl.core.viewmodel.HomeSearchViewModel
import com.hjl.jetpacklib.mvvm.view.BaseMVVMActivity
import com.jakewharton.rxbinding3.widget.editorActions
import com.jakewharton.rxbinding3.widget.textChanges
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeSearchActivity : BaseMVVMActivity<CoreActivityHomeSearchBinding, HomeSearchViewModel>() {


    private val mHotKeyAdapter = HomeSearchHotKeyAdapter()
    private val mHomeWebAdapter  = HomeWebCommonlyAdapter()
    private val mHomeSearchHistoryAdapter = HomeSearchHistoryAdapter()

    private lateinit var mSearchResultAdapter :  ArticleAdapter

    private var isShowShortHistory = true

    override fun getLayoutId(): Int {
        return  R.layout.core_activity_home_search
    }

    override fun initView() {
        super.initView()

        mSearchResultAdapter = ArticleAdapter(this)

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getSearchPager().collect{
                mSearchResultAdapter.submitData(it)
            }
        }

        // 搜索热词
        binding.homeSearchHotkey.apply {
            adapter = mHotKeyAdapter
            layoutManager = FlexboxLayoutManager(context)
            mHotKeyAdapter.setOnItemClickListener { position, view, bean ->
                binding.homeSearchEv.setText(bean.name)
                doSearch()
                //                    showKeyboard(view)
            }
        }

        // 常用网站
        binding.homeSearchCommonlyRv.apply {
            adapter = mHomeWebAdapter
            layoutManager = FlexboxLayoutManager(context)
        }

        // 搜索历史

        mHomeSearchHistoryAdapter.apply {
            setOnItemChildClickListener { position, view, bean ->
                mHomeSearchHistoryAdapter.dataList.remove(bean)
                mHomeSearchHistoryAdapter.notifyDataSetChanged()
                viewModel.deleteHistory(bean)
            }

            setOnItemClickListener { position, view, bean ->
                binding.homeSearchEv.setText(bean)
                doSearch()
            }


        }

        binding.homeSearchHistoryRv.apply {
            adapter = mHomeSearchHistoryAdapter
            layoutManager = LinearLayoutManager(context)
            addDivider(showEnd = true)
        }

        // 搜索栏
        binding.homeSearchCancelTv.setOnClickListener { finish() }
        binding.homeSearchResult.apply {
            visibility = View.GONE
            adapter = mSearchResultAdapter
            layoutManager = LinearLayoutManager(context)
            addDivider()
        }

        //
        binding.homeSearchStateTv.setOnClickListener {
            isShowShortHistory = !isShowShortHistory

            if (isShowShortHistory){
                binding.homeSearchStateTv.visibility = View.GONE
                mHomeSearchHistoryAdapter.setNewData(emptyList())
                viewModel.clearHistory()
            }else{
                binding.homeSearchStateTv.text = BaseApplication.getApplication()
                    .getString(R.string.core_string_clear_search_history)
                viewModel.getHistorySearch(false)
            }
        }
        val datas = viewModel.historyData.split(Constant.ITEM_SEPARATOR)
            .filter { it.isNotEmpty() }
        if (datas.size > 3){
            binding.homeSearchStateTv.visibility = View.VISIBLE
        }

        addDisposable(binding.homeSearchEv.editorActions().subscribe {
            if (binding.homeSearchEv.text!!.isNotEmpty()){
                doSearch()
                hideKeyboard()
            }

            changeMode(binding.homeSearchEv.text!!.isEmpty())
        })

        addDisposable(binding.homeSearchEv.textChanges().map {
            it.toString()
        }.debounce(500, TimeUnit.MILLISECONDS).subscribe {
            if (it.isEmpty()){
                changeMode(true)
            }
        })


    }

    private fun doSearch() {
        searchKey = binding.homeSearchEv.text.toString()
        mSearchResultAdapter.refresh()
        viewModel.saveSearchHistory(searchKey)
        changeMode(false)
    }


    override fun initData() {
        super.initData()

        viewModel.hotKeyList.observe(this, Observer {
            mHotKeyAdapter.setNewData(it)
        })

        viewModel.commonlyWebList.observe(this, Observer {
            mHomeWebAdapter.setNewData(it)
        })

        viewModel.historyList.observe(this, Observer {
            mHomeSearchHistoryAdapter.setNewData(it)
        })

        viewModel.getHotKey()
        viewModel.getCommonlyWeb()
        viewModel.getHistorySearch()

    }

    private fun changeMode(isShowHotKey : Boolean){
        runOnUiThread {
            if (isShowHotKey){
                binding.homeSearchResult.visibility = View.GONE
                binding.homeSearchRecommendSv.visibility = View.VISIBLE
            }else{
                binding.homeSearchResult.visibility = View.VISIBLE
                binding.homeSearchRecommendSv.visibility = View.GONE
            }
        }
    }

    companion object{
        var searchKey : String = ""
    }
}
