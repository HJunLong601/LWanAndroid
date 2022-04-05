package com.hjl.core.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.hjl.commonlib.base.BaseApplication
import com.tencent.bugly.crashreport.biz.UserInfoBean
import com.hjl.core.net.bean.*


/**
 * author: long
 * description please add a description here
 * Date: 2020/8/17
 */
object SpUtils {

    val SP_TAG_USER = "sp_tag_user"

    val SP_USER_INFO = "sp_user_info"


    fun saveUserInfo(userInfo: String){
        val editor = getUserInfoSP().edit()
        editor.putString(SP_USER_INFO, Base64.encodeToString(userInfo.toByteArray(),Base64.NO_PADDING))
        editor.apply()
    }

    fun getUserInfo():String{
        return String(Base64.decode(getUserInfoSP().getString(SP_USER_INFO,""),Base64.NO_PADDING))
    }

    private fun getUserInfoSP():SharedPreferences{
        return BaseApplication.getApplication().getSharedPreferences(SP_TAG_USER,Context.MODE_PRIVATE)
    }


}