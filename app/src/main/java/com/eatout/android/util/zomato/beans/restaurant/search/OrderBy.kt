package com.eatout.android.util.zomato.beans.restaurant.search

/**
 * Created by prashant.gup on 28/08/17.
 */
enum class OrderBy(val orderParam: String) {
    ASC("asc"),
    DESC("desc"),
    NONE("none")
}