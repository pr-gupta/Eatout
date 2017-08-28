package com.eatout.android.util.zomato.beans

/**
 * Created by prashant.gup on 28/08/17.
 */
data class SearchFilter(val entityId: Int = 0,
                        val entityType: EntityType = EntityType.NONE,
                        val query: String = "",
                        val startOffset: Int = 0,
                        val maxCount: Int = 20,
                        val latitude: Double = 0.0,
                        val longitude: Double = 0.0,
                        val radius: Double = 5000.0,
                        val cuisines: String = "",
                        val establishmentType: String = "",
                        val collectionID: String = "",
                        val category: String = "2",
                        val sort: SortParam = SortParam.NONE,
                        val orderBy: OrderBy = OrderBy.ASC
)