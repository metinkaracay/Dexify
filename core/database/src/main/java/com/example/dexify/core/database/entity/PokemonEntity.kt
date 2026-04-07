package com.example.dexify.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val flavorText: String? = null,
    val genus: String? = null,
    val abilities: List<String>? = null,
    val statHp: Int? = null,
    val statAttack: Int? = null,
    val statDefense: Int? = null,
    val statSpAtk: Int? = null,
    val statSpDef: Int? = null,
    val statSpeed: Int? = null
)
