package com.hjl.jetpacklib.mvvm

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.hjl.commonlib.base.BaseDialog

/**
 * author: long
 * description please add a description here
 * Date: 2021/5/28
 */
abstract class BaseDialog<VDB : ViewDataBinding>(context: Context) : BaseDialog(context) {

    private var viewBinding : VDB? = null
    protected lateinit var binding : VDB

    override fun setContentView(layoutResID: Int) {
        viewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutResID, null, false)
        binding = checkNotNull(viewBinding)
        setContentView(viewBinding!!.root)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

    }


}