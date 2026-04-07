package com.example.dexify.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dexify.core.database.dao.PokemonDao
import com.example.dexify.core.database.entity.PokemonEntity

@Database(
    entities = [PokemonEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
