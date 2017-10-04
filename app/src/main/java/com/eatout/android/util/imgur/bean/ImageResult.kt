package com.eatout.android.util.imgur.bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by prashant.gup on 03/10/17.
 */
data class ImageResult(
        @SerializedName("data")
        @Expose
        val data: Data,
        @SerializedName("success")
        @Expose
        val success: Boolean,
        @SerializedName("status")
        @Expose
        val status: Int
)