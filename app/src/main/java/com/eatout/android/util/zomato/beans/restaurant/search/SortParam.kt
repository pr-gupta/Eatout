package com.eatout.android.util.zomato.beans.restaurant.search

/**
 * Created by prashant.gup on 28/08/17.
 */
enum class SortParam(val sortParam:String) {
    NONE(""),
    COST("cost"),
    RATING("rating"),
    REAL_DISTANCE("real_distance")
}