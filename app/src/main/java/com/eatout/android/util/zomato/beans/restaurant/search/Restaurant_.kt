package com.eatout.android.util.zomato.beans.restaurant.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by prashant.gup on 29/08/17.
 */
data class Restaurant_ (
        @SerializedName("R")
        @Expose
        var r: R = R(),

        @SerializedName("apikey")
        @Expose
        var apikey: String = "",

        @SerializedName("id")
        @Expose
        var id: String = "",

        @SerializedName("name")
        @Expose
        var name: String = "",

        @SerializedName("url")
        @Expose
        var url: String = "",

        @SerializedName("location")
        @Expose
        var location: Location = Location(),

        @SerializedName("switch_to_order_menu")
        @Expose
        var switchToOrderMenu: Int = 0,

        @SerializedName("cuisines")
        @Expose
        var cuisines: String = "",

        @SerializedName("average_cost_for_two")
        @Expose
        var averageCostForTwo: Int = 0,

        @SerializedName("price_range")
        @Expose
        var priceRange: Int = 0,

        @SerializedName("currency")
        @Expose
        var currency: String = "",

        @SerializedName("offers")
        @Expose
        var offers: List<Any> = listOf(),

        @SerializedName("thumb")
        @Expose
        var thumb: String = "",

        @SerializedName("user_rating")
        @Expose
        var userRating: UserRating = UserRating(),

        @SerializedName("photos_url")
        @Expose
        var photosUrl: String = "",

        @SerializedName("menu_url")
        @Expose
        var menuUrl: String = "",

        @SerializedName("featured_image")
        @Expose
        var featuredImage: String = "",

        @SerializedName("has_online_delivery")
        @Expose
        var hasOnlineDelivery: Int = 0,

        @SerializedName("is_delivering_now")
        @Expose
        var isDeliveringNow: Int = 0,

        @SerializedName("deeplink")
        @Expose
        var deeplink: String = "",

        @SerializedName("has_table_booking")
        @Expose
        var hasTableBooking: Int = 0,

        @SerializedName("events_url")
        @Expose
        var eventsUrl: String = "",

        @SerializedName("establishment_types")
        @Expose
        var establishmentTypes: List<Any> = listOf(),

        @SerializedName("zomato_events")
        @Expose
        var zomatoEvents: List<ZomatoEvent> = listOf()
)