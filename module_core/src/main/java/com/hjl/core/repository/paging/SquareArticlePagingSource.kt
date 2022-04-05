package com.hjl.core.repository.paging

import androidx.paging.PagingState
import com.hjl.core.net.await
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.net.coreApiServer
import com.hjl.jetpacklib.mvvm.view.BasePagingSource

/**
 * author: long
 * description 获取广场文章列表
 * Date: 2021/4/29
 */
class SquareArticlePagingSource : BasePagingSource<Int, HomeArticleBean.Article>() {

    override fun getRefreshKey(state: PagingState<Int, HomeArticleBean.Article>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeArticleBean.Article> {
        return try {
            val key = params.key ?: 0
            val preKey = if (key > 1) key - 1 else null

            val data = coreApiServer.getSquareArticle(key).await()
            LoadResult.Page(data.datas,preKey,if (data.isOver) null else (key + 1))
        }catch (e : Exception){
            LoadResult.Error(e)
        }

    }
}