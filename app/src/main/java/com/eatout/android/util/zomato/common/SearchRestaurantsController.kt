package com.eatout.android.util.zomato.common

import android.content.Context
import android.location.LocationManager
import com.eatout.android.util.zomato.beans.SearchFilter
import android.content.Context.LOCATION_SERVICE



/**
 * Created by prashant.gup on 28/08/17.
 */
object SearchRestaurantsController {

    var _searchFilter = SearchFilter()

    fun searchRestaurants(context: Context) {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        searchRestaurants(_searchFilter)
    }


    fun searchRestaurants(searchFilter: SearchFilter) {

    }
}