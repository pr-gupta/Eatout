package com.eatout.android.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log


/**
 * Created by prashant.gup on 28/08/17.
 */
class GPSUtil(private val _context:Context){

    private var TAG = GPSUtil::class.java.simpleName

    companion object {
        var _latitude = 0.0
        var _longitude = 0.0
    }

    private fun checkLocationPermission(): Boolean {
        val permission = "android.permission.ACCESS_FINE_LOCATION"
        val res = _context.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    fun fetchGPSLocation() {
        Log.i(TAG, "Checking Location Permissions")
        if(checkLocationPermission()) {
            Log.i(TAG, "Location Permission Granted")
            if(getGpsStatus()) {
                Log.i(TAG, "GPS is enabled")
                getGPSLocation()
            }
            else {
                Log.w(TAG, "GPS Not Enabled")
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
        (_context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, CustomLocationListener(_context))
        (_context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0.0f, CustomLocationListener(_context))
    }

    private fun getGpsStatus(): Boolean =
            (_context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)

    private class CustomLocationListener(private val _context:Context): LocationListener {

        private val TAG = CustomLocationListener::class.java.simpleName

        override fun onLocationChanged(loc: Location?) {
            Log.i(TAG, "Location Changed - " + loc.toString())
            _latitude = loc!!.latitude
            _longitude = loc.longitude
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
