package com.eatout.android.util.zomato.controller

import android.content.Context
import android.util.Log
import com.eatout.android.db.DBRestaurantHelper
import com.eatout.android.util.zomato.events.GetRestaurantDetailCompletionEvent
import org.greenrobot.eventbus.EventBus

/**
 * Created by prashant.gup on 14/09/17.
 */
class RestaurantDetailControllerOffline(var context: Context): IRestaurantDetail(context) {

    private val TAG = javaClass.simpleName

    override fun getRestaurantDetails(resId: String) {
        Log.v(TAG, "In offline mode [$resId]")


        val restaurants = DBRestaurantHelper(context).getRestaurant(resId)

        EventBus.getDefault().post(GetRestaurantDetailCompletionEvent(restaurants.restaurant))
    }
}