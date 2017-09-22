package com.eatout.android.model.view

import android.databinding.ObservableField

/**
 * Created by prashant.gup on 20/09/17.
 */
class RestaurantCardViewModel (
        val restaurantTitle: ObservableField<String> = ObservableField(""),
        val restaurantCost: ObservableField<String> = ObservableField(""),
        val restaurantRatingValue: ObservableField<Float> = ObservableField(0.0f),
        val restaurantRatingColor: ObservableField<Int> = ObservableField(0)
){
}