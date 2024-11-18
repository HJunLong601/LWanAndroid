package com.hjl.core.ui.mine


import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjl.commonlib.base.BaseApplication
import com.hjl.commonlib.constant.Constant
import com.hjl.commonlib.extend.addDivider
import com.hjl.commonlib.utils.LogUtils
import com.hjl.core.R
import com.hjl.core.adpter.MavenSearchResultAdapter
import com.hjl.core.databinding.CoreActivityMavenSearchResultBinding
import com.hjl.core.viewmodel.MavenViewModel
import com.hjl.jetpacklib.mvvm.view.BaseMVVMActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MavenSearchResultActivity : BaseMVVMActivity<CoreActivityMavenSearchResultBinding, MavenViewModel>() {

    private lateinit var resultAdapter: MavenSearchResultAdapter

    override fun getLayoutId(): Int {
        return R.layout.core_activity_maven_search_result
    }

    override fun initData() {
        val key = intent.getStringExtra(Constant.INTENT_KEY01)
        LogUtils.i("start Search")
        binding.coreMavenTitle.titleCenterTv.text =
            BaseApplication.getApplication().getString(R.string.query_results)
        viewModel.searchMaven(key!!)

        viewModel.googleMavenList.observe(this, Observer {
            if (it.isNotEmpty()){
                showComplete()
                resultAdapter.setNewData(it.toMutableList())
            }else{
                showEmpty()
            }
        })
    }

    override fun initView() {
        showLoading()
        resultAdapter = MavenSearchResultAdapter(this, ArrayList())
        resultAdapter.setOnChildClickListener { adapter, holder, groupPosition, childPosition ->
            MavenVersionDialog(this,resultAdapter.datalist[groupPosition].artifactList[childPosition]).show()
        }

        binding.coreMavenResultRv.apply {
            adapter = resultAdapter
            layoutManager = LinearLayoutManager(context)
            addDivider()
        }
    }



    companion object{

        fun startMavenSearchResultActivity(context: Context,key : String){
            val intent = Intent(context,MavenSearchResultActivity::class.java).also { it.putExtra(Constant.INTENT_KEY01,key) }
            context.startActivity(intent)
        }

    }




}
