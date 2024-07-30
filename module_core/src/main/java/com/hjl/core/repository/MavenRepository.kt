package com.hjl.core.repository

import com.hjl.core.net.await
import com.hjl.core.net.coreApiServer
import com.hjl.core.utils.CACHE_MAVEN_GOOGLE_PACKAGE_LIST
import com.hjl.jetpacklib.mvvm.BaseRepository
import javax.inject.Inject

/**
 * author: long
 * description please add a description here
 * Date: 2021/5/14
 */
class MavenRepository @Inject constructor() : BaseRepository() {

    suspend fun getGoogleMavenPackage() = coreApiServer.getGoogleMavenPackage().await(CACHE_MAVEN_GOOGLE_PACKAGE_LIST)

    suspend fun searchGoogleMaven(key : String) = coreApiServer.searchGoogleMaven(key).await()

}