package com.example.dexify.feature.inventory.data.repository

import androidx.paging.PagingData
import com.example.dexify.feature.inventory.model.InventoryCategory
import com.example.dexify.feature.inventory.model.InventoryItemUiModel
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {
    fun getItemsByCategory(category: InventoryCategory): Flow<PagingData<InventoryItemUiModel>>
}
