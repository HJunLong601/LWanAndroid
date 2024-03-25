package com.hjl.commonlib.network.interceptor

import android.util.Log
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * created by long on 2019/10/21
 */
class LogInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(request)
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        Log.d(TAG, "---------------------Request Start---------------------------")
//        Log.d(TAG, "|${request.url()}")
        Log.d(TAG, "|${request}")
        val method = request.method()
        if ("POST" == method) {
            if (request.body() is FormBody) {
                val body = request.body() as FormBody?
                for (i in 0 until body!!.size()) {
                    Log.i(
                        TAG,
                        "| RequestParams:{params :${body.encodedName(i)} values:${
                            body.encodedValue(i)
                        }}"
                    )
                }
            }
        }
        Log.d(TAG, "Response: ")
        Log.d(TAG, "| Response:$content")
        Log.d(TAG, "------------------End Request Cost: $duration ms ------------------")
        return response.newBuilder().body(ResponseBody.create(mediaType, content)).build()
    }

    companion object {
        var TAG = "LogInterceptor"
    }
}