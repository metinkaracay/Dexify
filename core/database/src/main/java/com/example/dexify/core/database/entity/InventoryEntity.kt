package com.example.dexify.core.database.entity

import androidx.room.Entity

@Entity(tableName = "inventory", primaryKeys = ["id", "category"])
data class InventoryEntity(
    val id: Int,
    val name: String,
    val category: String,
    val imageUrl: String?,
    val description: String,
    val cost: Int,
    val effectRate: String
)
