package com.hjl.core.ui.mine

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjl.jetpacklib.mvvm.recycleview.OnItemClickListener
import com.hjl.commonlib.utils.AndroidUtils
import com.hjl.commonlib.utils.DensityUtil
import com.hjl.commonlib.utils.ToastUtil

import com.hjl.core.R
import com.hjl.core.adpter.MavenVersionAdapter
import com.hjl.core.databinding.CoreDialogMavenVersionBinding
import com.hjl.core.net.bean.MavenItemBean
import com.hjl.jetpacklib.mvvm.BaseDialog
import com.hjl.commonlib.extend.addDivider

/**
 * author: long
 * description please add a description here
 * Date: 2021/5/28
 */
class MavenVersionDialog(context: Context,val data : Map.Entry<String, List<MavenItemBean.MavenVersionBean>>) :  BaseDialog<CoreDialogMavenVersionBinding>(context){

    lateinit var mAdapter : MavenVersionAdapter

    override fun getLayoutId(): Int {
        return R.layout.core_dialog_maven_version
    }

    override fun initView() {
        super.initView()

        mAdapter = MavenVersionAdapter().also {
            it.setOnItemClickListener(object : OnItemClickListener<MavenItemBean.MavenVersionBean> {
                override fun onItemClick(
                    position: Int,
                    view: View,
                    bean: MavenItemBean.MavenVersionBean
                ) {
                    AndroidUtils.setPrimaryClip(context,bean.content)
                    ToastUtil.show("gradle 依赖已复制至粘贴板")
                }

            })
        }
        mAdapter.setNewData(data.value)
        if (data.value.size > 10){
            setShowSize(window,DensityUtil.dp2px(300F),DensityUtil.dp2px(350F))
        }else{
            setShowSize(window,DensityUtil.dp2px(300F),ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        binding.coreMavenTitleTv.text = data.key
        binding.coreMavenVersionRv.apply {

            addDivider(15F,15F,false)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

        }
    }

    override fun initData() {
        super.initData()

        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }
}