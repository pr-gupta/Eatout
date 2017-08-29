package com.eatout.android.util.zomato.beans.restaurant.search

import java.util.*

/**
 * Created by prashant.gup on 28/08/17.
 */

data class SearchFilter(
        var entityId: Int = 0,
        var entityType: EntityType = EntityType.NONE,
        var query: String = "",
        var startOffset: Int = 0,
        var maxCount: Int = 20,
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var radius: Double = 5000.0,
        var cuisines: Array<Int> = arrayOf(),
        var establishmentType: String = "",
        var collectionID: String = "",
        var category: Array<Int> = arrayOf(2),
        var sort: SortParam = SortParam.NONE,
        var orderBy: OrderBy = OrderBy.ASC
) {
    fun getStringMap() : Map<String, String> {
        val data = HashMap<String, String>()

        if(entityId != 0)
            data.put("entity_id", entityId.toString())

        if(entityType != EntityType.NONE)
            data.put("entity_type", entityType.type)

        if(query.isNotEmpty())
            data.put("q", query)

        if(startOffset != 0)
            data.put("start", startOffset.toString())

        data.put("count", maxCount.toString())

        if(latitude < -90 || latitude > 90)
            throw Exception("Invalid values for latitude found. Provided value = $latitude, acceptable range is -90 to +90")
        else
            data.put("lat", latitude.toString())

        if(longitude < -180 || longitude > 180)
            throw Exception("Invalid values for longitude found. Provided value = $longitude, acceptable range is -180 to +180")
        else
            data.put("lon", longitude.toString())

        data.put("radius", radius.toString())

        if(cuisines.isNotEmpty())
            data.put("cuisines", cuisines.joinToString())

        if(establishmentType.isNotEmpty())
            data.put("establishment_type", establishmentType)

        if(collectionID.isNotEmpty())
            data.put("collection_id", collectionID)

        if(category.isNotEmpty())
            data.put("category", category.joinToString())

        if(sort != SortParam.NONE)
            data.put("sort", sort.sortParam)

        if(orderBy != OrderBy.NONE)
            data.put("order_by", orderBy.orderParam)

        return data
    }
}