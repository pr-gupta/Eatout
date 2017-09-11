package com.eatout.android.util.zomato.controller

import android.util.Log
import com.eatout.android.util.PropertyUtil
import com.eatout.android.util.zomato.ZomatoAPI
import com.eatout.android.util.zomato.beans.URLS
import com.eatout.android.util.zomato.beans.restaurant.search.Restaurant_
import com.eatout.android.util.zomato.events.GetRestaurantDetailCompletionEvent
import com.google.gson.GsonBuilder
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by prashant.gup on 11/09/17.
 */
object RestaurantDetailController : Callback<Restaurant_> {

    val TAG = RestaurantDetailController::class.java.simpleName

    fun getRestaurantDetails(resId: String) {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
                .baseUrl(URLS.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        val zomatoAPI = retrofit.create(ZomatoAPI::class.java)
        zomatoAPI.getRestaurantDetails(PropertyUtil.getProperty("zomatoAPIKey"), resId).enqueue(this)
    }

    override fun onFailure(call: Call<Restaurant_>?, t: Throwable?) {
        Log.e(TAG, "Failed to get restaurant details")
        t!!.printStackTrace()
    }

    override fun onResponse(call: Call<Restaurant_>?, response: Response<Restaurant_>?) {
        if(response != null) {
            Log.v(TAG, "In response with data as ${response.body().toString()}")
            EventBus.getDefault().post(GetRestaurantDetailCompletionEvent(response.body() as Restaurant_))
        } else {
            Log.w(TAG, "null response received from Search Restaurant request")
        }
    }
}