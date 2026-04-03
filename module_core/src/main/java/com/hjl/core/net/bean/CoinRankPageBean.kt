package com.hjl.core.net.bean

data class CoinRankPageBean(
    val curPage: Int = 1,
    val datas: List<CoinRankItemBean> = emptyList(),
    val offset: Int = 0,
    val over: Boolean = false,
    val pageCount: Int = 0,
    val size: Int = 0,
    val total: Int = 0
)

data class CoinRankItemBean(
    val coinCount: Int = 0,
    val level: Int = 0,
    val nickname: String = "",
    val rank: String = "",
    val userId: Int = 0,
    val username: String = ""
) {
    val displayName: String
        get() = nickname.ifBlank { username.ifBlank { "匿名用户" } }
}
