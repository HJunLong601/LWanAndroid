package com.hjl.core.ui.system

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjl.commonlib.base.MultipleStatusView
import com.hjl.jetpacklib.mvvm.recycleview.OnItemChildClickListener
import com.hjl.commonlib.constant.Constant
import com.hjl.commonlib.utils.SpUtils
import com.hjl.commonlib.utils.ToastUtil
import com.hjl.core.R
import com.hjl.core.adpter.ArticleAdapter
import com.hjl.core.adpter.SimpleLoadStateAdapter
import com.hjl.core.databinding.CoreFragmentSystemDetailBinding
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.viewmodel.HomeViewModel
import com.hjl.commonlib.extend.addDivider
import com.hjl.jetpacklib.mvvm.view.BaseMVVMFragment2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Author : long
 * Description :
 * Date : 2021/5/9
 */
class SystemDetailFragment : BaseMVVMFragment2<CoreFragmentSystemDetailBinding,HomeViewModel>() {

    lateinit var articleAdapter: ArticleAdapter
    var courseId = 0

    override fun initLayoutResID(): Int {
        return R.layout.core_fragment_system_detail
    }

    override fun initData() {

        articleAdapter = ArticleAdapter(context!!)
        courseId = arguments!!.getInt(Constant.INTENT_KEY01)
    }

    override fun initView() {
        showLoading()
        binding.coreFragmentSystemDetailRv.apply {
            adapter = articleAdapter.withLoadStateFooter(SimpleLoadStateAdapter())
            layoutManager = LinearLayoutManager(context)
            addDivider()
        }

        articleAdapter.apply {
            setOnEmptyData { showEmpty() }
            onItemChildClickListener = object : OnItemChildClickListener<HomeArticleBean.Article> {
                override fun onItemChildClick(position: Int, view: View, bean: HomeArticleBean.Article) {
                    if (SpUtils.getCookie().isNullOrEmpty()){
                        ToastUtil.show("您尚未登录或登录已过期")
                        return
                    }

                    if (bean.isCollect){
                        viewModel.removeCollect(bean.id)
                        bean.isCollect = false
                        notifyDataSetChanged()
                    }else{
                        viewModel.collectArticle(bean.id)
                        bean.isCollect = true
                        notifyDataSetChanged()
                    }
                }
            }


        }
    }

    override fun loadData() {
        lifecycleScope.launch {
//           不会阻塞线程
            delay(8000)
            if(getViewStatus() != MultipleStatusView.STATUS_CONTENT){
                showError()
                ToastUtil.show("status error : ${getViewStatus()}")
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getSystemArticlePager(courseId).collect{
                showComplete()
                articleAdapter.submitData(it)
            }
        }
    }

    companion object{
        fun getInstance(int: Int) : SystemDetailFragment{
            val fragment = SystemDetailFragment()
            val bundle = Bundle().also { it.putInt(Constant.INTENT_KEY01,int) }
            fragment.arguments = bundle
            return fragment
        }
    }
}