package com.example.dexify.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ItemListResponse(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "results") val results: List<ItemEntry>
)

@JsonClass(generateAdapter = false)
data class ItemEntry(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)

@JsonClass(generateAdapter = false)
data class ItemDetailResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "cost") val cost: Int,
    @Json(name = "effect_entries") val effectEntries: List<ItemEffectEntry>,
    @Json(name = "flavor_text_entries") val flavorTextEntries: List<ItemFlavorText>,
    @Json(name = "category") val category: ItemCategory,
    @Json(name = "sprites") val sprites: ItemSprites
)

@JsonClass(generateAdapter = false)
data class ItemEffectEntry(
    @Json(name = "effect") val effect: String,
    @Json(name = "short_effect") val shortEffect: String,
    @Json(name = "language") val language: NamedResource
)

@JsonClass(generateAdapter = false)
data class ItemFlavorText(
    @Json(name = "text") val text: String,
    @Json(name = "language") val language: NamedResource
)

@JsonClass(generateAdapter = false)
data class ItemCategory(
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = false)
data class ItemSprites(
    @Json(name = "default") val default: String?
)

@JsonClass(generateAdapter = false)
data class NamedResource(
    @Json(name = "name") val name: String
)
