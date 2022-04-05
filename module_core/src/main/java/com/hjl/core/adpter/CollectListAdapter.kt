package com.hjl.core.adpter

import androidx.recyclerview.widget.DiffUtil
import com.hjl.core.R
import com.hjl.core.base.PagingDataAdapter
import com.hjl.core.base.PagingDataViewHolder
import com.hjl.core.databinding.CoreItemCollectBinding
import com.hjl.core.net.bean.CollectItemBean
import com.hjl.core.net.bean.HomeArticleBean

/**
 * Author : long
 * Description :
 * Date : 2020/9/5
 */
class CollectListAdapter : PagingDataAdapter<CollectItemBean.CollectItem, PagingDataViewHolder<CoreItemCollectBinding>,CoreItemCollectBinding>(diff) {
    override fun getLayoutRes(): Int {
        return R.layout.core_item_collect
    }

    override fun bindData(holder: PagingDataViewHolder<CoreItemCollectBinding>, position: Int) {
        holder.binding.data = getItem(position)
    }

    companion object{

        val diff = object : DiffUtil.ItemCallback<CollectItemBean.CollectItem>(){
            override fun areItemsTheSame(oldItem: CollectItemBean.CollectItem, newItem: CollectItemBean.CollectItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CollectItemBean.CollectItem, newItem: CollectItemBean.CollectItem): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }
}