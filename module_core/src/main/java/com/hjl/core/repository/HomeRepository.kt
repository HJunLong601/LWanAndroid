package com.hjl.core.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hjl.jetpacklib.mvvm.BaseRepository
import com.hjl.core.net.await
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.net.coreApiServer
import com.hjl.core.repository.paging.HomeArticlePagingSource
import com.hjl.core.repository.paging.HomeSystemPagingSource
import com.hjl.core.repository.paging.HomeWendaPagingSource
import com.hjl.core.repository.paging.SquareArticlePagingSource
import com.hjl.core.utils.CACHE_BANNER
import kotlinx.coroutines.flow.Flow


/**
 * @author: long
 * @description Repository for HomeFragment
 * @Date: 2020/6/3
 */
class HomeRepository : BaseRepository() {

    suspend fun getHomeBanner() = coreApiServer.getBanner().await(CACHE_BANNER)

    suspend fun collectArticle(id : Int) = coreApiServer.collectArticle(id).await()

    suspend fun removeCollect(id: Int) = coreApiServer.removeCollect(id).await()

    suspend fun getSystemList() = coreApiServer.getSystemTree()

    suspend fun getNaviList() = coreApiServer.getNaviTree()

    fun getWendaPager() = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { HomeWendaPagingSource() }
    ).flow


    fun getHomeArticlePager() = Pager(
    config = PagingConfig(pageSize = 20),
    pagingSourceFactory = { HomeArticlePagingSource() }
    ).flow

    fun getSystemArticlePager(courseId : Int) = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { HomeSystemPagingSource(courseId) }
    ).flow

    fun getSquareArticlePager() : Flow<PagingData<HomeArticleBean.Article>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { SquareArticlePagingSource() }
        ).flow
    }


}