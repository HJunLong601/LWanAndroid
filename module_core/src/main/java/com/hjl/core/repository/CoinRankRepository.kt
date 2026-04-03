package com.hjl.core.repository

import com.hjl.core.net.await
import com.hjl.core.net.coreApiServer
import com.hjl.jetpacklib.mvvm.BaseRepository
import javax.inject.Inject

class CoinRankRepository @Inject constructor() : BaseRepository() {

    suspend fun getCoinRank(page: Int) = coreApiServer.getCoinRank(page).await()
}
