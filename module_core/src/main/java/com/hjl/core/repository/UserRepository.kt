package com.hjl.core.repository

import com.hjl.jetpacklib.mvvm.BaseRepository
import com.hjl.core.net.await
import com.hjl.core.net.coreApiServer

/**
 * Author : long
 * Description :
 * Date : 2020/8/16
 */
class UserRepository : BaseRepository() {

    suspend fun login(username:String,password:String) =
        coreApiServer.login(username,password).await()

    suspend fun register(username:String,password:String,repassword:String) =
            coreApiServer.register(username, password, repassword).await()
}