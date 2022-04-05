package com.hjl.core.net

import com.hjl.core.net.bean.*
import retrofit2.Call
import retrofit2.http.*

/**
 * @author: long
 * @description Network Server For Core Module
 * @Date: 2020/6/4
 */
interface CoreApiServer {


    @GET("banner/json")
    fun getBanner() : Call<BaseResponse<List<HomeBannerBean>>>

    /**
     * 首页文章
     */
    @GET("article/list/{index}/json")
    fun getHomeArticleList(@Path("index") index : Int) : Call<BaseResponse<HomeArticleBean>>

    /**
     * 置顶文章
     */
    @GET("article/top/json")
    fun getHomeTopArticle():Call<BaseResponse<List<HomeArticleBean.Article>>>

    /**
     * 广场文章
     */

    @GET("user_article/list/{index}/json")
    fun getSquareArticle(@Path("index") index: Int) : Call<BaseResponse<HomeArticleBean>>


//    https://wanandroid.com/wenda/list/0/json

    /**
     * 广场文章
     */

    @GET("wenda/list/{index}/json")
    fun getWenda(@Path("index") index: Int) : Call<BaseResponse<HomeArticleBean>>

    /**
     * 体系数据
     */

    @GET("tree/json")
    fun getSystemTree() : Call<BaseResponse<List<SystemListBean>>>

    /**
     * 导航数据
     */
    @GET("navi/json")
    fun getNaviTree() : Call<BaseResponse<List<NavigationListBean>>>


    /**
     * 体系文章
     */
    @GET("article/list/{index}/json")
    fun getSystemArticle(@Path("index") index: Int,@Query("cid")courseId: Int) : Call<BaseResponse<HomeArticleBean>>




    /**************************************************************
     ************************* 用户相关  ***************************
     *************************************************************/

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username")username:String,@Field("password")password:String) : Call<BaseResponse<UserBean>>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    fun register(@Field("username")username:String,@Field("password")password:String,@Field("repassword")repassword:String) : Call<BaseResponse<UserBean>>

    /**
     * 根据id收藏文章
     */
    @POST("lg/collect/{id}/json")
    fun collectArticle(@Path("id")id : Int) : Call<BaseResponse<String>>

    /**
     * 自定位收藏
     */
    @FormUrlEncoded
    @POST("lg/collect/add/json")
    fun collectArticle(@Field("title")title:String,@Field("author")author:String,@Field("link")link:String) : Call<BaseResponse<String>>

    /**
     * 移除收藏/点赞
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun removeCollect(@Path("id")id : Int) : Call<BaseResponse<String>>

    /**
     * 获取收藏列表
     */
    @GET("lg/collect/list/{page}/json")
    fun getCollectList(@Path("page")page : Int) : Call<BaseResponse<CollectItemBean>>

    /**
     * 退出
     */
    @GET("user/logout/json")
    fun logout() :Call<BaseResponse<String>>




    /**************************************************************
     ************************* 搜索相关  ***************************
     *************************************************************/

    /**
     * 搜索热词
     */
    @GET("hotkey/json")
    fun getHotKey() :Call<BaseResponse<List<HomeSearchHotKeyBean>>>

    /**
     * 常用文章
     */
    @GET("friend/json")
    fun getCommonlyWeb() : Call<BaseResponse<List<CommonlyWebBean>>>

    /**
     * 搜索
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    fun search(@Path("page")page : Int,@Field("k")key:String) : Call<BaseResponse<HomeArticleBean>>



    /**************************************************************
     ************************* "我的" 相关接口  ********************
     *************************************************************/

    /**
     *
     */
    @GET("maven_pom/package/json")
    fun getGoogleMavenPackage() : Call<BaseResponse<List<String>>>

    @GET("maven_pom/search/json")
    fun searchGoogleMaven(@Query("k")key: String) : Call<BaseResponse<List<MavenItemBean>>>

}

//