package com.eatout.android.util.zomato.beans.common.categories

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by prashant.gup on 24/08/17.
 */
data class CategoryItem (@SerializedName("id")
                         @Expose
                         var _id: Int = 0,

                         @SerializedName("name")
                         @Expose
                         var _name: String = ""
)