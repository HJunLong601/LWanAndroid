package com.hjl.jetpacklib.mvvm.view

import android.util.Log
import androidx.paging.PagingSource
import com.hjl.jetpacklib.mvvm.exception.ApiException
import com.hjl.jetpacklib.mvvm.exception.ExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * Description Base Class for DataSource
 * Author long
 * Date 2020/6/15 23:16
 */
abstract class BasePagingSource<Key : Any,Value : Any> : PagingSource<Key,Value>() {

    private val jobCancelList = ArrayList<Job>()

    protected val TAG = javaClass.simpleName

    // 默认可以取消网络请求 如果需要请求网络在页面销毁继续完成保存数据等操作则设置为false
    protected fun launch(request : suspend () -> Unit, fail : suspend (ApiException) -> Unit, isCancelable: Boolean = true){

        val job = GlobalScope.launch(Dispatchers.IO) {

            try {
                request()
            } catch (e: Throwable) {
                val exception = ExceptionHandler.handle(e)
                Log.e(TAG, e.message.toString())
                fail(exception)
            }
        }

        if (isCancelable) jobCancelList.add(job)

    }




}