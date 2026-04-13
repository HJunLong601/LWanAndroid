package com.hjl.commonlib.network.interceptor;

import com.hjl.commonlib.utils.WanAndroidUrlUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 玩安卓域名兜底拦截器。
 * 旧的 www 域名请求会自动重写到主站域名，降低解析失败概率。
 */
public class WanAndroidDomainInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();
        if (WanAndroidUrlUtils.LEGACY_HOST.equalsIgnoreCase(url.host())) {
            HttpUrl newUrl = url.newBuilder()
                    .host(WanAndroidUrlUtils.PRIMARY_HOST)
                    .build();
            request = request.newBuilder().url(newUrl).build();
        }
        return chain.proceed(request);
    }
}
