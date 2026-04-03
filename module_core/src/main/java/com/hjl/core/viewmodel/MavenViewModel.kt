package com.hjl.core.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hjl.core.net.bean.MavenItemBean
import com.hjl.core.repository.MavenRepository
import com.hjl.jetpacklib.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * author: long
 * description please add a description here
 * Date: 2021/5/14
 */
@HiltViewModel
class MavenViewModel @Inject constructor(val repository : MavenRepository): BaseViewModel() {

    val googlePackageList = MutableLiveData<List<String>>()
    val googleMavenList = MutableLiveData<List<MavenItemBean>>()

    val searchKeyFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val searchTipFlow: Flow<List<String>> = searchKeyFlow.map { key ->
        googlePackageList.value?.filter {
            it.contains(key)
        }?.toList() ?: emptyList()
    }

    fun getGoogleMavenList(){
        launch({
            googlePackageList.postValue(repository.getGoogleMavenPackage())
        })
    }

    fun searchMaven(key : String){

        launch({
            googleMavenList.postValue(repository.searchGoogleMaven(key))
        })

    }
}
