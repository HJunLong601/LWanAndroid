package com.hjl.core.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hjl.jetpacklib.mvvm.BaseRepository
import com.hjl.core.net.await
import com.hjl.core.net.coreApiServer
import com.hjl.core.repository.paging.HomeSearchResultPagingSource
import com.hjl.core.utils.CACHE_HOME_HOT_KEY
import com.hjl.core.utils.CACHE_HOME_WEB_COMMONLY

/**
 * author: long
 * description please add a description here
 * Date: 2020/9/29
 */
class HomeSearchRepository : BaseRepository() {


    suspend fun getHotKey() = coreApiServer.getHotKey().await(CACHE_HOME_HOT_KEY)

    suspend fun getWebCommonly() = coreApiServer.getCommonlyWeb().await(CACHE_HOME_WEB_COMMONLY)

    fun getSearchPager() = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { HomeSearchResultPagingSource() }
    ).flow

}