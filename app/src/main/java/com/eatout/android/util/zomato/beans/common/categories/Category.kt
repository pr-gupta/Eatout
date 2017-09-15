package com.eatout.android.util.zomato.beans.common.categories

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by prashant.gup on 23/08/17.
 */
data class Category(@SerializedName("categories")
                    @Expose
                    var categoryItem: CategoryItem = CategoryItem()
)