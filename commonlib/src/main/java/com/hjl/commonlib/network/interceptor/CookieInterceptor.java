package com.hjl.commonlib.network.interceptor;

import com.hjl.commonlib.utils.SpUtils;
import com.hjl.commonlib.utils.StringUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: long
 * description 添加和保存Cookie
 * Date: 2020/8/17
 */
public class CookieInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        String url = chain.request().url().toString();
        Response response;

        if (url.contains("user/login")){
            response = chain.proceed(builder.build());
            List<String> headers = response.headers("Set-Cookie");
            if (headers.size() > 0){
                SpUtils.addCookie(headers.toString());
            }
            return response;
        }

        if (!StringUtils.isEmpty(SpUtils.getCookie())) builder.addHeader("Cookie",SpUtils.getCookie());

        return chain.proceed(builder.build());
    }

}
