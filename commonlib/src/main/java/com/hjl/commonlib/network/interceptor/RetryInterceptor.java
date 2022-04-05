package com.hjl.commonlib.network.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * created by long on 2019/11/6
 */
public class RetryInterceptor implements Interceptor {

    private String TAG = "RetryInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();
        Response response = chain.proceed(originalRequest);

        int retryCount = 0;

        while (!response.isSuccessful() && retryCount < 3){
            Log.d(TAG, "retryCount: " + retryCount );
            response.close();
            response = chain.proceed(originalRequest);
            retryCount++;

        }

        return response;
    }
}
