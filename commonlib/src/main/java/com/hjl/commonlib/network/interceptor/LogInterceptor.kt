package com.hjl.commonlib.network.interceptor

import android.util.Log
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
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
        val responseBody = response.body ?: return response
        val mediaType = responseBody.contentType()
        val content = responseBody.string()
        Log.d(TAG, "---------------------Request Start---------------------------")
//        Log.d(TAG, "|${request.url()}")
        Log.d(TAG, "|${request}")
        val method = request.method
        if ("POST" == method) {
            val requestBody = request.body
            if (requestBody is FormBody) {
                for (i in 0 until requestBody.size) {
                    Log.i(
                        TAG,
                        "| RequestParams:{params :${requestBody.encodedName(i)} values:${
                            requestBody.encodedValue(i)
                        }}"
                    )
                }
            }
        }
        Log.d(TAG, "Response: ")
        Log.d(TAG, "| Response:$content")
        Log.d(TAG, "------------------End Request Cost: $duration ms ------------------")
        return response.newBuilder().body(content.toResponseBody(mediaType)).build()
    }

    companion object {
        var TAG = "LogInterceptor"
    }
}
