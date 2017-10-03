package com.eatout.android.util.zomato.controller

import android.content.Context
import android.util.Log
import com.eatout.android.db.DBCategoryHelper
import com.eatout.android.util.PropertyUtil
import com.eatout.android.util.zomato.ZomatoAPI
import com.eatout.android.util.zomato.beans.URLS
import com.eatout.android.util.zomato.beans.common.categories.CategoriesList
import com.eatout.android.util.zomato.events.GetCategoryCompletionEvent
import com.google.gson.GsonBuilder
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by prashant.gup on 23/08/17.
 */
class CategoriesController (val context:Context) : Callback<CategoriesList>,ICategories(context) {
    private var _categoryList: CategoriesList? = null
    private val TAG = javaClass.simpleName

    override fun getCategories() {
        Log.v(TAG, "in getCategories")
        if(_categoryList == null) {
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                    .baseUrl(URLS.ZOMATO_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            val zomatoAPI = retrofit.create(ZomatoAPI::class.java)
            zomatoAPI.getCategories(PropertyUtil.getProperty("zomatoAPIKey")).enqueue(this)
        }
        else
            postCategory()
    }

    override fun onFailure(call: Call<CategoriesList>?, t: Throwable?) {
        Log.e(TAG, t?.message)
    }

     override fun onResponse(call: Call<CategoriesList>?, response: Response<CategoriesList>?) {
        Log.v(TAG, "In onResponse")
        if(response != null && response.isSuccessful) {
            _categoryList = response.body() as CategoriesList

            val db = DBCategoryHelper(context)
            for(category in _categoryList!!._categories)
                db.insertCategory(category)
            postCategory()
        }
        else {
            if (response != null)
                Log.w(TAG, "Warning!, received unsuccessful response\n\n" + response.toString())
            else
                Log.w(TAG, "Warning!, received null response from get Categories")
        }
    }

    private fun postCategory() {
        EventBus.getDefault().post(GetCategoryCompletionEvent(_categoryList as CategoriesList))
    }


}