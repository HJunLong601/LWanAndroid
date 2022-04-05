package com.hjl.commonlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hjl.commonlib.base.BaseApplication;

/**
 * author: long
 * description please add a description here
 * Date: 2020/8/17
 */
public class SpUtils {

    private static final String COMMON_SP = "common_sp";

    private static final String SP_COOKIE = "sp_cookie";

    public static void clearCookie(){
        addCookie("");
    }

    public static void addCookie(String cookie){
        SharedPreferences sharedPreferences = getCommonSP();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(SP_COOKIE,cookie);
        edit.apply();
    }

    public static String getCookie(){
        return getCommonSP().getString(SP_COOKIE,"");
    }

    public static SharedPreferences getCommonSP(){
        return BaseApplication.getApplication().getSharedPreferences(COMMON_SP, Context.MODE_PRIVATE);
    }

    public static String getData(String key){
        return getCommonSP().getString(key,"");
    }

    public static void saveData(String key,String data){
        getCommonSP().edit().putString(key,data).apply();
    }

}
