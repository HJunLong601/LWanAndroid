package com.hjl.core.repository.paging

import androidx.paging.PagingState
import com.hjl.jetpacklib.mvvm.view.BasePagingSource
import com.hjl.core.net.await
import com.hjl.core.net.bean.CollectItemBean
import com.hjl.core.net.coreApiServer
import com.hjl.core.utils.CACHE_COLLECT_LIST

/**
 * Author : long
 * Description :
 * Date : 2020/9/5
 */
class CollectArticlePagingSource() : BasePagingSource<Int, CollectItemBean.CollectItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectItemBean.CollectItem> {
        return try {
            val bean = coreApiServer.getCollectList(params.key!!).await(CACHE_COLLECT_LIST)
            LoadResult.Page(bean.datas,null,if (bean.isOver) null else (bean.curPage + 1))
        }catch (e : Exception){
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, CollectItemBean.CollectItem>): Int? {
        return 0
    }
}