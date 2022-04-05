package com.hjl.jetpacklib.mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.hjl.commonlib.R
import com.hjl.jetpacklib.mvvm.BaseRepository
import com.hjl.jetpacklib.mvvm.BaseViewModel
import com.hjl.jetpacklib.mvvm.initViewModel
import java.lang.reflect.ParameterizedType

/**
 * Description 基于MVVM的 Fragment
 * Date 2020/3/3 14:03
 * created by long
 */
abstract class BaseMVVMFragment<VDB : ViewDataBinding, BVM : BaseViewModel, BR : BaseRepository> :
        BaseFragment() {

    protected lateinit var viewModel: BVM

    protected lateinit var binding: VDB

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        val arguments = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val bvmClass: Class<BVM> = arguments[1] as Class<BVM>
        val brClass: Class<BR> = arguments[2] as Class<BR>

        viewModel = initViewModel(bvmClass,brClass)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val mView = inflater.inflate(R.layout.common_fragment_base_multiple, container, false)
        mContentView = inflater.inflate(initLayoutResID(), container, false)
        val lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mContentView!!.layoutParams = lp
        mMultipleStatusView = mView.findViewById(R.id.multiple_state_view)

        mMultipleStatusView.addView(mContentView)
        mMultipleStatusView.setContentView(mContentView)
        mMultipleStatusView.showContent()

        binding = DataBindingUtil.bind<VDB>(mMultipleStatusView)!!
        binding.lifecycleOwner = this

        return mView
    }
}