package com.eatout.android.model.view

import android.content.Context
import android.content.Intent
import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.util.Log
import android.view.View
import com.eatout.android.FilterActivity
import com.eatout.android.util.GPSUtil
import com.wang.avi.AVLoadingIndicatorView

/**
 * Created by prashant.gup on 25/09/17.
 */
class HomeActivityViewModel(
        val context: Context,
        val locationInput: ObservableField<String> = ObservableField(""),
        val gpsAVLoadingIndicatorVisibility: ObservableField<Boolean> = ObservableField(false),
        val gpsLocationFound: ObservableField<Boolean> = ObservableField(false),
        val restaurantAVLoadingIndicatorViewVisibility: ObservableField<Boolean> = ObservableField(false)
) {

    companion object {
        @JvmStatic
        @BindingAdapter("app:avChanged")
        fun avChanged(avLoadingIndicatorView: AVLoadingIndicatorView, avLoadingIndicatorVisibility: Boolean) {
            when (avLoadingIndicatorVisibility) {
                true -> avLoadingIndicatorView.show()
                false -> avLoadingIndicatorView.hide()
            }
        }
    }

    private var mListener: HomeActivityViewModelChangedListener
    private val TAG = javaClass.simpleName


    init {
        if (context !is HomeActivityViewModel.HomeActivityViewModelChangedListener)
            throw throw RuntimeException(context.toString() + " must implement HomeActivityViewModelChangedListener")
        mListener = context
    }

    fun onFABClicked(view: View) {
        view.context.startActivity(Intent(view.context, FilterActivity::class.java))
    }

    fun onGPSButtonClicked(view: View) {
        Log.v(TAG, "GPSButtonClicked")
        gpsAVLoadingIndicatorVisibility.set(true)
        mListener.turnOnRestaurantSearchNeeded()
        GPSUtil(_context = context).fetchGPSLocation()
    }

    interface HomeActivityViewModelChangedListener {
        fun turnOffRestaurantSearchNeeded()
        fun turnOnRestaurantSearchNeeded()
    }
}