package com.hjl.core.net

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hjl.jetpacklib.mvvm.exception.ApiException
import com.hjl.jetpacklib.mvvm.exception.ExceptionHandler
import com.hjl.commonlib.network.retrofit.ApiRetrofit
import com.hjl.commonlib.utils.JsonUtils
import com.hjl.commonlib.utils.LogUtils
import com.hjl.commonlib.utils.SpUtils
import com.hjl.commonlib.utils.ToastUtil

import com.hjl.module_base.CacheUtils
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Description 拓展retrofit网络请求
 * Author long
 * Date 2020/6/13 22:23
 */


var coreApiServer = ApiRetrofit.getInstance().createApiServer(CoreApiServer::class.java)!!


fun CoroutineScope.launchNetRequest(
        request : suspend () -> Unit,
        fail : suspend (ApiException) -> Unit = {LogUtils.e("HTTP",it.errorMessage)}){

    launch {

        try {
            withContext(Dispatchers.IO){
                request()
            }
        } catch (e: Throwable) {
            withContext(Dispatchers.Main){
                val exception = ExceptionHandler.handle(e)
                Log.e("http", e.message!!)
                fail(exception)
            }
        }
    }

}

// 拓展了Call 的方法 直接在这个 await方法调用 enqueue 处理并返回结果
// mClass传入要解析的Class类型；泛型无法获取故而传入；待优化
// 如果解析的数据是个列表，传入列表的泛型值；如果是单个数据直接传Bean类型
suspend inline fun <reified T> Call<BaseResponse<T>>.await(cacheKey : String? = null) : T{

    return suspendCoroutine {
        try {
            enqueue(object : Callback<BaseResponse<T>> {
                override fun onFailure(call: Call<BaseResponse<T>>, t: Throwable) {
                    dealFailure(cacheKey, it, t)
                }

                override fun onResponse(call: Call<BaseResponse<T>>, response: Response<BaseResponse<T>>) {
                    val body: BaseResponse<T> = response.body() as BaseResponse<T>


                    // 需要重新登录
                    if (body.errorCode == -1001){
                        SpUtils.addCookie("");
//                    LoginActivity.startLoginActivity(BaseApplication.getApplication())
                    }else if (body.errorCode != 0){     // 服务端返回错误
                        dealFailure(
                            cacheKey,
                            it,
                            ApiException(body.errorCode,body.errorMsg)
                        )
                    }else{
                        try {
                            if (body.data == null){
                                body.data = "" as T
                            }

                            val json = Gson().toJson(body.data)
                            if (!cacheKey.isNullOrEmpty() && !json.isNullOrEmpty()){
                                CacheUtils.saveCache(cacheKey, json)
                            }
                            it.resume(body.data)
                        }catch (e : Exception){
                            it.resumeWithException(e)
                        }catch (e : Throwable){
                            it.resumeWithException(e)
                        }
                    }
                }
            })
        }catch (e : Exception){
            ToastUtil.show(e.message)
            it.resumeWithException(e)
        }
    }
}

inline fun <reified T> dealFailure(cacheKey: String?, it: Continuation<T>, t: Throwable) {
    if (cacheKey.isNullOrEmpty()) {
        it.resumeWithException(t)
    } else {
        CacheUtils.getCache(cacheKey) { cache ->

            if (cache.isEmpty()) {
                it.resumeWithException(t)
            } else {
                val type: Type = object : TypeToken<BaseResponse<T>>() {}.type
                val bean = JsonUtils.parseObject<T>(cache,(type as ParameterizedType).actualTypeArguments[0])

                if (bean != null) {
                    it.resume(bean)
                    // 因为返回缓存数据，造成请求成功的假象，提示一下网络不好
                    ToastUtil.show(t.message)
                }else{
                    it.resumeWithException(t)
                }
            }
        }
    }
}