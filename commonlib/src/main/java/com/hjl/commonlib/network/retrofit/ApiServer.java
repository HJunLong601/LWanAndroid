package com.hjl.commonlib.network.retrofit;



import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServer{

    /**
     * example request
     */

    @FormUrlEncoded
    @POST("xxxx/getWarningById")
    Observable<BaseResponse> waning(@Field("warningId") String warningId);

    @FormUrlEncoded
    @POST("v1/wyBase/login")
    Observable<BaseResponse> login(@FieldMap HashMap<String,String> data);

    @GET("xxxx/xxxx/xxxx/{workCode}")
    Observable<BaseResponse> getConfig(@Path("workCode") String workCode);
     // @Path  : v1/xxxx/xxxx/123
    // @Query : v1/xxxx/xxxx?workCode=123

    // 文件上传
    //上传图片(私有接口)
    /*
    @POST("index.php/PrivateApi/Goods/uploadPic")
    @Multipart
    Observable<BaseListModel<String>> upLoadImg(@Part MultipartBody.Part parts);
    */
    // 多图上传
    /*
    @POST("index.php/PrivateApi/Goods/uploadPic")
    @Multipart
    Observable<BaseListModel<String>> upLoadImg(@Part MultipartBody.Part[] parts);
    */
    // 混合  https://www.jianshu.com/p/6ccdec4f3dd2
    /*
    @POST("index.php/PrivateApi/Goods/uploadPic")
    @Multipart
    Observable<BaseListModel<String>> upLoadImg(@Part MultipartBody.Part[] parts, @Part("APP_KEY") RequestBody APP_KEY, @Part("APP_TOKEN") RequestBody APP_TOKEN);
    */
    // 加入 header
//    @FormUrlEncoded
//    @POST("xxxx/xxxx/login")
//    Observable<BaseResponse> login(@HeaderMap Map<String,String> headers, @FieldMap Map<String,String> data);


    /**
     * 实验
     */

    /*
    @Multipart
    @POST("xxxx/xxxx/xxxx/video/upload")
    Observable<BaseResponse> uploadImg(@Part("token") RequestBody token, @Part("projectId") RequestBody projectId, @Part MultipartBody.Part[] parts);

    //

    val parts = arrayOfNulls<MultipartBody.Part>(size = data.size)


        for ((index,value) in data.withIndex())
        data.forEach {
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),value)
            val filePart = MultipartBody.Part.createFormData("name$index",value.name,requestFile)
            parts[index] = filePart
        }

        val token = RequestBody.create(MediaType.parse("multipart/form-data"), token)
        val projectId = RequestBody.create(MediaType.parse("multipart/form-data"),projectId)

        addDisposable(apiServer.uploadImg(token,projectId,parts),object : HttpObserver<BaseResponse>(){
            override fun onSuccess(o: BaseResponse?) {
                view.uploadImgSuccess("success ${o?.errorCode}")
            }

            override fun showError(msg: String?) {
                view.postFieldFail("$msg" )
            }

        })


     */



}
