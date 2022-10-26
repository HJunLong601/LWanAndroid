package com.hjl.skin

import android.graphics.Color
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hjl.commonlib.extend.addDivider


import com.hjl.jetpacklib.mvvm.view.BaseActivity
import com.hjl.module_base.router.RouterName
import com.hjl.skin.databinding.SkinActivitySkinBinding
import skin.support.SkinCompatManager

@Route(path = RouterName.SKIN_SKIN_ACTIVITY)
class SkinActivity : BaseActivity<SkinActivitySkinBinding>()  {


    private lateinit var skinAdapter : SkinAdapter
    val skinList = mutableListOf<SkinItemBean>()
    val spanCount = 3

    override fun initData() {
        skinAdapter = SkinAdapter()
        skinAdapter.setOnItemClickListener { position, view, bean ->

            if (bean.skinName.isEmpty()) return@setOnItemClickListener

            if (bean.skinName.contains("blue")){
                SkinCompatManager.getInstance().restoreDefaultTheme()
                return@setOnItemClickListener
            }
            SkinCompatManager.getInstance().loadSkin(bean.skinName, SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS)
        }
    }

    override fun initView() {
        binding.baseTitleLayout.titleCenterTv.text = "主题换肤"
        binding.baseSkinRv.apply {
            adapter = skinAdapter
            layoutManager = GridLayoutManager(context,spanCount)
            addDivider(dividerHeight = 2)
        }
    }

    override fun initLoad() {

        skinList.add(SkinItemBean("经典黑",
            "dark.skin",
            backgroundColor = Color.parseColor("#2C2C2C"))
        )

        skinList.add(SkinItemBean("经典蓝",
            "blue.skin",
            backgroundColor = Color.parseColor("#04B5FD"))
        )

        skinList.add(SkinItemBean("豆沙绿",
            "green.skin",
            backgroundColor = Color.parseColor("#C7EDCC"))
        )

        skinList.add(
            SkinItemBean(
                "枫叶黄",
                "yellow.skin",
                backgroundColor = Color.parseColor("#D6D5B7")
            )
        )

        fillSkinList() // 网格线 填充空对象
        skinAdapter.setNewData(skinList)
        setStatusBar()

    }

    private fun fillSkinList(){

        if (skinList.size % spanCount != 0){

            var emptySize = spanCount - skinList.size % spanCount
            while (emptySize > 0){
                skinList.add(SkinItemBean("","",backgroundColor = Color.TRANSPARENT))
                emptySize--
            }
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.skin_activity_skin
    }

}