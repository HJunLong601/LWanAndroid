package com.hjl.commonlib.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;
import java.util.List;

public class JsonUtils {

    private static final String TAG = "JsonError";

    public static Object toJsonObject(String json){
        return JSON.parseObject(json);
    }

    public static <T> T toJsonBean(String json,Class<T> cls){
        if (!json.startsWith("{") && !json.startsWith("[")) {
            return (T) json;
    }
        try {
            T result = JSON.parseObject(json, cls);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Json解析错误. " + e.toString());
            Log.e(TAG, "json =  " + json);
        }
        return null;
    }

    public static String toJSONString(Object object) {
        try {
            String json = JSON.toJSONString(object);
            // Log.d(TAG, "json = " + json);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Bean-Json错误. object = " + object);
        }
        return null;
    }

    /**********************************************************/

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        try {
            return JSON.parseArray(text, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Json解析错误. " + e.toString());
            Log.e(TAG, "json =  " + text);
        }
        return null;
    }

    public static List<Object> parseArray(String text, Type[] types) {
        try {
            return JSON.parseArray(text, types);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Json解析错误. " + e.toString());
            Log.e(TAG, "json =  " + text);
        }
        return null;
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        if (!text.startsWith("{") && !text.startsWith("[")) {
            return (T) text;
        }
        try {
            T result = JSON.parseObject(text, clazz);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "Json解析错误. " + e.toString());
            LogUtils.e(TAG, "json =  " + text);
        }
        return null;
    }

    public static <T> T parseObject(String text, Type clazz) {
        try {
            return JSON.parseObject(text, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "Json解析错误. " + e.toString());
            LogUtils.e(TAG, "json =  " + text);
        }
        return null;
    }

}
