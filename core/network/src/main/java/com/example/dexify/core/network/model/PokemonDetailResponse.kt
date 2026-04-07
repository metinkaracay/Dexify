package com.example.dexify.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class PokemonDetailResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "weight") val weight: Int,
    @Json(name = "types") val types: List<TypeSlot>,
    @Json(name = "stats") val stats: List<StatSlot>,
    @Json(name = "abilities") val abilities: List<AbilitySlot>
)

@JsonClass(generateAdapter = false)
data class TypeSlot(
    @Json(name = "slot") val slot: Int,
    @Json(name = "type") val type: TypeInfo
)

@JsonClass(generateAdapter = false)
data class TypeInfo(
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = false)
data class StatSlot(
    @Json(name = "base_stat") val baseStat: Int,
    @Json(name = "stat") val stat: StatInfo
)

@JsonClass(generateAdapter = false)
data class StatInfo(
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = false)
data class AbilitySlot(
    @Json(name = "ability") val ability: AbilityInfo
)

@JsonClass(generateAdapter = false)
data class AbilityInfo(
    @Json(name = "name") val name: String
)
