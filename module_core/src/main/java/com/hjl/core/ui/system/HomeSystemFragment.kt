package com.hjl.core.ui.system

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.hjl.commonlib.adapter.LazyFragmentStateAdapter
import com.hjl.commonlib.utils.LogUtils
import com.hjl.core.R
import com.hjl.core.databinding.CoreFragmentSystemMainBinding
import com.hjl.jetpacklib.mvvm.view.BaseFragment2

/**
 * author: long
 * description please add a description here
 * Date: 2021/5/12
 */
class HomeSystemFragment : BaseFragment2<CoreFragmentSystemMainBinding>() {
    override fun initLayoutResID(): Int {
        return R.layout.core_fragment_system_main
    }

    override fun initData() {

    }

    override fun initView() {

        val titles = arrayOf("体系", "导航")
        val fragments = arrayListOf<Fragment>(SystemListFragment(), SystemNavigationFragment())

        binding.coreSystemMainVp.apply {
            adapter = LazyFragmentStateAdapter(mContext,fragments)
            offscreenPageLimit = fragments.size
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        TabLayoutMediator(binding.coreSystemMainTb,binding.coreSystemMainVp){ tab,index ->
            tab.text = titles[index]
        }.attach()
    }

    override fun loadData() {
        LogUtils.i("HomeSystemFragment loadData")
    }
}