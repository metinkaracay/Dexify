package com.example.dexify.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dexify.core.database.dao.InventoryDao
import com.example.dexify.core.database.dao.PokemonDao
import com.example.dexify.core.database.entity.InventoryEntity
import com.example.dexify.core.database.entity.PokemonEntity

@Database(
    entities = [PokemonEntity::class, InventoryEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun inventoryDao(): InventoryDao
}
