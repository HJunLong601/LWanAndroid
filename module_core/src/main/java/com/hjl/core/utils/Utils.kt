package com.hjl.core.utils

import com.hjl.commonlib.utils.SpUtils

/**
 * Author : long
 * Description :
 * Date : 2020/9/6
 */

object Utils {

    fun hasCookie():Boolean{
        return SpUtils.getCookie().isNotEmpty()
    }

}