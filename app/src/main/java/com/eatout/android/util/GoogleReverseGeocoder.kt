package com.eatout.android.util

import android.app.Activity
import android.content.Context
import android.location.Location
import android.util.Log
import com.eatout.android.util.zomato.events.LocationUpdateEvent
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.PendingResult
import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng
import org.greenrobot.eventbus.EventBus

/**
 * Created by prashant.gup on 28/08/17.
 */
object GoogleReverseGeocoder {

    private val TAG = GoogleReverseGeocoder::class.java.simpleName

    fun reverseGeoEncode(context: Context, location: Location?) {
        if(location != null) {
            val geoAPIContext = GeoApiContext.Builder().apiKey(PropertyUtil.getProperty("googleAPIKey")).build()

            GeocodingApi.reverseGeocode(geoAPIContext, LatLng(location.latitude, location.longitude)).setCallback(object : PendingResult.Callback<Array<GeocodingResult>> {
                override fun onResult(result: Array<GeocodingResult>) {
                    (context as Activity).runOnUiThread { EventBus.getDefault().post(LocationUpdateEvent(location, result[0].formattedAddress)) }
                }

                override fun onFailure(e: Throwable) {
                    Log.e(TAG, "Failed to get Location from google reverse geocode")
                    e.printStackTrace()
                }
            })


        }
    }
}