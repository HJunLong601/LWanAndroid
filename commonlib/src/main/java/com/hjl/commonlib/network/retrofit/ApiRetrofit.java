package com.hjl.commonlib.network.retrofit;

import com.hjl.commonlib.network.interceptor.CacheInterceptor;
import com.hjl.commonlib.network.interceptor.CookieInterceptor;
import com.hjl.commonlib.network.interceptor.LogInterceptor;
import com.hjl.commonlib.network.interceptor.RetryInterceptor;
import com.hjl.commonlib.utils.FileUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiRetrofit {

    public final String BASE_SERVER_URL = "https://www.wanandroid.com/";
    private volatile static ApiRetrofit apiRetrofit;
    private Retrofit retrofit;
    private Retrofit retrofitWithoutGson;
    private OkHttpClient okHttpClient;

    private String TAG = "ApiRetrofit";


    private ApiRetrofit(){

        File cacheDir = new File(FileUtils.getInternalStorageCachePath(),"response");
        Cache cache = new Cache(cacheDir,20 * 1024 * 1024);


        okHttpClient = new OkHttpClient.Builder()
                //添加log拦截器
                .addInterceptor(new LogInterceptor())
                .addInterceptor(new RetryInterceptor())
                .addInterceptor(new CookieInterceptor())
//                .addInterceptor(new CacheInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .cache(cache)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                //支持RxJava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        retrofitWithoutGson = new Retrofit.Builder()
                .baseUrl(BASE_SERVER_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                //支持RxJava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

    }

    public static ApiRetrofit getInstance() {
        if (apiRetrofit == null) {
            synchronized (ApiRetrofit.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new ApiRetrofit();
                }
            }
        }
        return apiRetrofit;
    }

    public <T> T createApiServer(Class<T> cls){
        return retrofit.create(cls);
    }

    public <T> T createApiServerWithoutGson(Class<T> cls){
        return retrofitWithoutGson.create(cls);
    }
}
