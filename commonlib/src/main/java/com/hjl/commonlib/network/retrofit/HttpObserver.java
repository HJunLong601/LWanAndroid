package com.hjl.commonlib.network.retrofit;

import com.google.gson.JsonParseException;
import com.hjl.commonlib.R;
import com.hjl.commonlib.base.BaseApplication;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class HttpObserver<T> extends DisposableObserver<T> {

    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1001;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1002;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 1003;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1004;


    public HttpObserver() {

    }

    @Override
    protected void onStart() {

    }

    @Override
    public void onNext(T o) {
        try {
            onSuccess(o);
        } catch (Exception e) {
            e.printStackTrace();
            onError(e.toString());
        }


    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(PARSE_ERROR);
        } else {
            if (e != null) {
                onError(e.toString());
            } else {
                onError(BaseApplication.getApplication().getString(R.string.unknown_error));
            }
        }

    }

    private void onException(int unknownError) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError(BaseApplication.getApplication().getString(R.string.connection_error));
                break;

            case CONNECT_TIMEOUT:
                onError(BaseApplication.getApplication().getString(R.string.cnnctn_tmd_t));
                break;

            case BAD_NETWORK:
                onError(BaseApplication.getApplication().getString(R.string.network_issues));
                break;

            case PARSE_ERROR:
                onError(BaseApplication.getApplication().getString(R.string.fld_t_prs_dt));
                break;

            default:
                break;
        }
    }


    @Override
    public void onComplete() {


    }
    public abstract void onSuccess(T o);

    public abstract void onError(String msg);

}
