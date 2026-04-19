package com.example.dexify.feature.inventory.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.dexify.core.database.dao.InventoryDao
import com.example.dexify.core.database.entity.InventoryEntity
import com.example.dexify.core.network.Constants
import com.example.dexify.core.network.PokeApiService
import com.example.dexify.feature.inventory.model.InventoryCategory

@OptIn(ExperimentalPagingApi::class)
class InventoryRemoteMediator(
    private val apiService: PokeApiService,
    private val inventoryDao: InventoryDao,
    private val category: InventoryCategory
) : RemoteMediator<Int, InventoryEntity>() {

    override suspend fun initialize(): InitializeAction {
        val count = inventoryDao.countByCategory(category.name)
        return if (count > 0) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, InventoryEntity>
    ): MediatorResult {
        val offset = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> inventoryDao.countByCategory(category.name)
        }

        return try {
            val entities = when (category) {
                InventoryCategory.ITEMS -> fetchItems(offset, state.config.pageSize)
                InventoryCategory.BERRIES -> fetchBerries(offset, state.config.pageSize)
                InventoryCategory.MEDICINE -> fetchMedicine(offset, state.config.pageSize)
            }

            if (loadType == LoadType.REFRESH) {
                inventoryDao.clearByCategory(category.name)
            }

            inventoryDao.insertAll(entities)

            MediatorResult.Success(endOfPaginationReached = entities.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun fetchItems(offset: Int, limit: Int): List<InventoryEntity> {
        val response = apiService.getItems(limit = limit, offset = offset)
        return response.results.map { entry ->
            val id = entry.url.trimEnd('/').substringAfterLast('/').toInt()
            val detail = try { apiService.getItemDetail(id) } catch (_: Exception) { null }

            val description = detail?.flavorTextEntries
                ?.firstOrNull { it.language.name == "en" }
                ?.text?.replace("\n", " ")?.replace("\u000c", " ")
                ?: detail?.effectEntries?.firstOrNull { it.language.name == "en" }?.shortEffect
                ?: ""

            InventoryEntity(
                id = id,
                name = entry.name.replace("-", " ").replaceFirstChar { it.uppercase() },
                category = InventoryCategory.ITEMS.name,
                imageUrl = "${Constants.ARTWORK_ITEMS_BASE_URL}${entry.name}.png",
                description = description,
                cost = detail?.cost ?: 0,
                effectRate = "—"
            )
        }
    }

    private suspend fun fetchBerries(offset: Int, limit: Int): List<InventoryEntity> {
        val response = apiService.getBerries(limit = limit, offset = offset)
        return response.results.map { entry ->
            val id = entry.url.trimEnd('/').substringAfterLast('/').toInt()
            val detail = try { apiService.getBerryDetail(id) } catch (_: Exception) { null }

            val description = "Growth: ${detail?.growthTime ?: "?"}h · Max harvest: ${detail?.maxHarvest ?: "?"} · Size: ${detail?.size ?: "?"}mm"

            InventoryEntity(
                id = id,
                name = entry.name.replaceFirstChar { it.uppercase() } + " Berry",
                category = InventoryCategory.BERRIES.name,
                imageUrl = null,
                description = description,
                cost = 0,
                effectRate = "Power: ${detail?.naturalGiftPower ?: "?"}"
            )
        }
    }

    private suspend fun fetchMedicine(offset: Int, limit: Int): List<InventoryEntity> {
        val response = apiService.getItems(limit = limit, offset = 16 + offset)
        return response.results.map { entry ->
            val id = entry.url.trimEnd('/').substringAfterLast('/').toInt()
            val detail = try { apiService.getItemDetail(id) } catch (_: Exception) { null }

            val description = detail?.effectEntries
                ?.firstOrNull { it.language.name == "en" }
                ?.shortEffect?.replace("\n", " ")
                ?: ""

            InventoryEntity(
                id = id,
                name = entry.name.replace("-", " ").replaceFirstChar { it.uppercase() },
                category = InventoryCategory.MEDICINE.name,
                imageUrl = "${Constants.ARTWORK_ITEMS_BASE_URL}${entry.name}.png",
                description = description,
                cost = detail?.cost ?: 0,
                effectRate = "—"
            )
        }
    }
}
