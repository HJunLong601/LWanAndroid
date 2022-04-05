package com.hjl.core.viewmodel

import android.util.LruCache
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.hjl.commonlib.utils.SpUtils
import com.hjl.core.net.bean.CommonlyWebBean
import com.hjl.jetpacklib.mvvm.BaseViewModel
import com.hjl.core.net.bean.HomeSearchHotKeyBean
import com.hjl.core.repository.HomeSearchRepository
import com.hjl.core.utils.Constant

/**
 * author: long
 * description please add a description here
 * Date: 2020/9/29
 */
class HomeSearchViewModel : BaseViewModel() {

    private val repository = HomeSearchRepository()

    val hotKeyList = MutableLiveData<List<HomeSearchHotKeyBean>>()
    val commonlyWebList = MutableLiveData<List<CommonlyWebBean>>()
    val historyList = MutableLiveData<List<String>>() // 当前显示的列表

    var historyData: StringBuffer = StringBuffer(SpUtils.getData(Constant.SP_KEY_SEARCH_HISTORY)) // 真实数据

    fun getHotKey(){
        launch({
            hotKeyList.postValue(repository.getHotKey())
        })
    }

    fun getCommonlyWeb(){
        launch({
            commonlyWebList.postValue(repository.getWebCommonly())
        })
    }

    fun getHistorySearch(isShort : Boolean = true){
        val datas = historyData.toString().split(Constant.ITEM_SEPARATOR).filter { it.isNotEmpty() }
        if (isShort && datas.size > 3){
            historyList.postValue(datas.toMutableList().subList(0,3))
        }else{
            historyList.postValue(datas)
        }

    }

    fun saveSearchHistory(item : String){

        val data = historyList.value?.toMutableList()
        data?.add(0,item)

        if (data?.size!! > 10){
            data.subList(0,10)
        }
        // update local list
        historyList.postValue(data)

        // save to SP
        saveHistorySp(data)
    }

    private fun saveHistorySp(data: MutableList<String>) {
        if (data.size == 0){
            clearHistory()
            return
        }

        historyData = StringBuffer()
        data.forEach {
            historyData.append(it)
            historyData.append(Constant.ITEM_SEPARATOR)
        }
        SpUtils.saveData(Constant.SP_KEY_SEARCH_HISTORY, historyData.toString())
    }

    fun deleteHistory(item : String){
        val datas = historyData.split(Constant.ITEM_SEPARATOR).filter { it.isNotEmpty() }.toMutableList()

        if (datas.isNullOrEmpty()) return
        datas.remove(item)
        saveHistorySp(datas)
    }

    fun clearHistory(){
        SpUtils.saveData(Constant.SP_KEY_SEARCH_HISTORY,"")
    }

    fun getSearchPager() = repository.getSearchPager().cachedIn(viewModelScope)

}