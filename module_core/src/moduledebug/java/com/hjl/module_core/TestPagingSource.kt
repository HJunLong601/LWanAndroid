package com.hjl.module_core

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hjl.commonlib.utils.LogUtils
import com.hjl.core.net.await
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.net.coreApiServer
import java.lang.Exception

/**
 * author: long
 * description please add a description here
 * Date: 2021/4/21
 */
class TestPagingSource : PagingSource<Int, HomeArticleBean.Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeArticleBean.Article> {
        return try {
            val key = params.key ?: 0
            val preKey = if (key > 1) key - 1 else null
            val nextKey = key + 1
            LogUtils.i("loading page $key")
            val data = coreApiServer.getHomeArticleList(key).await()
            LoadResult.Page(data.datas,preKey,nextKey)
        }catch (e : Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, HomeArticleBean.Article>): Int? = null
}