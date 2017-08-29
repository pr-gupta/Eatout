package com.eatout.android.util.zomato.beans.restaurant.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by prashant.gup on 29/08/17.
 */
data class Photo_(
        @SerializedName("url")
        @Expose
        private val url: String,

        @SerializedName("thumb_url")
        @Expose
        private val thumbUrl: String,

        @SerializedName("order")
        @Expose
        private val order: Int,

        @SerializedName("md5sum")
        @Expose
        private val md5sum: String,

        @SerializedName("photo_id")
        @Expose
        private val photoId: Int,

        @SerializedName("uuid")
        @Expose
        private val uuid: Long,

        @SerializedName("type")
        @Expose
        private val type: String
)