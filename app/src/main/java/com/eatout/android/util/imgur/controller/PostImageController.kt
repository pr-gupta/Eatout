package com.eatout.android.util.imgur.controller

import com.eatout.android.util.PropertyUtil
import com.eatout.android.util.imgur.ImgurAPI
import com.eatout.android.util.imgur.bean.ImageResult
import com.eatout.android.util.zomato.beans.URLS
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by prashant.gup on 04/10/17.
 */
class PostImageController(
        private val sendImageURLtoFireBase: (String) -> Unit
) : Callback<ImageResult> {

    private val TAG = javaClass.simpleName

    fun postImage(imageBase64: String) {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
                .baseUrl(URLS.IMGUR_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        val imgurAPI = retrofit.create(ImgurAPI::class.java)
        imgurAPI.postImage(imageData = imageBase64, authorizationHeader = PropertyUtil.getProperty("imgurClientID")).enqueue(this)
    }

    override fun onFailure(call: Call<ImageResult>?, t: Throwable?) {
        t!!.printStackTrace()
    }

    override fun onResponse(call: Call<ImageResult>?, response: Response<ImageResult>?) {
        response?.let {
            val imageResult = response.body()!!
            sendImageURLtoFireBase(imageResult.data.link)
        }
    }
}