package com.example.dexify.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class PokemonSpeciesResponse(
    @Json(name = "flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>,
    @Json(name = "genera") val genera: List<GenusEntry>
)

@JsonClass(generateAdapter = false)
data class FlavorTextEntry(
    @Json(name = "flavor_text") val flavorText: String,
    @Json(name = "language") val language: LanguageInfo
)

@JsonClass(generateAdapter = false)
data class GenusEntry(
    @Json(name = "genus") val genus: String,
    @Json(name = "language") val language: LanguageInfo
)

@JsonClass(generateAdapter = false)
data class LanguageInfo(
    @Json(name = "name") val name: String
)
