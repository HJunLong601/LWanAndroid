package com.hjl.commonlib.utils;

import android.util.Log;

import com.hjl.commonlib.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogUtils {

    private static String className;//类名
    private static String methodName;//方法名
    private static int lineNumber;//行数

    private static final int JSON_INDENT = 2;

    private static String defaultTag = "LogUtils";

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    private static String createLog(String log ) {
        //        buffer.append(methodName);
        String buffer = "(" + className + ":" + lineNumber + ") " +
                log;
        return buffer;
    }

    private static void getMethodNames(StackTraceElement[] sElements){
        getMethodNames(sElements,1);
    }

    private static void getMethodNames(StackTraceElement[] sElements,int deep){
        className = sElements[deep].getFileName();
        methodName = sElements[deep].getMethodName();
        lineNumber = sElements[deep].getLineNumber();
    }


    public static void e(String message){
        e("",message);
    }

    public static void e(String TAG, String message){
        if (!isDebuggable())
            return;

        if (!StringUtils.isEmpty(TAG)){
            getMethodNames(new Throwable().getStackTrace());
            Log.e(TAG, createLog(message));
        }else {
            getMethodNames(new Throwable().getStackTrace(),2);
            Log.e(defaultTag, createLog(message));
        }
    }

    public static void i(String message){
        i("",message);
    }

    public static void i(String TAG, String message){
        if (!isDebuggable())
            return;


        if (TAG != null && TAG.trim().length() != 0){
            getMethodNames(new Throwable().getStackTrace());
            Log.i(TAG, createLog(message));
        }else {
            getMethodNames(new Throwable().getStackTrace(),2);
            Log.i(defaultTag, createLog(message));
        }
    }

    public static void d(String message){
        d("",message);
    }

    public static void d(String TAG, String message){
        if (!isDebuggable())
            return;


        if (TAG != null && TAG.trim().length() != 0){
            getMethodNames(new Throwable().getStackTrace());
            Log.d(TAG, createLog(message));
        }else {
            getMethodNames(new Throwable().getStackTrace(),2);
            Log.d(defaultTag, createLog(message));
        }
    }

    public static void v(String message){
        v("",message);
    }

    public static void v(String TAG, String message){
        if (!isDebuggable())
            return;

        if (TAG != null && TAG.trim().length() != 0){
            getMethodNames(new Throwable().getStackTrace());
            Log.v(TAG, createLog(message));
        }else {
            getMethodNames(new Throwable().getStackTrace(),2);
            Log.v(defaultTag, createLog(message));
        }
    }

    public static void w(String message){
        w("",message);
    }

    public static void w(String TAG, String message){
        if (!isDebuggable())
            return;

        if (TAG != null && TAG.trim().length() != 0){
            getMethodNames(new Throwable().getStackTrace());
            Log.w(TAG, createLog(message));
        }else {
            getMethodNames(new Throwable().getStackTrace(),2);
            Log.w(defaultTag, createLog(message));
        }
    }

    public static void json(String TAG , String json){

        if (json == null || json.trim().length() == 0){
            e(TAG,"Null Json content");
            return;
        }
        json = json.trim();
        try {
            if (json.startsWith("{")){
                JSONObject jsonObject = new JSONObject(json);
                d(TAG,jsonObject.toString(JSON_INDENT));
                return;
            }

            if (json.startsWith("[")){
                JSONArray jsonArray = new JSONArray(json);
                d(TAG,jsonArray.toString(JSON_INDENT));
                return;
            }
            e(TAG,"Invalid Json content");
        }catch (JSONException e) {
            e(TAG,"Invalid Json content");
        }

    }

    private static void outputJsonObject(String TAG, String json){


    }

    private static void outputJsonArray(String json){

    }



}
