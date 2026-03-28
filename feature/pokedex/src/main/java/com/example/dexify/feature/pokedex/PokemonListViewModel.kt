package com.example.dexify.feature.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dexify.core.network.PokeApiService
import com.example.dexify.feature.pokedex.model.PokedexFilterState
import com.example.dexify.feature.pokedex.model.Pokemon
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
class PokemonListViewModel @Inject constructor(
    private val apiService: PokeApiService
) : ViewModel() {

    private val _filterState = MutableStateFlow(PokedexFilterState())
    val filterState: StateFlow<PokedexFilterState> = _filterState.asStateFlow()

    val pokemonPagingFlow: Flow<PagingData<Pokemon>> = _filterState
        .flatMapLatest { filters ->
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    prefetchDistance = 10,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    PokemonPagingSource(
                        apiService = apiService,
                        query = filters.query
                    )
                }
            ).flow
        }
        .cachedIn(viewModelScope)

    fun applyFilters(newState: PokedexFilterState) {
        _filterState.value = newState
    }

    fun resetFilters() {
        _filterState.value = PokedexFilterState()
    }
}
