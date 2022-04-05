package com.hjl.core.base

import android.view.View
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.hjl.jetpacklib.mvvm.recycleview.OnItemChildClickListener
import com.hjl.jetpacklib.mvvm.recycleview.OnItemClickListener

/**
 * Author : long
 * Description :
 * Date : 2020/7/25
 */
open class PagingDataViewHolder<DB : ViewDataBinding>(val binding: DB) : RecyclerView.ViewHolder(binding.root) {


    private var childIdClickList = ArrayList<Int>()


    fun <T> setOnItemClickListener(onItemClickListener: OnItemClickListener<T>, bean : T){
        binding.root.setOnClickListener {
            onItemClickListener.onItemClick(bindingAdapterPosition,it,bean)
        }
    }

    fun <T> setOnItemChildClickListener(onItemChildClickListener: OnItemChildClickListener<T>, bean : T) {
        for (id in childIdClickList) {
            val view: View? = itemView.findViewById(id)
            view?.setOnClickListener {
                onItemChildClickListener.onItemChildClick(bindingAdapterPosition,it,bean)
            }
        }
    }



    open fun addChildClick(@IdRes id: Int) {
        if(!childIdClickList.contains(id)){
            childIdClickList.add(id)
        }
    }


}