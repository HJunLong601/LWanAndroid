package com.hjl.jetpacklib.mvvm.view


import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.hjl.commonlib.base.BaseMultipleActivity
import com.hjl.jetpacklib.R
import com.hjl.jetpacklib.mvvm.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * Description 基于MVVM的 Fragment
 * Date 2020/3/3 13:12
 * created by long
 */
abstract class BaseMVVMActivity<VDB : ViewDataBinding,BVM : BaseViewModel> : BaseMultipleActivity() {

    protected lateinit var viewModel: BVM

    private var dataBinding: VDB? = null

    // 做一层兼容
    protected lateinit var binding : VDB


    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {

        val arguments = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val bvmClass: Class<BVM> = arguments[1] as Class<BVM>
        viewModel = ViewModelProvider(this).get(bvmClass)


        super.onCreate(savedInstanceState)

    }


    override fun initBaseView() {
        super.initBaseView()

        dataBinding = DataBindingUtil.bind(mMultipleStateView.getmContentView())
        dataBinding!!.lifecycleOwner = this
        binding =  checkNotNull(dataBinding)
    }

    override fun getStatusBarColor(): Int {
        return R.color.common_base_theme_color
    }

    override fun onDestroy() {
        super.onDestroy()
        dataBinding = null
    }
}