package com.hjl.jetpacklib.mvvm.view

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.hjl.jetpacklib.mvvm.BaseRepository
import com.hjl.jetpacklib.mvvm.BaseViewModel
import com.hjl.jetpacklib.mvvm.initViewModel
import java.lang.reflect.ParameterizedType

/**
 * @author: long
 * @description extends from BaseFragment2
 * @Date: 2020/6/3
 */
abstract class BaseMVVMFragment2<VDB : ViewDataBinding, BVM : BaseViewModel> :
        BaseFragment2<VDB>() {

    protected lateinit var viewModel: BVM

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModel()
        super.onCreate(savedInstanceState)
    }

    open fun initViewModel(){
        val arguments = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val bvmClass: Class<BVM> = arguments[1] as Class<BVM>
        viewModel = ViewModelProvider(this).get(bvmClass)
    }



}