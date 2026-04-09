package com.example.dexify.feature.pokedex.detail

import androidx.compose.ui.graphics.Color
import com.example.dexify.core.designsystem.theme.*

object TypeUtils {

    private val typeColorMap = mapOf(
        "normal" to TypeNormal,
        "fire" to TypeFire,
        "water" to TypeWater,
        "electric" to TypeElectric,
        "grass" to TypeGrass,
        "ice" to TypeIce,
        "fighting" to TypeFighting,
        "poison" to TypePoison,
        "ground" to TypeGround,
        "flying" to TypeFlying,
        "psychic" to TypePsychic,
        "bug" to TypeBug,
        "rock" to TypeRock,
        "ghost" to TypeGhost,
        "dragon" to TypeDragon,
        "dark" to TypeDark,
        "steel" to TypeSteel,
        "fairy" to TypeFairy
    )

    private val defaultTypeColor = Color(0xFF68A090)
    private val defaultGradientEnd = Color(0xFF407060)

    fun getTypeColor(type: String): Color {
        return typeColorMap[type.lowercase()] ?: defaultTypeColor
    }

    fun getTypeGradient(types: List<String>?): List<Color> {
        if (types.isNullOrEmpty()) {
            return listOf(defaultTypeColor, defaultGradientEnd)
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
