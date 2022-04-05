package com.hjl.core.ui.main

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSONObject
import com.hjl.jetpacklib.mvvm.recycleview.OnItemChildClickListener
import com.hjl.commonlib.utils.LogUtils
import com.hjl.commonlib.utils.SpUtils
import com.hjl.commonlib.utils.ToastUtil
import com.hjl.core.R
import com.hjl.core.adpter.ArticleAdapter
import com.hjl.core.adpter.SimpleLoadStateAdapter
import com.hjl.core.databinding.CoreFragmentHomeBinding
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.net.bean.HomeBannerBean
import com.hjl.core.utils.CACHE_BANNER
import com.hjl.core.viewmodel.HomeViewModel
import com.hjl.commonlib.extend.addDivider
import com.hjl.commonlib.extend.quickStartActivity
import com.hjl.jetpacklib.mvvm.view.BaseMVVMFragment2
import com.hjl.module_base.CacheUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @author: long
 * @description 首页Fragment
 * @Date: 2020/6/2
 */
class HomeFragment : BaseMVVMFragment2<CoreFragmentHomeBinding, HomeViewModel>(), View.OnClickListener {

    private lateinit var homeArticleAdapter: ArticleAdapter

    private var currentCookie : String = ""

    override fun initLayoutResID(): Int {
        return R.layout.core_fragment_home
    }

    override fun initData() {
        viewModel.bannerData.observe(this, Observer {
            if (it.isNullOrEmpty()) return@Observer
            homeArticleAdapter.setBannerData(it.toMutableList())
        })
    }

    override fun initView() {

        binding.coreTitleLl.apply {
            titleCenterTv.text = "首页"
            titleRightIv.setImageResource(R.drawable.view_icon_search_white)
            titleRightIv.setOnClickListener(this@HomeFragment)
        }


        currentCookie = SpUtils.getCookie()

        homeArticleAdapter = ArticleAdapter(this.context!!).also {

            it.headLayoutRes = R.layout.core_header_banner
            it.bindRefreshLayout(binding.homeRefresh){
                LogUtils.i("refresh done")
                // 解决焦点问题
                binding.homeRv.scrollToPosition(0)
            }
            it.onItemChildClickListener = object :
                OnItemChildClickListener<HomeArticleBean.Article> {
                override fun onItemChildClick(position: Int, view: View, bean: HomeArticleBean.Article) {

                    if (SpUtils.getCookie().isNullOrEmpty()){
                        ToastUtil.show("您尚未登录或登录已过期")
                        return
                    }

                    if (bean.isCollect){
                        viewModel.removeCollect(bean.id)
                        bean.isCollect = false
                        it.notifyDataSetChanged()
                    }else{
                        viewModel.collectArticle(bean.id)
                        bean.isCollect = true
                        it.notifyDataSetChanged()
                    }
                }
            }
        }

        binding.homeRv.apply {
            this.adapter = homeArticleAdapter.withLoadStateFooter(SimpleLoadStateAdapter())
            this.layoutManager = LinearLayoutManager(context)
            addDivider()
        }



        binding.homeRefresh.setOnRefreshListener {
            homeArticleAdapter.refresh()
            LogUtils.i("refresh")
        }

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getHomePagingData().collect{
                LogUtils.i("getData")
                homeArticleAdapter.submitData(it)
            }
        }

        // 先加载缓存的Banner 之后再加载最新的
        CacheUtils.getCache(CACHE_BANNER){
            if (it.isNotEmpty()){
                val data = JSONObject.parseArray(it, HomeBannerBean::class.java)
                LogUtils.i("Banner","get Banner cache :$data")
                lifecycleScope.launch(Dispatchers.Main){ homeArticleAdapter.setBannerData(data)}
                lifecycleScope.launch(Dispatchers.IO) {
                    delay(8000)
                    LogUtils.i("loadBannerData")
                    viewModel.loadBannerData()
                }
            }else{
                LogUtils.i("no cache , loadBannerData")
                viewModel.loadBannerData()
            }
        }
    }

    override fun loadData() {
        binding.vm = viewModel
    }

    override fun onResume() {
        super.onResume()
        if (currentCookie != SpUtils.getCookie()){
            homeArticleAdapter.refresh()
            homeArticleAdapter.notifyDataSetChanged()
            currentCookie = SpUtils.getCookie()
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.title_right_iv ->{
                quickStartActivity(HomeSearchActivity::class.java)
            }
        }
    }
}