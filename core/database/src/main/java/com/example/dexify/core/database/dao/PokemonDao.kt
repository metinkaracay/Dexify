package com.example.dexify.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dexify.core.database.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<PokemonEntity>)

    @Update
    suspend fun update(pokemon: PokemonEntity)

    @Query("DELETE FROM pokemon")
    suspend fun clearAll()

    @Query("SELECT * FROM pokemon ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getPagedPokemon(limit: Int, offset: Int): List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :query || '%' ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun searchByNamePaged(query: String, limit: Int, offset: Int): List<PokemonEntity>

    @Query("SELECT COUNT(*) FROM pokemon")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM pokemon")
    fun getCountFlow(): Flow<Int>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun getPokemonByIdFlow(id: Int): Flow<PokemonEntity?>
}
