package com.hjl.core.repository.paging

import androidx.paging.PagingState
import com.hjl.core.net.await
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.net.coreApiServer
import com.hjl.jetpacklib.mvvm.view.BasePagingSource

/**
 * Author : long
 * Description : 体系点击下具体的文章
 * Date : 2021/5/6
 */
class HomeSystemPagingSource(private val courseId : Int) : BasePagingSource<Int, HomeArticleBean.Article>(){
    override fun getRefreshKey(state: PagingState<Int, HomeArticleBean.Article>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeArticleBean.Article> {
        return try {
            val key = params.key ?: 0
            val preKey = if (key > 1) key - 1 else null
            val data = coreApiServer.getSystemArticle(key, courseId).await()
            LoadResult.Page(data.datas,preKey,if (data.isOver) null else (key + 1))
        }catch (e : Exception){
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}