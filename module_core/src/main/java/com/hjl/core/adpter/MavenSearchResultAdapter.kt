package com.hjl.core.adpter

import android.content.Context
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter
import com.donkingliang.groupedadapter.holder.BaseViewHolder
import com.hjl.commonlib.base.BaseApplication
import com.hjl.core.R
import com.hjl.core.databinding.CoreItemMavenChildBinding
import com.hjl.core.databinding.CoreItemMavenGroupBinding
import com.hjl.core.net.bean.MavenItemBean

/**
 * author: long
 * description please add a description here
 * Date: 2021/5/14
 */
class MavenSearchResultAdapter(context: Context,val datalist : MutableList<MavenItemBean>) :
    GroupedRecyclerViewAdapter(context,true) {


    fun setNewData(data : MutableList<MavenItemBean>){
        BaseApplication.runOnUIThread {
            datalist.clear()
            datalist.addAll(data)
            notifyDataChanged()
        }
    }

    override fun getGroupCount(): Int {
        return datalist.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return datalist[groupPosition].artifactList.size
    }

    override fun getHeaderLayout(viewType: Int): Int {
        return R.layout.core_item_maven_group
    }

    override fun getFooterLayout(viewType: Int): Int {
        return -1
    }

    override fun hasFooter(groupPosition: Int): Boolean {
        return false
    }

    override fun hasHeader(groupPosition: Int): Boolean {
        return true
    }

    override fun onBindFooterViewHolder(holder: BaseViewHolder?, groupPosition: Int) {

    }

    override fun onBindHeaderViewHolder(holder: BaseViewHolder?, groupPosition: Int) {
        holder!!.getBinding<CoreItemMavenGroupBinding>().coreItemMavenGroupTv.text = datalist[groupPosition].groupName
    }

    override fun onBindChildViewHolder(
        holder: BaseViewHolder?,
        groupPosition: Int,
        childPosition: Int
    ) {
        holder!!.getBinding<CoreItemMavenChildBinding>().coreMavenItemChildTv.text =
            datalist[groupPosition].artifactList[childPosition].key
        holder.getBinding<CoreItemMavenChildBinding>().coreMavenItemCountTv.text =
            datalist[groupPosition].artifactList[childPosition].value.size.toString()
    }

    override fun getChildLayout(viewType: Int): Int {
        return R.layout.core_item_maven_child
    }
}