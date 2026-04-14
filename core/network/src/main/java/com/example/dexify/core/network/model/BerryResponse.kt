package com.example.dexify.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class BerryListResponse(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "results") val results: List<BerryEntry>
)

@JsonClass(generateAdapter = false)
data class BerryEntry(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)

@JsonClass(generateAdapter = false)
data class BerryDetailResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "growth_time") val growthTime: Int,
    @Json(name = "max_harvest") val maxHarvest: Int,
    @Json(name = "natural_gift_power") val naturalGiftPower: Int,
    @Json(name = "size") val size: Int,
    @Json(name = "smoothness") val smoothness: Int,
    @Json(name = "item") val item: BerryItemRef
)

@JsonClass(generateAdapter = false)
data class BerryItemRef(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)
