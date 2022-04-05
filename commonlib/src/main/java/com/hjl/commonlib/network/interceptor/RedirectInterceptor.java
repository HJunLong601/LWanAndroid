package com.hjl.commonlib.network.interceptor;

import com.hjl.commonlib.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description 重定向拦截器
 * Date 2020/3/13 16:51
 * created by long
 */
public class RedirectInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        int code = response.code();
        if (code == 302) {
            //获取重定向的地址
            String location = response.headers().get("Location");
            LogUtils.e("重定向地址：", "location = " + location);
            //重新构建请求
            Request newRequest = request.newBuilder().url(location).build();
            response = chain.proceed(newRequest);
        }

        return response;
    }
}
