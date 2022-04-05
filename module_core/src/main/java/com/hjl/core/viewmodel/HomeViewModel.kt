package com.hjl.core.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.hjl.jetpacklib.mvvm.BaseViewModel
import com.hjl.commonlib.utils.LogUtils
import com.hjl.core.net.await
import com.hjl.core.net.bean.SystemListBean
import com.hjl.core.net.bean.HomeArticleBean
import com.hjl.core.net.bean.HomeBannerBean
import com.hjl.core.net.bean.NavigationListBean
import com.hjl.core.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

/**
 * @author: long
 * @description 文章、首页相关
 * @Date: 2020/6/3
 */
class HomeViewModel : BaseViewModel() {

    private val repository = HomeRepository()



    val bannerData = MutableLiveData<List<HomeBannerBean>>()
    val systemListData = MutableLiveData<List<SystemListBean>>()
    val naviListData = MutableLiveData<List<NavigationListBean>>()


    fun loadBannerData(){
        launch({
            val dataList= arrayListOf(HomeBannerBean("",""))
            dataList.addAll(repository.getHomeBanner())
            bannerData.postValue(dataList)
        },{
            LogUtils.e(TAG,it.errorMessage)
        })
    }

    fun collectArticle(id : Int){
        launch({
            repository.collectArticle(id)
        })
    }

    fun removeCollect(id : Int) = launch({
        repository.removeCollect(id)
    })

    /**
     * 体系目录
     */
    fun getSystemList() = launch({
        systemListData.postValue(repository.getSystemList().await())
    })

    /**
     * 导航目录
     */
    fun getNaviList() = launch({
        naviListData.postValue(repository.getNaviList().await())
    })



    /**
     * 体系子项具体文章
     */
    fun getSystemArticlePager(courseId : Int) = repository.getSystemArticlePager(courseId).cachedIn(viewModelScope)

    /**
     * 负一广场页
     */
    fun getSquarePagingData() = repository.getSquareArticlePager().cachedIn(viewModelScope)

    fun getHomePagingData() = repository.getHomeArticlePager().cachedIn(viewModelScope)

    fun getWendaPagingData() = repository.getWendaPager().cachedIn(viewModelScope)




}