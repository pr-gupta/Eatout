package com.eatout.android.model.view

import android.content.Context
import android.databinding.ObservableField

/**
 * Created by prashant.gup on 27/09/17.
 */
class RestaurantDetailViewModel(
        val context: Context,
        val isUberDataLoaded: ObservableField<Boolean> = ObservableField(false),
        val isRestaurantDataLoaded: ObservableField<Boolean> = ObservableField(false),
        val restaurantName: ObservableField<String> = ObservableField(""),
        val restaurantAddress: ObservableField<String> = ObservableField(""),
        val restaurantCost: ObservableField<String> = ObservableField(""),
        val restaurantDistance: ObservableField<String> = ObservableField(""),
        val restaurantRating: ObservableField<Float> = ObservableField(0.0f),
        val restaurantRatingColor: ObservableField<Int> = ObservableField(0),
        val restaurantCuisinesText: ObservableField<String> = ObservableField(""),
        val hasOnlineDelivery: ObservableField<Int> = ObservableField(0),
        val isDeliveringNow: ObservableField<Int> = ObservableField(0),
        val hasTableBooking: ObservableField<Int> = ObservableField(0),
        val UberRideDetails1: ObservableField<String> = ObservableField(""),
        val UberRideDetails2: ObservableField<String> = ObservableField(""),
        val UberRideDetails3: ObservableField<String> = ObservableField(""),
        val UberRideDetails4: ObservableField<String> = ObservableField("")
) {

    private val mListener: RestaurantDetailViewModelChangeListener

    init {
        if (context !is RestaurantDetailViewModel.RestaurantDetailViewModelChangeListener)
            throw throw RuntimeException(context.toString() + " must implement HomeActivityViewModelChangedListener")
        mListener = context
    }

    fun uberButtonClicked() {
        mListener.buildAndStartUberRideActivity()
    }

    fun viewMoreDetailsButtonClicked() {
        mListener.buildAndStartZomatoWebViewActivity()
    }

    interface RestaurantDetailViewModelChangeListener {
        fun buildAndStartUberRideActivity()
        fun buildAndStartZomatoWebViewActivity()
    }
}