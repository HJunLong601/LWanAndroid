package com.hjl.jetpacklib.mvvm


import android.util.Log
import androidx.lifecycle.ViewModel
import com.hjl.jetpacklib.mvvm.exception.ApiException
import com.hjl.jetpacklib.mvvm.exception.ExceptionHandler
import com.hjl.commonlib.utils.LogUtils
import kotlinx.coroutines.*

/**
 * Description
 * Date 2020/3/2 16:49
 * created by long
 */
open class BaseViewModel : ViewModel() {


    private val jobCancelList = ArrayList<Job>()

    protected val TAG = javaClass.simpleName

    // 默认可以取消网络请求 如果需要请求网络在页面销毁继续完成保存数据等操作则设置为false
    protected fun launch(
        request : suspend () -> Unit,
        fail : suspend (ApiException) -> Unit = {LogUtils.e(it.errorMessage)},
        isCancelable: Boolean = true){

        val job = GlobalScope.launch {

            try {
                withContext(Dispatchers.IO){
                    request()
                }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main){
                    val exception = ExceptionHandler.handle(e)
                    Log.e(TAG, e.message.toString())
                    fail(exception)
                }
            }
        }

        if (isCancelable) jobCancelList.add(job)

    }

    override fun onCleared() {

        // 取消网络请求
        for (job in jobCancelList){
            if (job.isActive){
                job.cancel()
            }
        }

        super.onCleared()
    }

}