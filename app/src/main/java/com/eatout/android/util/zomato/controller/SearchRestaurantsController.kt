package com.eatout.android.util.zomato.controller

import android.content.Context
import android.util.Log
import com.eatout.android.util.PropertyUtil
import com.eatout.android.util.zomato.ZomatoAPI
import com.eatout.android.util.zomato.beans.URLS
import com.eatout.android.util.zomato.beans.restaurant.search.SearchFilter
import com.eatout.android.util.zomato.beans.restaurant.search.SearchResult
import com.eatout.android.util.zomato.events.SearchRestaurantCompletionEvent
import com.google.gson.GsonBuilder
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by prashant.gup on 28/08/17.
 */
class SearchRestaurantsController(var context: Context) : Callback<SearchResult>, ISearchRestaurant(context) {

    private val TAG = javaClass.simpleName


    override fun searchRestaurants(searchFilter: SearchFilter) {
        Log.i(TAG, "In search Restaurants")

        val data = searchFilter.getStringMap()
        Log.d(TAG, data.toString())
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
                .baseUrl(URLS.ZOMATO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        val zomatoAPI = retrofit.create(ZomatoAPI::class.java)
        zomatoAPI.searchRestaurants(PropertyUtil.getProperty("zomatoAPIKey"), data).enqueue(this)
    }

    override fun onFailure(call: Call<SearchResult>?, t: Throwable?) {
        t!!.printStackTrace()
    }

    override fun onResponse(call: Call<SearchResult>?, response: Response<SearchResult>?) {
        if(response != null) {
            Log.v(TAG, "In response with data as ${response.body().toString()}")
            val searchResult = (response.body() as SearchResult)
            EventBus.getDefault().post(SearchRestaurantCompletionEvent(searchResult))
        } else {
            Log.w(TAG, "null response received from Search Restaurant request")
        }
    }

}