package com.eatout.android.util.imgur.bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by prashant.gup on 03/10/17.
 */
data class Data(
        @SerializedName("id")
        @Expose
        val id: String,
        @SerializedName("title")
        @Expose
        val title: Any,
        @SerializedName("description")
        @Expose
        val description: Any,
        @SerializedName("datetime")
        @Expose
        val datetime: Int,
        @SerializedName("type")
        @Expose
        val type: String,
        @SerializedName("animated")
        @Expose
        val animated: Boolean,
        @SerializedName("width")
        @Expose
        val width: Int,
        @SerializedName("height")
        @Expose
        val height: Int,
        @SerializedName("size")
        @Expose
        val size: Int,
        @SerializedName("views")
        @Expose
        val views: Int,
        @SerializedName("bandwidth")
        @Expose
        val bandwidth: Int,
        @SerializedName("vote")
        @Expose
        val vote: Any,
        @SerializedName("favorite")
        @Expose
        val favorite: Boolean,
        @SerializedName("nsfw")
        @Expose
        val nsfw: Any,
        @SerializedName("section")
        @Expose
        val section: Any,
        @SerializedName("account_url")
        @Expose
        val accountUrl: Any,
        @SerializedName("account_id")
        @Expose
        val accountId: Any,
        @SerializedName("is_ad")
        @Expose
        val isAd: Boolean,
        @SerializedName("in_most_viral")
        @Expose
        val inMostViral: Boolean,
        @SerializedName("has_sound")
        @Expose
        val hasSound: Boolean,
        @SerializedName("tags")
        @Expose
        val tags: List<Any>,
        @SerializedName("ad_type")
        @Expose
        val adType: Int,
        @SerializedName("ad_url")
        @Expose
        val adUrl: String,
        @SerializedName("in_gallery")
        @Expose
        val inGallery: Boolean,
        @SerializedName("link")
        @Expose
        val link: String
)