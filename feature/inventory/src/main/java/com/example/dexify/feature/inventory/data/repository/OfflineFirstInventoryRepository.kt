package com.example.dexify.feature.inventory.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.dexify.core.database.dao.InventoryDao
import com.example.dexify.core.network.PokeApiService
import com.example.dexify.feature.inventory.data.InventoryRemoteMediator
import com.example.dexify.feature.inventory.model.InventoryCategory
import com.example.dexify.feature.inventory.model.InventoryItemUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstInventoryRepository @Inject constructor(
    private val inventoryDao: InventoryDao,
    private val apiService: PokeApiService
) : InventoryRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getItemsByCategory(category: InventoryCategory): Flow<PagingData<InventoryItemUiModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 10,
                enablePlaceholders = false
            ),
            remoteMediator = InventoryRemoteMediator(
                apiService = apiService,
                inventoryDao = inventoryDao,
                category = category
            ),
            pagingSourceFactory = {
                inventoryDao.getByCategoryPagingSource(category.name)
            }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                InventoryItemUiModel(
                    id = entity.id,
                    name = entity.name,
                    imageUrl = entity.imageUrl,
                    category = category,
                    description = entity.description,
                    cost = entity.cost,
                    effectRate = entity.effectRate
                )
            }
        }
    }
}
