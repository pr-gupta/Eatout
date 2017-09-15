package com.eatout.android.util.zomato.controller

import android.content.Context
import android.location.Location
import android.util.Log
import com.eatout.android.db.DBRestaurantHelper
import com.eatout.android.util.GPSUtil
import com.eatout.android.util.zomato.beans.restaurant.search.Restaurant
import com.eatout.android.util.zomato.beans.restaurant.search.SearchFilter
import com.eatout.android.util.zomato.beans.restaurant.search.SearchResult
import com.eatout.android.util.zomato.events.SearchRestaurantCompletionEvent
import org.greenrobot.eventbus.EventBus

/**
 * Created by prashant.gup on 14/09/17.
 */
class SearchRestaurantControllerOffline(val context: Context): ISearchRestaurant(context) {

    private val TAG = javaClass.simpleName

    override fun searchRestaurants(searchFilter: SearchFilter) {

        Log.v(TAG, "In offline mode")

        val searchResult = SearchResult()

        searchResult.restaurants = DBRestaurantHelper(context).getRestaurantList(SearchFilter())

        val removeRestaurantList = searchResult.restaurants.filter { shouldRemoveRestaurant(it, searchFilter) }

        searchResult.restaurants.removeAll(removeRestaurantList)

        searchResult.resultsFound = searchResult.restaurants.size
        searchResult.resultsStart = 0
        searchResult.resultsShown = searchResult.restaurants.size
        Log.d(TAG, searchResult.toString())
        EventBus.getDefault().post(SearchRestaurantCompletionEvent(searchResult))
    }

    private fun shouldRemoveRestaurant(restaurant: Restaurant, searchFilter: SearchFilter):Boolean {

        if(distance(restaurant) > searchFilter.radius)
            return true
        return false
    }

    private fun distance(restaurant: Restaurant):Double {
        val locA = Location("loc1")
        locA.latitude = GPSUtil._latitude
        locA.longitude = GPSUtil._longitude

        val locB = Location("loc2")
        locB.latitude = restaurant.restaurant.location.latitude.toDouble()
        locB.longitude = restaurant.restaurant.location.longitude.toDouble()

        Log.v(TAG, locA.distanceTo(locB).toString())

        return locA.distanceTo(locB).toDouble()
    }


}