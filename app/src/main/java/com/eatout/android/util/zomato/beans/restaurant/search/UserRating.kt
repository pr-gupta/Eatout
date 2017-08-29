package com.eatout.android.util.zomato.beans.restaurant.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by prashant.gup on 29/08/17.
 */
data class UserRating (
        @SerializedName("aggregate_rating")
        @Expose
        var aggregateRating: String,

        @SerializedName("rating_text")
        @Expose
        var ratingText: String,

        @SerializedName("rating_color")
        @Expose
        var ratingColor: String,

        @SerializedName("votes")
        @Expose
        var votes: String
)