package com.eatout.android.util.zomato.controller

import android.content.Context

/**
 * Created by prashant.gup on 14/09/17.
 */
abstract class IRestaurantDetail(context: Context) {
    abstract fun getRestaurantDetails(resId: String)
}