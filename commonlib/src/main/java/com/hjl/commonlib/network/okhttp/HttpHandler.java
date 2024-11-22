package com.hjl.commonlib.network.okhttp;

import android.content.Context;
import android.util.Log;

import com.hjl.commonlib.R;
import com.hjl.commonlib.base.BaseApplication;
import com.hjl.commonlib.utils.JsonUtils;
import com.hjl.commonlib.utils.ToastUtil;
import com.hjl.commonlib.utils.Utils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.SocketException;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;


public abstract class HttpHandler<T> implements Callback{

    private static final String TAG = Utils.getSimpleTAG(HttpHandler.class);
    protected Context mAppContext;

    protected Class<T>  entityClass;  // T.class 泛型的class类型  用于gson解析
    private long startTime;

    public HttpHandler() {

        this.mAppContext = BaseApplication.getApplication();
        try {
            entityClass = (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception e) {
            e.printStackTrace();
            entityClass = (Class<T>) Object.class;
        }

    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG, "onFailure " + e.toString());
        if (e instanceof UnknownHostException || e instanceof SocketException) {
            onFailOnUiThread(BaseApplication.getApplication().getString(R.string.ntwrk_cnnctn_flr), e.getMessage());
        } else {
            onFailOnUiThread(BaseApplication.getApplication().getString(R.string.ntwrk_cnnctn_fld_pl), e.getMessage());
        }

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        handlerResponse(response);
    }

    public void handlerResponse(Response response) throws IOException {
        if (response.code() == 200){
            //请求码成功
            String respBodyStr = response.body().string();
            final String httpUrl = response.request().url().toString();
            Headers headers = response.request().headers();
          //  Log.w(TAG, "resuest url: " + httpUrl + "\r\n  header:" + headers + "\r\n");
          //  Log.w(TAG, "respBodyStr  result=:" + respBodyStr);
            if (!Utils.isStringEmpty(respBodyStr)){
                T data = JsonUtils.toJsonBean(respBodyStr,entityClass);
                if (data != null){
                    onSuccessOnUiThread(data);
                }else {
                    onFailOnUiThread(BaseApplication.getApplication().getString(R.string.json_parsing_error), respBodyStr);
                    Log.e(TAG, BaseApplication.getApplication().getString(R.string.json_parsing_error));
                }

            }else {
                onFailOnUiThread(BaseApplication.getApplication().getString(R.string.th_rtrnd_rslt_s_mpty), respBodyStr);
            }
        }else {
            onFailOnUiThread(BaseApplication.getApplication().getString(R.string.rqst_rrr_rrr_cd_cln) + response.code(), response.body().string());
        }
    }

    private void onSuccessOnUiThread(T data){
        BaseApplication.runOnUIThread(()->{
            onSuccess(data);
            onFinish();
        });
    }

    private void onFailOnUiThread(String message, String response){
        BaseApplication.runOnUIThread(()->{
            onFailure(message,response);
            onFinish();
        });
    }

    /**
     * 请求开始
     */
    void onStart(){
        startTime = System.currentTimeMillis();
      //  Log.w(TAG,"===========================start request at " + com.example.commonlib.utils.DateUtils.getHttpRequetTime(startTime) + " ===========================");
    }

    /**
     * 请求结束
     */
    private void onFinish(){
        long endTime = System.currentTimeMillis();
     //   Log.w(TAG,"===========================end request at " + com.example.commonlib.utils.DateUtils.getHttpRequetTime(endTime) + " ===========================");
      //  Log.w(TAG,"=========================== spend time is " + (endTime-startTime)+ " millis ===========================\n");
    }

    public abstract void onSuccess(T data);

    public void onFailure(String message, String response){
        // 有回应但是可能是请求参数有问题或者服务端有问题
        ToastUtil.showSingleToast(message);// 默认打印信息
    }
}
