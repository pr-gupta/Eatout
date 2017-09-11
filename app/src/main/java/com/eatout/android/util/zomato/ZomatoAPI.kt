package com.eatout.android.util.zomato

import com.eatout.android.util.zomato.beans.common.categories.CategoriesList
import com.eatout.android.util.zomato.beans.restaurant.search.Restaurant_
import com.eatout.android.util.zomato.beans.restaurant.search.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Created by prashant.gup on 23/08/17.
 */
interface ZomatoAPI {

    @GET("categories")
    fun getCategories(@Header("user-key") userKey: String) : Call<CategoriesList>

    @GET("search")
    fun searchRestaurants(@Header("user-key") userKey: String, @QueryMap options: Map<String, String>) : Call<SearchResult>

    @GET("restaurant")
    fun getRestaurantDetails(@Header("user-key") userKey: String, @Query("res_id") resID: String): Call<Restaurant_>

}