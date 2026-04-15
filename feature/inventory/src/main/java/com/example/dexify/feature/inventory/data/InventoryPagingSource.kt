package com.example.dexify.feature.inventory.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.dexify.core.network.Constants
import com.example.dexify.core.network.PokeApiService
import com.example.dexify.feature.inventory.model.InventoryCategory
import com.example.dexify.feature.inventory.model.InventoryItemUiModel

class InventoryPagingSource(
    private val apiService: PokeApiService,
    private val category: InventoryCategory
) : PagingSource<Int, InventoryItemUiModel>() {

    override fun getRefreshKey(state: PagingState<Int, InventoryItemUiModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(PAGE_SIZE) ?: anchorPage?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InventoryItemUiModel> {
        val offset = params.key ?: 0
        return try {
            val items = when (category) {
                InventoryCategory.ITEMS -> fetchItems(offset, params.loadSize)
                InventoryCategory.BERRIES -> fetchBerries(offset, params.loadSize)
                InventoryCategory.MEDICINE -> fetchMedicine(offset, params.loadSize)
            }

            LoadResult.Page(
                data = items,
                prevKey = if (offset == 0) null else offset - params.loadSize,
                nextKey = if (items.isEmpty()) null else offset + params.loadSize
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun fetchItems(offset: Int, limit: Int): List<InventoryItemUiModel> {
        val listResponse = apiService.getItems(limit = limit, offset = offset)
        return listResponse.results.map { entry ->
            val id = entry.url.trimEnd('/').substringAfterLast('/').toInt()
            val detail = try {
                apiService.getItemDetail(id)
            } catch (_: Exception) { null }

            val description = detail?.flavorTextEntries
                ?.firstOrNull { it.language.name == "en" }
                ?.text?.replace("\n", " ")?.replace("\u000c", " ")
                ?: detail?.effectEntries?.firstOrNull { it.language.name == "en" }?.shortEffect
                ?: ""

            InventoryItemUiModel(
                id = id,
                name = entry.name.replace("-", " ").replaceFirstChar { it.uppercase() },
                imageUrl = "${Constants.ARTWORK_ITEMS_BASE_URL}${entry.name}.png",
                category = InventoryCategory.ITEMS,
                description = description,
                cost = detail?.cost ?: 0,
                effectRate = "—"
            )
        }
    }

    private suspend fun fetchBerries(offset: Int, limit: Int): List<InventoryItemUiModel> {
        val listResponse = apiService.getBerries(limit = limit, offset = offset)
        return listResponse.results.map { entry ->
            val id = entry.url.trimEnd('/').substringAfterLast('/').toInt()
            val detail = try {
                apiService.getBerryDetail(id)
            } catch (_: Exception) { null }

            val itemName = detail?.item?.name ?: entry.name
            val description = "Growth: ${detail?.growthTime ?: "?"}h · Max harvest: ${detail?.maxHarvest ?: "?"} · Size: ${detail?.size ?: "?"}mm"

            InventoryItemUiModel(
                id = id,
                name = entry.name.replaceFirstChar { it.uppercase() } + " Berry",
                imageUrl = null,
                category = InventoryCategory.BERRIES,
                description = description,
                cost = 0,
                effectRate = "Power: ${detail?.naturalGiftPower ?: "?"}"
            )
        }
    }

    private suspend fun fetchMedicine(offset: Int, limit: Int): List<InventoryItemUiModel> {
        val listResponse = apiService.getItems(limit = limit, offset = 16 + offset)
        return listResponse.results.map { entry ->
            val id = entry.url.trimEnd('/').substringAfterLast('/').toInt()
            val detail = try {
                apiService.getItemDetail(id)
            } catch (_: Exception) { null }

            val description = detail?.effectEntries
                ?.firstOrNull { it.language.name == "en" }
                ?.shortEffect?.replace("\n", " ")
                ?: ""

            InventoryItemUiModel(
                id = id,
                name = entry.name.replace("-", " ").replaceFirstChar { it.uppercase() },
                imageUrl = "${Constants.ARTWORK_ITEMS_BASE_URL}${entry.name}.png",
                category = InventoryCategory.MEDICINE,
                description = description,
                cost = detail?.cost ?: 0,
                effectRate = "—"
            )
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
