package com.hjl.commonlib.network.interceptor;

import com.hjl.commonlib.base.BaseApplication;
import com.hjl.commonlib.utils.NetWorkUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description 缓存拦截器
 * Author long
 * Date 2020/6/14 16:17
 */
public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        if (!NetWorkUtils.isNetworkConnected(BaseApplication.getApplication())){

            CacheControl.Builder builder = new CacheControl.Builder();
            builder.maxAge(0, TimeUnit.SECONDS);
            builder.maxStale(24,TimeUnit.HOURS);

            request.newBuilder().cacheControl(builder.build());

        }


        return chain.proceed(request);
    }
}
