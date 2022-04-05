package com.hjl.core.net

/**
 * @author: long
 * @description BaseResponse for WanAndroid API
 * @Date: 2020/6/4
 */
data class BaseResponse<T>(var data : T,var errorCode : Int = -1,var errorMsg : String = "")