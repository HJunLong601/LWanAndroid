package com.hjl.core.ui.main


import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hjl.commonlib.adapter.LazyFragmentStateAdapter
import com.hjl.commonlib.base.ResourceManager
import com.hjl.core.R
import com.hjl.core.databinding.CoreFragmentMainBinding
import com.hjl.core.databinding.CoreFragmentMainTabBinding
import com.hjl.core.ui.mine.MineFragment
import com.hjl.core.ui.system.HomeSystemFragment
import com.hjl.jetpacklib.mvvm.view.BaseFragment2

/**
 * author: long
 * description please add a description here
 * Date: 2021/4/2
 */
class MainFragment : BaseFragment2<CoreFragmentMainBinding>() {
    override fun initLayoutResID(): Int {
        return R.layout.core_fragment_main
    }

    override fun initData() {




    }

    override fun initView() {

        val fragments = arrayListOf<Fragment>(
            HomeFragment(),
            WenDaFragment(),
            HomeSystemFragment(),
            MineFragment()
        )

        binding.fragmentMainVp.adapter = LazyFragmentStateAdapter(this,fragments)
        binding.fragmentMainVp.offscreenPageLimit = fragments.size
        binding.fragmentMainVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

//        fragment_main_vp.isUserInputEnabled = false

        val tabList = arrayOf("首页","问答","体系","我的")
        val tabSelectedIconList = arrayOf(
            R.drawable.core_icon_home,
            R.drawable.core_icon_wenda,
            R.drawable.core_icon_book,
            R.drawable.core_icon_mine)

        val tabIconList = arrayOf(
            R.drawable.core_icon_home_grey,
            R.drawable.core_icon_wenda_gray,
            R.drawable.core_icon_book_grey,
            R.drawable.core_icon_mine_grey
        )

        binding.fragmentMainTl.setSelectedTabIndicatorColor(ResourceManager.getInstance().getColor(context,R.color.common_transparent))
        binding.fragmentMainTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    val tabBinding = DataBindingUtil.bind<CoreFragmentMainTabBinding>(tab.customView!!)
                    tabBinding?.mainTabIv?.setImageResource(tabIconList[tab.position])
                    tabBinding?.mainTabTv?.setTextColor(ResourceManager.getInstance().getColor(context,R.color.common_gray))
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val tabBinding = DataBindingUtil.bind<CoreFragmentMainTabBinding>(tab.customView!!)
                    tabBinding?.mainTabIv?.setImageResource(tabSelectedIconList[tab.position])
                    tabBinding?.mainTabTv?.setTextColor(ResourceManager.getInstance().getColor(context,R.color.common_base_theme_color))

//                    if (it.position == 3){
//                        SkinCompatManager.getInstance().loadSkin("night.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
//                    }
                }
            }

        })

        TabLayoutMediator(binding.fragmentMainTl,binding.fragmentMainVp,true,false){ tab,index ->
             val tabBinding = DataBindingUtil.inflate<CoreFragmentMainTabBinding>(
                LayoutInflater.from(mContext),
                R.layout.core_fragment_main_tab,
                null,
                false
            )
            tab.customView = tabBinding.root
            tabBinding.mainTabTv.text =  tabList[index]
            tabBinding.mainTabIv.setImageResource(tabIconList[index])

        }.attach()
    }

    override fun loadData() {

    }
}