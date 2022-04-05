package com.hjl.skin

import android.graphics.Color

/**
 * Author : long
 * Description :
 * Date : 2021/12/21
 */
data class SkinItemBean(val desc : String,
                        val skinName : String,
                        val backgroundRes : Int = -1,
                        val backgroundColor : Int = Color.BLACK)
