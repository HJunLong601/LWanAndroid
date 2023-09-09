package com.hjl.core.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hjl.commonlib.utils.LogUtils
import com.hjl.commonlib.utils.ToastUtil
import com.hjl.jetpacklib.mvvm.recycleview.OnItemChildClickListener
import com.hjl.jetpacklib.mvvm.recycleview.OnItemClickListener
import kotlinx.coroutines.*
import java.lang.reflect.ParameterizedType

/**
 * Author : long
 * Description :
 * Date : 2020/7/25
 */
abstract class PagingDataAdapter<
        T : Any,
        VH : PagingDataViewHolder<DB>,
        DB : ViewDataBinding>(
        diffCallback: DiffUtil.ItemCallback<T>,
        mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
        workerDispatcher: CoroutineDispatcher = Dispatchers.Default
) : PagingDataAdapter<T,RecyclerView.ViewHolder>(diffCallback,mainDispatcher,workerDispatcher){


    abstract fun getLayoutRes() : Int
    var onItemClickListener : OnItemClickListener<T>? = null
    var onItemChildClickListener : OnItemChildClickListener<T>? = null
    var headLayoutRes : Int = 0
    var headerView : View? = null
    var isNotifiedRefreshDone = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == HOLDER_HEAD){
            headerView = LayoutInflater.from(parent.context).inflate(headLayoutRes, parent, false)
            return HeadViewHolder(headerView!!)
        }

        val binding = DataBindingUtil.inflate<DB>(
                LayoutInflater.from(parent.context),
                getLayoutRes(),
                parent,
                false)

        val holderType = ((javaClass as Class).genericSuperclass as ParameterizedType).actualTypeArguments[1]

        return if (holderType is ParameterizedType){
            val holderClass = holderType.rawType as Class<VH>
            holderClass.getConstructor(ViewDataBinding::class.java).newInstance(binding)
        }else{
            val holderClass = holderType as Class<VH>
            val dataBindingClass = ((javaClass as Class).genericSuperclass as ParameterizedType).actualTypeArguments[2] as Class<DB>
            holderClass.getConstructor(dataBindingClass).newInstance(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PagingDataViewHolder<*> -> {
                val realPosition = if (headLayoutRes != 0) {
                    position - 1
                } else {
                    position
                }
                bindData(holder as VH, realPosition)
                onItemClickListener?.let {
                    holder.setOnItemClickListener(it, getItem(realPosition)!!)
                }
                onItemChildClickListener?.let {
                    holder.setOnItemChildClickListener(it, getItem(realPosition)!!)
                }
            }

            is HeadViewHolder -> {
                bindHeaderView(holder)
            }
        }

    }

    open fun bindHeaderView(holder: HeadViewHolder) {

    }

    fun setOnEmptyData(onEmpty : () -> Unit){
        addLoadStateListener {
            when(it.append){
                is LoadState.NotLoading -> {
                    if (it.append.endOfPaginationReached && itemCount == 0) onEmpty.invoke()
                }
            }
        }
    }

    fun bindRefreshLayout(refreshLayout : SwipeRefreshLayout,onRefreshDone : ()-> Unit = {}){
        refreshLayout.setOnRefreshListener { refresh() }
        addLoadStateListener { loadStates ->
            when(loadStates.refresh){
                is LoadState.Loading ->{
                    LogUtils.i("refresh Loading")
                    refreshLayout.isRefreshing = true
                    isNotifiedRefreshDone = false
                }
                is LoadState.Error ->{
                    refreshLayout.isRefreshing = false
                    ToastUtil.show("Refresh Error")
                }
                is LoadState.NotLoading -> {
                    refreshLayout.isRefreshing = false
                    LogUtils.i("refresh NotLoading")
                    if (!isNotifiedRefreshDone){
                        onRefreshDone.invoke()
                        isNotifiedRefreshDone = true
                    }
                }
            }

            when(loadStates.append){
                is LoadState.Loading ->{

                }
                is LoadState.Error ->{
                    GlobalScope.launch(Dispatchers.IO)  {
                        delay(5000)
                        ToastUtil.show("Append Error")
                        retry()
                    }
                }
                is LoadState.NotLoading -> {
                    LogUtils.i("append NotLoading")
                }
            }
            when (loadStates.prepend){
                is LoadState.Loading ->{

                }
                is LoadState.Error ->{
                    GlobalScope.launch(Dispatchers.IO)  {
                        delay(5000)
                        ToastUtil.show("prepend Error")
                        retry()
                    }
                }
                is LoadState.NotLoading -> {

                }
            }
        }



    }

    abstract fun bindData(holder: VH, position: Int)

    override fun getItemViewType(position: Int): Int {
        if (position == 0 && headLayoutRes != 0){
            return HOLDER_HEAD
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return if (headLayoutRes == 0){
            super.getItemCount()
        }else{
            super.getItemCount() + 1
        }
    }

    companion object{
        const val HOLDER_HEAD = 1
    }


}