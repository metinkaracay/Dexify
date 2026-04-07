package com.example.dexify.feature.pokedex.detail

import androidx.compose.ui.graphics.Color

object TypeUtils {

    private val typeColorMap = mapOf(
        "normal" to Color(0xFFA8A77A),
        "fire" to Color(0xFFEE8130),
        "water" to Color(0xFF6390F0),
        "electric" to Color(0xFFF7D02C),
        "grass" to Color(0xFF7AC74C),
        "ice" to Color(0xFF96D9D6),
        "fighting" to Color(0xFFC22E28),
        "poison" to Color(0xFFA33EA1),
        "ground" to Color(0xFFE2BF65),
        "flying" to Color(0xFFA98FF3),
        "psychic" to Color(0xFFF95587),
        "bug" to Color(0xFFA6B91A),
        "rock" to Color(0xFFB6A136),
        "ghost" to Color(0xFF735797),
        "dragon" to Color(0xFF6F35FC),
        "dark" to Color(0xFF705746),
        "steel" to Color(0xFFB7B7CE),
        "fairy" to Color(0xFFD685AD)
    )

    fun getTypeColor(type: String): Color {
        return typeColorMap[type.lowercase()] ?: Color(0xFF68A090)
    }

    fun getTypeGradient(types: List<String>?): List<Color> {
        if (types.isNullOrEmpty()) {
            return listOf(Color(0xFF68A090), Color(0xFF407060))
        }
        val primary = getTypeColor(types.first())
        val secondary = if (types.size > 1) {
            getTypeColor(types[1])
        } else {
            primary.copy(alpha = 0.7f)
        }
        return listOf(primary, secondary)
    }
}
