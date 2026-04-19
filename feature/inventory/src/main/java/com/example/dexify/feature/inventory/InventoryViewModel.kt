package com.example.dexify.feature.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dexify.feature.inventory.data.repository.InventoryRepository
import com.example.dexify.feature.inventory.model.InventoryCategory
import com.example.dexify.feature.inventory.model.InventoryItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import androidx.paging.filter
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: InventoryRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow(InventoryCategory.ITEMS)
    val selectedCategory: StateFlow<InventoryCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val pagingFlow: Flow<PagingData<InventoryItemUiModel>> = combine(
        _selectedCategory,
        _searchQuery
    ) { category, query -> Pair(category, query) }
        .flatMapLatest { (category, query) ->
            repository.getItemsByCategory(category).map { pagingData ->
                if (query.isBlank()) {
                    pagingData
                } else {
                    pagingData.filter { it.name.contains(query, ignoreCase = true) }
                }
            }
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
