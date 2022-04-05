package com.hjl.module_core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hjl.core.R
import com.hjl.core.databinding.ItemTestPagingBinding
import com.hjl.core.net.bean.HomeArticleBean

/**
 * author: long
 * description please add a description here
 * Date: 2021/4/21
 */
class TestPagingAdapter : PagingDataAdapter<HomeArticleBean.Article,TestPagingAdapter.TestPagingViewHolder>(diff) {



    override fun onBindViewHolder(holder: TestPagingViewHolder, position: Int) {
        holder.bind(getItem(position)!!.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestPagingViewHolder {

        val binding = DataBindingUtil.inflate<ItemTestPagingBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_test_paging,
            parent,
            false
        )
        return TestPagingViewHolder(binding)
    }


    class TestPagingViewHolder(val binding : ItemTestPagingBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(title : String){
            binding.testItemTitle.text = title
        }
    }

    companion object{
        val diff = object : DiffUtil.ItemCallback<HomeArticleBean.Article>(){
            override fun areItemsTheSame(oldItem: HomeArticleBean.Article, newItem: HomeArticleBean.Article): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HomeArticleBean.Article, newItem: HomeArticleBean.Article): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }


}