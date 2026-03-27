package com.example.dexify.feature.pokedex.model

data class PokedexFilterState(
    val query: String = "",
    val generationId: Int? = null,
    val typeId: String? = null,
    val habitatId: String? = null
)
