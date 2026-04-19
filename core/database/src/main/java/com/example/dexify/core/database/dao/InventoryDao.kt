package com.example.dexify.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dexify.core.database.entity.InventoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {

    @Query("SELECT * FROM inventory WHERE category = :category ORDER BY id ASC")
    fun getByCategoryPagingSource(category: String): PagingSource<Int, InventoryEntity>

    @Query("SELECT * FROM inventory WHERE category = :category ORDER BY id ASC")
    fun getByCategoryFlow(category: String): Flow<List<InventoryEntity>>

    @Query("SELECT COUNT(*) FROM inventory WHERE category = :category")
    suspend fun countByCategory(category: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<InventoryEntity>)

    @Query("DELETE FROM inventory WHERE category = :category")
    suspend fun clearByCategory(category: String)
}

