package com.hjl.core.ui.mine

import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjl.commonlib.utils.DensityUtil
import com.hjl.commonlib.utils.LogUtils
import com.hjl.commonlib.utils.ToastUtil
import com.hjl.core.R
import com.hjl.core.databinding.CoreActivityMavenBinding
import com.hjl.core.ui.SimpleWebActivity
import com.hjl.core.viewmodel.MavenViewModel
import com.hjl.jetpacklib.databinding.BaseItemSimpleTextBinding
import com.hjl.commonlib.extend.KTAG
import com.hjl.commonlib.extend.addDivider
import com.hjl.jetpacklib.mvvm.recycleview.SimpleTextAdapter
import com.hjl.jetpacklib.mvvm.view.BaseMVVMActivity
import com.jakewharton.rxbinding3.widget.editorActions
import kotlinx.coroutines.flow.collect

class MavenActivity : BaseMVVMActivity<CoreActivityMavenBinding, MavenViewModel>() {

    private lateinit var tipAdapter : SimpleTextAdapter<BaseItemSimpleTextBinding>

    override fun initData() {
        viewModel.getGoogleMavenList()
        viewModel.googlePackageList.observe(this, Observer { allPackageList ->
            LogUtils.d(KTAG,allPackageList.toString())

            lifecycleScope.launchWhenResumed {
                viewModel.searchTipFlow?.collect {
                    LogUtils.d(KTAG,"getFilterList:${it.size}")
                    if (it.isEmpty() || (it.size == 1 && it[0].equals(binding.coreMavenSearchEv.text.toString()))){
                        binding.coreMavenTipRv.visibility = View.GONE
                        return@collect
                    }

                    tipAdapter.setNewData(it)
                    binding.coreMavenTipRv.apply {
                        if (it.size > 4){
                            layoutParams.height = DensityUtil.dp2px(84.0F)
                        }else{
                            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        }
                        visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun initView() {

        addDisposable(binding.coreMavenSearchEv.editorActions().subscribe {
            if (binding.coreMavenSearchEv.text!!.length >= 4){
                MavenSearchResultActivity.startMavenSearchResultActivity(this,binding.coreMavenSearchEv.text.toString())
            }else{
                ToastUtil.show("必须输入至少四个字符的关键字哦~")
            }
        })

        binding.coreMavenSearchEv.doAfterTextChanged {
            if (it.toString().isNotEmpty()){
                viewModel.searchKeyChannel.offer(it.toString())
            }else{
                binding.coreMavenTipRv.visibility = View.GONE
            }
        }


        binding.coreMavenBaiduTv.setOnClickListener {
            SimpleWebActivity.loadUrl(this,"https://www.baidu.com")
        }

        binding.coreMavenGoogleTv.setOnClickListener {
            SimpleWebActivity.loadUrl(this,"https://www.google.com/")
        }

        tipAdapter = SimpleTextAdapter()
        tipAdapter.setOnItemClickListener { position, view, bean ->
            binding.coreMavenSearchEv.setText(
                bean
            )
        }



        binding.coreMavenTipRv.apply {
            layoutManager = LinearLayoutManager(this@MavenActivity)
            adapter = tipAdapter
            addDivider(showEnd = false)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.core_activity_maven
    }

    override fun getStatusBarColor(): Int {
        return R.color.common_white
    }
}
