package com.hjl.core.repository.paging

import android.util.Log
import androidx.paging.PagingState
import com.alibaba.fastjson.JSONObject
import com.hjl.commonlib.utils.LogUtils
import com.hjl.jetpacklib.mvvm.view.BasePagingSource
import com.hjl.core.net.await
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.net.coreApiServer
import com.hjl.core.utils.CACHE_ARTICLE_PAGE
import com.hjl.core.utils.CACHE_ARTICLE_TOP
import com.hjl.module_base.CacheUtils
import kotlin.Exception

/**
 * author: long
 * description PagingSource for HomeFragment
 * Date: 2020/6/11
 */
class HomeArticlePagingSource : BasePagingSource<Int, HomeArticleBean.Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeArticleBean.Article> {
        return try {
            val key = params.key ?: 0
            val preKey = if (key > 1) key - 1 else null
            var nextKey : Int?= key + 1
            LogUtils.i("loading page $key")
            if (key == 0){
                val data = coreApiServer.getHomeTopArticle().await()
                for (item in data) {
                    item.isTop = true
                }
                LoadResult.Page(data,preKey,nextKey)
            }else{
                val data = coreApiServer.getHomeArticleList(key - 1).await()
                if (data.isOver) nextKey = null
                LoadResult.Page(data.datas,preKey,nextKey)
            }

        }catch (e : java.lang.Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, HomeArticleBean.Article>): Int? = null
}