package com.hjl.commonlib.base

/**
 * Author : long
 * Description :
 * Date : 2022/10/26
 */
abstract class BaseSingleton<in P, out T> {
    @Volatile
    private var instance: T? = null

    protected abstract val creator: (P) -> T

    fun getInstance(param: P): T =
        instance ?: synchronized(this) {
            instance ?: creator(param).also { instance = it }
        }


}