package com.eatout.android.util.zomato.beans.restaurant.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by prashant.gup on 29/08/17.
 */
data class Location (
        @SerializedName("address")
        @Expose
        var address: String = "",
        
        @SerializedName("locality")
        @Expose
        var locality: String = "",
        
        @SerializedName("city")
        @Expose
        var city: String = "",
        
        @SerializedName("city_id")
        @Expose
        var cityId: Int = 0,
        
        @SerializedName("latitude")
        @Expose
        var latitude: String = "",
        
        @SerializedName("longitude")
        @Expose
        var longitude: String = "",
        
        @SerializedName("zipcode")
        @Expose
        var zipcode: String = "",
        
        @SerializedName("country_id")
        @Expose
        var countryId: Int = 0,
        
        @SerializedName("locality_verbose")
        @Expose
        var localityVerbose: String = ""
)