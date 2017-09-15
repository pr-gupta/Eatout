package com.eatout.android.db

import android.provider.BaseColumns

/**
 * Created by prashant.gup on 14/09/17.
 */
class EatoutDBContract {

    /**
     * Contract to Store RestaurantDetails in DB
     */
    object Restaurants:BaseColumns {
        val TABLE_NAME = "restaurants"

        val RES_ID = "res_id"
        val NAME = "name"
        val ADDRESS = "address"
        val LATITUDE = "latitude"
        val LONGITUDE = "longitude"
        val AVERAGE_COST_FOR_TWO = "average_cost_for_two"
        val CURRENCY = "currency"
        val CUISINES = "cuisines"
        val THUMB_URL = "thumb_url"
        val FEATURED_IMAGE = "featured_image"
        val USER_RATING = "user_rating"
        val RATING_COLOR = "rating_color"
        val DOES_DELIVERS_ONLINE = "does_delivers_online"
        val IS_DELIVERING_NOW = "is_delivering_now"
        val IS_RESERVATION_AVAILABLE = "is_reservation_available"
    }

    object Categories:BaseColumns {
        val TABLE_NAME = "categories"

        val CATEGORY_NAME = "name"
        val CATEGORY_ID = "id"
    }

}