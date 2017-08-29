package com.eatout.android.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.View
import com.eatout.android.util.zomato.events.LocationUpdateEvent
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import java.util.*
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker.checkCallingOrSelfPermission
import com.google.android.gms.location.LocationServices
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.PendingResult
import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng


/**
 * Created by prashant.gup on 28/08/17.
 */
class GPSUtil(private val _context:Context){

    private var TAG = GPSUtil::class.java.simpleName

    private fun checkLocationPermission(): Boolean {
        val permission = "android.permission.ACCESS_FINE_LOCATION"
        val res = _context.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    fun fetchGPSLocation() {

        if(checkLocationPermission()) {
            if(getGpsStatus()) {
                getGPSLocation()
            }
            else {
                Log.v(TAG, "GPS Not Enabled")
            }
        }
        else {
            Log.w(TAG, "Location permission not granted. Asking User to provide permission")
            ActivityCompat.requestPermissions(_context as Activity , arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

    }

    private fun getGPSLocation() {
        val locationManger = _context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManger.getLastKnownLocation(locationManger.getBestProvider(Criteria(), true))

//        if(location != null) {
//            Log.i(TAG, "Found last known location of user")
//            GoogleReverseGeocoder.reverseGeoEncode(_context, location)
//        }
            Log.i(TAG, "Registering for newer location updates")
            (_context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0.1f, CustomLocationListener(_context))
    }

    private fun getGpsStatus(): Boolean =
            (_context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)

    private class CustomLocationListener(private val _context:Context): LocationListener {

        private val TAG = CustomLocationListener::class.java.simpleName

        override fun onLocationChanged(loc: Location?) {
            Log.i(TAG, "Location Changed - " + loc.toString())
            GoogleReverseGeocoder.reverseGeoEncode(_context, loc)
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(p0: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(p0: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}
