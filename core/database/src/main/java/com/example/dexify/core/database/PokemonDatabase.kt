package com.example.dexify.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dexify.core.database.dao.PokemonDao
import com.example.dexify.core.database.entity.PokemonEntity

@Database(
    entities = [PokemonEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
