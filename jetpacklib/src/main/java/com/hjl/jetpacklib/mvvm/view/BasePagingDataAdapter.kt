package com.hjl.jetpacklib.mvvm.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Author : long
 * Description :
 * Date : 2020/7/6
 */
abstract class BasePagingDataAdapter<T : Any, VB : ViewDataBinding>(
        diffCallback: DiffUtil.ItemCallback<T>,
        mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
        workerDispatcher: CoroutineDispatcher = Dispatchers.Default
) : PagingDataAdapter<T, BasePagingDataAdapter.BaseViewHolder<VB>>(diffCallback,mainDispatcher,workerDispatcher){

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        onBindViewHolder(getItem(position)!!,holder.binding)
    }

    abstract fun onBindViewHolder(data: T, binding: VB)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val binding = DataBindingUtil.inflate<VB>(
                LayoutInflater.from(parent.context),
                getLayoutId(),
                parent,
                false)
        return BaseViewHolder(binding)
    }

    @LayoutRes
    protected abstract fun getLayoutId() : Int


    class BaseViewHolder<VB : ViewDataBinding>(var binding: VB) : RecyclerView.ViewHolder(binding.root){

    }

}