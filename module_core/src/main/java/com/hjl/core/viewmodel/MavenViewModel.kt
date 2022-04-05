package com.hjl.core.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hjl.core.net.bean.MavenItemBean
import com.hjl.core.repository.MavenRepository
import com.hjl.jetpacklib.mvvm.BaseViewModel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

/**
 * author: long
 * description please add a description here
 * Date: 2021/5/14
 */
class MavenViewModel : BaseViewModel() {

    private val repository = MavenRepository()

    val googlePackageList = MutableLiveData<List<String>>()
    val googleMavenList = MutableLiveData<List<MavenItemBean>>()

    // flow学习 https://mp.weixin.qq.com/s/e7JQ8DWljSuW-Sfd8biLZQ
    val searchKeyChannel = ConflatedBroadcastChannel<String>()
    var searchTipFlow : Flow<List<String>>? = null

    fun getGoogleMavenList(){
        launch({
            googlePackageList.postValue(repository.getGoogleMavenPackage())

            searchTipFlow = searchKeyChannel.asFlow().map { key ->
                googlePackageList.value?.filter {
                    it.contains(key)
                }?.toList() ?: emptyList()
            }
        })
    }

    fun searchMaven(key : String){

        launch({
            googleMavenList.postValue(repository.searchGoogleMaven(key))
        })

    }

    override fun onCleared() {
        super.onCleared()
        searchKeyChannel.close()
    }
}