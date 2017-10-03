package com.eatout.android.util.imgur.controller

import android.util.Log
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
 * Created by prashant.gup on 03/10/17.
 */
class GetImageController : Callback<ImageResult> {

    private val TAG = javaClass.simpleName

    fun getImage(imageHash: String) {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
                .baseUrl(URLS.IMGUR_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        val imgurAPI = retrofit.create(ImgurAPI::class.java)
        imgurAPI.getImage(imageHash, PropertyUtil.getProperty("imgurClientID")).enqueue(this)
    }

    override fun onResponse(call: Call<ImageResult>?, response: Response<ImageResult>?) {
        response?.let {
            it.body()?.let {
                Log.v(TAG, it.toString())
            }
        }
    }

    override fun onFailure(call: Call<ImageResult>?, t: Throwable?) {
        Log.e(TAG, t.toString())
    }
}