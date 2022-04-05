package com.hjl.core.ui.main

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjl.jetpacklib.mvvm.recycleview.OnItemChildClickListener
import com.hjl.commonlib.utils.SpUtils
import com.hjl.commonlib.utils.ToastUtil
import com.hjl.core.R
import com.hjl.core.adpter.ArticleAdapter
import com.hjl.core.databinding.CoreFragmentWendaBinding
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.viewmodel.HomeViewModel
import com.hjl.commonlib.extend.addDivider
import com.hjl.jetpacklib.mvvm.view.BaseMVVMFragment2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Author : long
 * Description :
 * Date : 2021/5/29
 */
class WenDaFragment : BaseMVVMFragment2<CoreFragmentWendaBinding,HomeViewModel>() {

    lateinit var articleAdapter : ArticleAdapter


    override fun initLayoutResID(): Int {
        return R.layout.core_fragment_wenda
    }

    override fun initView() {
        showLoading()
        binding.coreWendaRv.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(mContext)
            addDivider()
        }
        articleAdapter.bindRefreshLayout(binding.coreWendaRefresh)
        binding.coreWendaTitle.titleCenterTv.text = "问答"
        binding.coreWendaTitle.titleLeftIv
    }

    override fun initData() {
        articleAdapter = ArticleAdapter(mContext).also {
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
            it.setOnEmptyData { showEmpty() }
        }
    }

    override fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getWendaPagingData().collect{
                showComplete()
                articleAdapter.submitData(it)
            }
        }
    }




}