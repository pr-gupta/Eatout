package com.eatout.android.util.zomato.controller

import android.content.Context
import com.eatout.android.util.zomato.beans.restaurant.search.SearchFilter

/**
 * Created by prashant.gup on 13/09/17.
 */
abstract class ISearchRestaurant(context: Context) {
    abstract fun searchRestaurants(searchFilter: SearchFilter)
}