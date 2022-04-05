package com.hjl.module_core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hjl.core.net.bean.HomeArticleBean
import kotlinx.coroutines.flow.Flow

/**
 * author: long
 * description please add a description here
 * Date: 2021/4/21
 */
class PagingViewModel : ViewModel() {

    val repository = PagingRepository()

    fun getPagingData() : Flow<PagingData<HomeArticleBean.Article>>{
        return repository.getPagingData().cachedIn(viewModelScope)
    }

}