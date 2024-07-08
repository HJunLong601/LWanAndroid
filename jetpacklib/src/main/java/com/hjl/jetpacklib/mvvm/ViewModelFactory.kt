package com.hjl.jetpacklib.mvvm


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Description 初始化ViewModel
 * Date 2020/3/3 13:04
 * created by long
 */

/**
 * 在Activity中初始化viewModel
 */
fun <BVM : BaseViewModel> initViewModel(
        vmClass: Class<BVM>,
        rClass: Class<out BaseRepository>
) = object : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return vmClass.getConstructor(rClass).newInstance(rClass.newInstance()) as T
    }}.create(vmClass)
