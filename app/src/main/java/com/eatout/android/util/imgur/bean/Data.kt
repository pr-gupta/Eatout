package com.eatout.android.util.imgur.bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by prashant.gup on 03/10/17.
 */
data class Data(
        @SerializedName("id")
        @Expose
        private val id: String,
        @SerializedName("title")
        @Expose
        private val title: Any,
        @SerializedName("description")
        @Expose
        private val description: Any,
        @SerializedName("datetime")
        @Expose
        private val datetime: Int,
        @SerializedName("type")
        @Expose
        private val type: String,
        @SerializedName("animated")
        @Expose
        private val animated: Boolean,
        @SerializedName("width")
        @Expose
        private val width: Int,
        @SerializedName("height")
        @Expose
        private val height: Int,
        @SerializedName("size")
        @Expose
        private val size: Int,
        @SerializedName("views")
        @Expose
        private val views: Int,
        @SerializedName("bandwidth")
        @Expose
        private val bandwidth: Int,
        @SerializedName("vote")
        @Expose
        private val vote: Any,
        @SerializedName("favorite")
        @Expose
        private val favorite: Boolean,
        @SerializedName("nsfw")
        @Expose
        private val nsfw: Any,
        @SerializedName("section")
        @Expose
        private val section: Any,
        @SerializedName("account_url")
        @Expose
        private val accountUrl: Any,
        @SerializedName("account_id")
        @Expose
        private val accountId: Any,
        @SerializedName("is_ad")
        @Expose
        private val isAd: Boolean,
        @SerializedName("in_most_viral")
        @Expose
        private val inMostViral: Boolean,
        @SerializedName("has_sound")
        @Expose
        private val hasSound: Boolean,
        @SerializedName("tags")
        @Expose
        private val tags: List<Any>,
        @SerializedName("ad_type")
        @Expose
        private val adType: Int,
        @SerializedName("ad_url")
        @Expose
        private val adUrl: String,
        @SerializedName("in_gallery")
        @Expose
        private val inGallery: Boolean,
        @SerializedName("link")
        @Expose
        private val link: String
)