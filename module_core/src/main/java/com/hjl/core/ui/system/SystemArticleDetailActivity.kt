package com.hjl.core.ui.system

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.hjl.commonlib.adapter.LazyFragmentStateAdapter
import com.hjl.commonlib.constant.Constant
import com.hjl.commonlib.utils.TabLayoutUtils
import com.hjl.core.R
import com.hjl.core.databinding.CoreActivitySystemArticleDetailBinding
import com.hjl.core.net.bean.SystemListBean
import com.hjl.jetpacklib.mvvm.view.BaseActivity

class SystemArticleDetailActivity : BaseActivity<CoreActivitySystemArticleDetailBinding>() {

    lateinit var bean: SystemListBean
    private val titleList = ArrayList<String>()
    private val fragmentList = ArrayList<Fragment>()
    var index = 0

    override fun initData() {
        bean = intent.getSerializableExtra(Constant.INTENT_KEY01) as SystemListBean
        index = intent.getIntExtra(Constant.INTENT_KEY02,0)

        bean.children.forEach {
            titleList.add(it.name)
            fragmentList.add(SystemDetailFragment.getInstance(it.id))
        }

    }

    override fun initView() {
        binding.coreTitleLl.titleCenterTv.text = bean.name
        binding.coreSystemVp.apply {
            adapter = LazyFragmentStateAdapter(this@SystemArticleDetailActivity,fragmentList)
            offscreenPageLimit = fragmentList.size
            setCurrentItem(index,false)
            
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        TabLayoutMediator( binding.coreSystemTab,binding.coreSystemVp){ tab,index ->
            tab.text = titleList[index]
        }.attach()

        TabLayoutUtils.setVerticalTabIndicator(binding.coreSystemTab,10)
    }

    override fun initLoad() {

    }

    override fun getLayoutId(): Int {
        return R.layout.core_activity_system_article_detail
    }

    companion object{
        fun startSystemArticleDetailActivity(context : Context, dataBean : SystemListBean, index : Int){
            val intent = Intent(context,SystemArticleDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(Constant.INTENT_KEY01,dataBean)
            bundle.putInt(Constant.INTENT_KEY02,index)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}