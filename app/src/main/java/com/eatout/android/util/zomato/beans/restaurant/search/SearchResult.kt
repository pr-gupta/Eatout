package com.eatout.android.util.zomato.beans.restaurant.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by prashant.gup on 29/08/17.
 */
data class SearchResult(
        @SerializedName("results_found")
        @Expose
        var resultsFound: Int,
        
        @SerializedName("results_start")
        @Expose
        var resultsStart: Int,
        
        @SerializedName("results_shown")
        @Expose
        var resultsShown: Int,
        
        @SerializedName("restaurants")
        @Expose
        var restaurants: List<Restaurant>
) 