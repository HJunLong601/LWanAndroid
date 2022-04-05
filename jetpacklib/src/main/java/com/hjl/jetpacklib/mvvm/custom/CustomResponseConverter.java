package com.hjl.jetpacklib.mvvm.custom;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.hjl.jetpacklib.mvvm.exception.ApiException;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Description  根据result 解析，解决请求成功但是参数错误（如密码错误）返回为 "" 导致的json解析异常的问题
 * Date 2020/3/5 9:02
 * created by long
 */
public class CustomResponseConverter<T> implements Converter<ResponseBody, T> {


    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private Type mType;

    CustomResponseConverter(Gson gson, TypeAdapter<T> adapter, Type mType) {
        this.gson = gson;
        this.adapter = adapter;
        this.mType = mType;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String body = value.string();
        ApiException apiException = null;
        String errorCode = "";
        String message = "";
        try {
            JSONObject jsonObject = new JSONObject(body);
            errorCode = jsonObject.getString("errorCode");
            message = jsonObject.getString("message");
            String result = jsonObject.getString("result");

            if (result == null || result.length() == 0){
                throw new ApiException(Integer.parseInt(errorCode),message);
            }else {
                return gson.fromJson(body,mType);
            }

        } catch (Exception e) {
            throw new ApiException(Integer.valueOf(errorCode),message);
        }


    }
}
