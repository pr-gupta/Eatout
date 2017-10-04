package com.eatout.android.util.imgur

import com.eatout.android.util.imgur.bean.ImageResult
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by prashant.gup on 03/10/17.
 */
interface ImgurAPI {
    @GET("image/{imageHash}")
    fun getImage(@Path("imageHash") imageHash: String, @Header("authorization") authorizationHeader: String): Call<ImageResult>

    @FormUrlEncoded
    @POST("image")
    fun postImage(@Field("image") imageData: String, @Header("authorization") authorizationHeader: String): Call<ImageResult>
}