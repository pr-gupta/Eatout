package com.eatout.android.util.zomato.beans.restaurant.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by prashant.gup on 29/08/17.
 */
data class Event(
        @SerializedName("event_id")
        @Expose
        var eventId: Int,
        
        @SerializedName("friendly_start_date")
        @Expose
        var friendlyStartDate: String,
        
        @SerializedName("friendly_end_date")
        @Expose
        var friendlyEndDate: String,
        
        @SerializedName("start_date")
        @Expose
        var startDate: String,
        
        @SerializedName("end_date")
        @Expose
        var endDate: String,
        
        @SerializedName("end_time")
        @Expose
        var endTime: String,
        
        @SerializedName("start_time")
        @Expose
        var startTime: String,
        
        @SerializedName("is_active")
        @Expose
        var isActive: Int,
        
        @SerializedName("date_added")
        @Expose
        var dateAdded: String,
        
        @SerializedName("photos")
        @Expose
        var photos: List<Photo>,
        
        @SerializedName("restaurants")
        @Expose
        var restaurants: List<Any>,
        
        @SerializedName("is_varid")
        @Expose
        var isvarid: Int,
        
        @SerializedName("share_url")
        @Expose
        var shareUrl: String,
        
        @SerializedName("title")
        @Expose
        var title: String,
        
        @SerializedName("description")
        @Expose
        var description: String,
        
        @SerializedName("display_time")
        @Expose
        var displayTime: String,
        
        @SerializedName("display_date")
        @Expose
        var displayDate: String,
        
        @SerializedName("is_end_time_set")
        @Expose
        var isEndTimeSet: Int,
        
        @SerializedName("disclaimer")
        @Expose
        var disclaimer: String,
        
        @SerializedName("event_category")
        @Expose
        var eventCategory: Int,
        
        @SerializedName("event_category_name")
        @Expose
        var eventCategoryName: String,
        
        @SerializedName("book_link")
        @Expose
        var bookLink: String
)