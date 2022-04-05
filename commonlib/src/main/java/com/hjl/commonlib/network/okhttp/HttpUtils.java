package com.hjl.commonlib.network.okhttp;

import android.graphics.DashPathEffect;

import com.hjl.commonlib.network.interceptor.LogInterceptor;
import com.hjl.commonlib.network.interceptor.RetryInterceptor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {

    /**
     * 连接超时
     */
    private static final long CONNECT_TIMEOUT_MILLIS = 8 * 1000;

    /**
     * 读取超时
     */
    private static final long READ_TIMEOUT_MILLIS = 8 * 1000;

    /**
     * 写入超时
     */
    private static final long WRITE_TIMEOUT_MILLIS = 8 * 1000;

    // 同步请求超时
    private static final long SYNC_TIMEOUT_MILLIS = 1500;

    private static final String TAG = HttpUtils.class.getSimpleName();

    /**
     * OkHttpClient实例
     */
    private static OkHttpClient client;     //异步
    private static OkHttpClient syncClient; //同步


    /**
     * 异步post请求 带请求头
     */
    public static void post(String url, RequestParams params, RequestParams headers, HttpHandler httpHandler ){
        Request request = CommonRequest.createPostRequest(url,params,headers);
        getClient().newCall(request).enqueue(httpHandler);
        httpHandler.onStart();
    }

    /**
     * 异步post请求 不带请求头
     */
    public static void post(String url, RequestParams params,HttpHandler httpHandler ){
        post(url,params,null,httpHandler);
    }

    /**
     * 同步post请求
     *
     */
    public static String postSync(String url,RequestParams params){
        return postSync(url,params,null);
    }

    public static String postSync(String url,RequestParams params,RequestParams headers) {
        Request request = CommonRequest.createGetRequest(url, params,headers);
        try {
            Response response = getSyncClient().newCall(request).execute();
            final String result = response.body().string();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * file upload 文件上传
     */
    public static void uploadFile(String url,RequestParams params,HttpHandler httpHandler){
        Request request = CommonRequest.createFileRequest(url,params);
        getClient().newCall(request).enqueue(httpHandler);
        httpHandler.onStart();
    }



    /**
     * get 请求 不带请求头
     */

    public static void get(String url,RequestParams params,HttpHandler httpHandler){
        get(url,params,null,httpHandler);
    }

    public static void get(String url,HttpHandler handler){
        Request request = CommonRequest.getRequest(url,null);
        getClient().newCall(request).enqueue(handler);
        handler.onStart();
    }

    public static void get(String url,HttpHandler handler,String tag){
        Request request = CommonRequest.getRequest(url,tag);
        getClient().newCall(request).enqueue(handler);
        handler.onStart();
    }

    /**
     * get 请求 带请求头
     */

    public static void get(String url,RequestParams params,RequestParams headers,HttpHandler httpHandler){
        Request request = CommonRequest.createGetRequest(url,params,headers);
        getClient().newCall(request).enqueue(httpHandler);
        httpHandler.onStart();
    }

    /**
     * get 请求 带请求头 带tag
     */

    public static void get(String url,RequestParams params,RequestParams headers,HttpHandler httpHandler,String tag){
        Request request = CommonRequest.createGetRequest(url,params,headers,tag);
        getClient().newCall(request).enqueue(httpHandler);
        httpHandler.onStart();
    }

    /**
     *  GET request with synchronization
     */

    public static String getSync(String url,RequestParams params){
        return getSync(url,params,null);
    }

    public static String getSync(String url,RequestParams params,RequestParams headers){

        try {
            Request request = CommonRequest.createGetRequest(url,params,headers);
            Response response = getSyncClient().newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void simpleGet(String url,Callback responseCallback){
        Request request = CommonRequest.createGetRequest(url, null);
        HttpUtils.getClient().newCall(request).enqueue(responseCallback);
    }

    public static void simplePost(String url,RequestParams params,Callback callback){

    }


    /**
     *  get okHttpClient
     */


    public static OkHttpClient getClient(){
        // TODO: 2019/5/3 重定向
        if (client == null){
            client = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                    .addInterceptor(new com.hjl.commonlib.network.interceptor.LogInterceptor())
                    .addInterceptor(new RetryInterceptor())
                    .readTimeout(READ_TIMEOUT_MILLIS,TimeUnit.MILLISECONDS)
                    .writeTimeout(WRITE_TIMEOUT_MILLIS,TimeUnit.MILLISECONDS)
                    .cache(new Cache(new File("cache"),24*1024*1024))
                    .build();
        }
        return client;
    }

    private static OkHttpClient getSyncClient() {
        if (syncClient == null) {
            syncClient = new OkHttpClient.Builder()
                    .connectTimeout(SYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                    .addInterceptor(new com.hjl.commonlib.network.interceptor.LogInterceptor())
                    .addInterceptor(new RetryInterceptor())
                    .readTimeout(SYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                    .writeTimeout(SYNC_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).build();
        }
        return syncClient;
    }

    public static void cancelTag(Object tag)
    {
        if (client != null){
            for (Call call : client.dispatcher().queuedCalls())
            {
                if (tag.equals(call.request().tag()))
                {
                    call.cancel();
                }
            }
            for (Call call : client.dispatcher().runningCalls())
            {
                if (tag.equals(call.request().tag()))
                {
                    call.cancel();
                }
            }
        }

        if (syncClient != null){
            for (Call call : syncClient.dispatcher().queuedCalls())
            {
                if (tag.equals(call.request().tag()))
                {
                    call.cancel();
                }
            }

            for (Call call : syncClient.dispatcher().runningCalls())
            {
                if (tag.equals(call.request().tag()))
                {
                    call.cancel();
                }
            }
        }


    }


    private void test(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor())
                .connectTimeout(CONNECT_TIMEOUT_MILLIS,
                        TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MILLIS,TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT_MILLIS,TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .url("www.baidu.com") // 传入url地址
                .get()  // get请求
//                .headers(mHeadBuilder.build()) // 传入请求头
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }



}
