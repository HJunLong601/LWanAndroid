package com.hjl.core.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hjl.core.R
import com.hjl.core.databinding.CoreItemLoadMoreBinding

/**
 * Author : long
 * Description :
 * Date : 2020/6/25
 */
class SimpleLoadStateAdapter : LoadStateAdapter<LoadStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = DataBindingUtil.inflate<CoreItemLoadMoreBinding>(
                LayoutInflater.from(parent.context),
                R.layout.core_item_load_more,
                parent,
                false
        )
        return LoadStateViewHolder(binding)
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return super.displayLoadStateAsItem(loadState)
//        return true
    }
}

class LoadStateViewHolder(binding: CoreItemLoadMoreBinding) : RecyclerView.ViewHolder(binding.root){

    val isLoading = ObservableBoolean(true)
    val isEnd = ObservableBoolean(false)

    init {
        binding.holder = this
    }

    fun bindState(loadState: LoadState){
        isLoading.set(loadState is LoadState.Loading)
        isEnd.set(loadState.endOfPaginationReached)
    }
}