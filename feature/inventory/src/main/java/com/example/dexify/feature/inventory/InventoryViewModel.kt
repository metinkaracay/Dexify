package com.example.dexify.feature.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dexify.core.network.PokeApiService
import com.example.dexify.feature.inventory.data.InventoryPagingSource
import com.example.dexify.feature.inventory.model.InventoryCategory
import com.example.dexify.feature.inventory.model.InventoryItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val apiService: PokeApiService
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow(InventoryCategory.ITEMS)
    val selectedCategory: StateFlow<InventoryCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val pagingFlow: Flow<PagingData<InventoryItemUiModel>> = _selectedCategory
        .flatMapLatest { category ->
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    initialLoadSize = 20,
                    prefetchDistance = 10,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    InventoryPagingSource(
                        apiService = apiService,
                        category = category
                    )
                }
            ).flow
        }
        .cachedIn(viewModelScope)

    fun selectCategory(category: InventoryCategory) {
        if (_selectedCategory.value == category) return
        _selectedCategory.value = category
    }

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }
}
