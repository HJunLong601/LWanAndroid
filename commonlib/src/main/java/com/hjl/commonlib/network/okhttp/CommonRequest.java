package com.hjl.commonlib.network.okhttp;

import android.util.Log;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * okhttp request对象封装
 */
public class CommonRequest {

    private static final String TAG = "HTTP CommonRequest";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_AUDIO = MediaType.parse("audio/*");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * post 请求
     */

    public static Request createPostRequest(String url, RequestParams params){
        return createPostRequest(url,params,null);
    }

    public static Request createPostRequest(String url,RequestParams params,RequestParams headers){
        FormBody.Builder mFromBodyBuider = new FormBody.Builder();

        //添加参数
        if (params != null){
            Log.w(TAG,"============== params list ==================");
            for (Map.Entry<String,String> entry : params.getParams().entrySet()){
                mFromBodyBuider.add(entry.getKey(),entry.getValue());
                Log.w(TAG," params:  " + entry.getKey() + "    valus:  " + entry.getValue());
            }
            Log.w(TAG,"============== params list ==================");
        }

        FormBody mFormBody = mFromBodyBuider.build();
        Request.Builder request = new Request.Builder().url(url)
                .post(mFormBody);

        // 加入请求头
        if (headers != null){
            Headers.Builder mHeadBuilder = new Headers.Builder();
            for (Map.Entry<String,String> entry : headers.getParams().entrySet()){
                mHeadBuilder.add(entry.getKey(),entry.getValue());
            }
            request.headers(mHeadBuilder.build());
        }

        return request.build();
    }

    public static Request createPostRequest(String url,String json){
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Log.w(TAG,"==================JSON params is:\n " + json);
        return request;

    }


    /**
     * get 请求
     */

    public static Request getRequest(String url, Object tag) {
        // LogUtils.i(TAG, "Request:\n" + url + "\n" + paramsJson);
        Request request = new Request.Builder().url(url).get().tag(tag).build();
        return request;
    }

    public static Request createGetRequest(String url , RequestParams params){
        return createGetRequest(url,params,null);
    }

    public static Request createGetRequest(String url , RequestParams params ,RequestParams headers){
        return createGetRequest(url,params,headers,null);
    }

    public static Request createGetRequest(String url , RequestParams params ,RequestParams headers,String tag){

        StringBuilder urlBuilder = new StringBuilder(url).append("?");


        if (params != null) {
            for (Map.Entry<String, String> entry : params.getParams() .entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                Log.w(TAG,"params: " + entry.getKey() + " valus: " + entry.getValue());
            }
        }
        Request.Builder request = new Request.Builder()
                .url(urlBuilder.substring(0,urlBuilder.length()-1))
                .tag(tag)
                .get();

        if (headers != null){
            Headers.Builder mHeaderBuilder = new Headers.Builder();
            for (Map.Entry<String,String> entry : headers.getParams().entrySet()){
                mHeaderBuilder.add(entry.getKey(),entry.getValue());
            }
            request.headers(mHeaderBuilder.build());
        }


        return request.build();
    }

    // TODO: 2019/5/3 file upload request  /-- done --/
    /**
     * 文件上传
     */

    public static Request createFileRequest(String url,RequestParams params){

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody requestBody ;
        Map<String, File> map = params.getFileMap();
        //遍历map中所有参数到builder
        for (String key : map.keySet()) {
            String fileType = getMimeType(map.get(key).getName());
            Log.w(TAG,"file name: " +  map.get(key).getName());
            builder.addFormDataPart("files", map.get(key).getName(), RequestBody.create(MediaType.parse(fileType), map.get(key)));
        }
        requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url).post(requestBody)//添加请求体
                .build();
        return request;

    }

    // 根据文件名字获取文件的mime类型
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }

}
