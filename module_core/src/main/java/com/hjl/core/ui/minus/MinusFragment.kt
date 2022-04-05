package com.hjl.core.ui.minus

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjl.jetpacklib.mvvm.recycleview.OnItemChildClickListener
import com.hjl.commonlib.utils.SpUtils
import com.hjl.commonlib.utils.ToastUtil
import com.hjl.core.R
import com.hjl.core.adpter.ArticleAdapter
import com.hjl.core.databinding.CoreFragmentMinusBinding
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.viewmodel.HomeViewModel
import com.hjl.commonlib.extend.addDivider
import com.hjl.jetpacklib.mvvm.view.BaseMVVMFragment2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * author: long
 * description 负一 广场页
 * Date: 2021/4/29
 */
class MinusFragment : BaseMVVMFragment2<CoreFragmentMinusBinding,HomeViewModel>() {

    lateinit var articleAdapter : ArticleAdapter


    override fun initLayoutResID(): Int {
        return R.layout.core_fragment_minus
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

    override fun initView() {
        showLoading()
        binding.coreMinusRv.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(mContext)
            addDivider()
        }
        articleAdapter.bindRefreshLayout(binding.coreMinusRefresh)
        binding.coreMinusTitle.titleCenterTv.text = "广场"
        binding.coreMinusTitle.titleLeftIv
    }

    override fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getSquarePagingData().collect{
                showComplete()
                articleAdapter.submitData(it)
            }
        }
    }
}