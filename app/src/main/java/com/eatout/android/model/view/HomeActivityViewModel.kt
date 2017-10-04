package com.eatout.android.model.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.util.Log
import android.view.View
import android.widget.Toast
import com.eatout.android.FilterActivity
import com.eatout.android.util.GPSUtil
import com.wang.avi.AVLoadingIndicatorView
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by prashant.gup on 25/09/17.
 */
class HomeActivityViewModel(
        val context: Context,
        val locationInput: ObservableField<String> = ObservableField(""),
        val gpsAVLoadingIndicatorVisibility: ObservableField<Boolean> = ObservableField(false),
        val gpsLocationFound: ObservableField<Boolean> = ObservableField(false),
        val restaurantAVLoadingIndicatorViewVisibility: ObservableField<Boolean> = ObservableField(false),
        val name: ObservableField<String> = ObservableField("")
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
    private var lastTimeNoted = System.currentTimeMillis()

    init {
        if (context !is HomeActivityViewModel.HomeActivityViewModelChangedListener)
            throw throw RuntimeException(context.toString() + " must implement HomeActivityViewModelChangedListener")
        mListener = context

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate({
            Log.v(TAG, "ReP - ${System.currentTimeMillis()}, $lastTimeNoted, ${gpsAVLoadingIndicatorVisibility.get()}")
            if (((System.currentTimeMillis() - lastTimeNoted) / 1000) >= 10)
                if (gpsAVLoadingIndicatorVisibility.get() == true) {
                    gpsAVLoadingIndicatorVisibility.set(false)
                    Log.v(TAG, "Resetting gpsAVLoadingVisibility")
                    (context as Activity).runOnUiThread {
                        Toast.makeText(context, "Unable to get Location", Toast.LENGTH_LONG).show()
                    }
                }
        }, 0, 5, TimeUnit.SECONDS)
    }

    fun onFABClicked(view: View) {
        view.context.startActivity(Intent(view.context, FilterActivity::class.java))
    }

    fun onGPSButtonClicked(view: View) {
        Log.v(TAG, "GPSButtonClicked")
        gpsAVLoadingIndicatorVisibility.set(true)
        lastTimeNoted = System.currentTimeMillis()
        mListener.turnOnRestaurantSearchNeeded()
        GPSUtil(_context = context).fetchGPSLocation()
    }

    interface HomeActivityViewModelChangedListener {
        fun turnOffRestaurantSearchNeeded()
        fun turnOnRestaurantSearchNeeded()
    }
}