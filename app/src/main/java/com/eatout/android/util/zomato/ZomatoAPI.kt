package com.eatout.android.util.zomato

import com.eatout.android.util.zomato.beans.CategoriesList
import retrofit2.Call
import com.eatout.android.util.zomato.beans.Category
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Created by prashant.gup on 23/08/17.
 */
interface ZomatoAPI {

    @GET("categories")
    fun getCategories(@Header("user-key") userKey: String):Call<CategoriesList>
}