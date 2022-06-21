package com.hjl.jetpacklib.mvvm.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hjl.commonlib.R
import com.hjl.commonlib.base.MultipleStatusView
import com.hjl.commonlib.utils.AndroidUtils
import com.hjl.commonlib.utils.LogUtils

/**
 * @author: long
 * @description Base on ViewPager2,with lazy load
 * @Date: 2020/5/26
 */
abstract class BaseFragment2<VDB : ViewDataBinding> : Fragment(){

    protected var TAG: String = javaClass.simpleName

    lateinit var mContext: FragmentActivity

    private var isViewCreated: Boolean = false // 界面是否已创建完成
    protected var isVisibleToUser: Boolean = false // 是否对用户可见
    private var isDataLoaded: Boolean = false// 数据是否已请求, isNeedReload()返回false的时起作用
    private var isFragmentHidden: Boolean = true // 记录当前fragment的是否隐藏

    protected lateinit var mContentView: View
    protected lateinit var mMultipleStatusView: MultipleStatusView
    private var viewBinding : VDB? = null

    protected lateinit var binding : VDB

    private var isFirstLoad = true

    @LayoutRes
    abstract fun initLayoutResID(): Int

    abstract fun initData()

    abstract fun initView()

    // 真正请求数据的方法
    abstract fun loadData()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onResume() {
        super.onResume()

        if (isFirstLoad or loadMultipleTime()){
            loadData()
            isFirstLoad = false
            LogUtils.i(TAG,"load data")
        }

    }

    protected fun loadMultipleTime() = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val mView = inflater.inflate(R.layout.common_fragment_base_multiple, container, false)
        viewBinding = DataBindingUtil.inflate(inflater,initLayoutResID(),container,false)
        binding = checkNotNull(viewBinding)
        mContentView = viewBinding!!.root
        val lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mContentView.layoutParams = lp
        mMultipleStatusView = mView.findViewById(R.id.multiple_state_view)

        mMultipleStatusView.addView(mContentView)
        mMultipleStatusView.setContentView(mContentView)
        mMultipleStatusView.showContent()

        return mView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    open fun getViewStatus() : Int{
        return mMultipleStatusView.viewStatus
    }

    open fun showLoading() {
        AndroidUtils.runOnMainThread { mMultipleStatusView.showLoading() }
    }


    open fun showComplete() {
        AndroidUtils.runOnMainThread { mMultipleStatusView.showContent() }
    }


    open fun showEmpty() {
        AndroidUtils.runOnMainThread { mMultipleStatusView.showEmpty() }
    }


    open fun showError() {
        AndroidUtils.runOnMainThread { mMultipleStatusView.showError() }
    }
}