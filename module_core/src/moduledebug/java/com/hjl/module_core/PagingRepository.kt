package com.hjl.module_core

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hjl.core.net.bean.HomeArticleBean
import kotlinx.coroutines.flow.Flow

/**
 * author: long
 * description please add a description here
 * Date: 2021/4/21
 */
class PagingRepository {

    fun getPagingData() : Flow<PagingData<HomeArticleBean.Article>>{
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { TestPagingSource()}
        ).flow
    }

}