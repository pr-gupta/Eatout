package com.eatout.android.util

import android.content.Context
import android.net.ConnectivityManager


/**
 * Created by prashant.gup on 14/09/17.
 */
class NetworkUtil {
    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}