package com.eatout.android.util.zomato.beans.restaurant.search

/**
 * Created by prashant.gup on 28/08/17.
 */
enum class EntityType (val type:String){
    NONE(""),
    CITY ("city"),
    SUB_ZONE ("subzone"),
    ZONE ("zone"),
    LANDMARK("landmark"),
    METRO("metro"),
    GROUP("group")
}