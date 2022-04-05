package com.hjl.core.repository.paging

import androidx.paging.PagingState
import com.hjl.core.net.await
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.net.coreApiServer
import com.hjl.core.ui.main.HomeSearchActivity
import com.hjl.jetpacklib.mvvm.view.BasePagingSource

/**
 * author: long
 * description please add a description here
 * Date: 2020/9/30
 */
class HomeSearchResultPagingSource : BasePagingSource<Int, HomeArticleBean.Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeArticleBean.Article> {
        return try {
            val index:Int = params.key ?: 0
            val data = coreApiServer.search(index, HomeSearchActivity.searchKey).await()
            LoadResult.Page(data.datas, null, if (data.isOver) null else (index + 1))
        }catch (e : Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, HomeArticleBean.Article>): Int? = null

}