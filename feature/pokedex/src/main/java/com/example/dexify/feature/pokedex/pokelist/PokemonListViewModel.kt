package com.example.dexify.feature.pokedex.pokelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.dexify.core.database.dao.PokemonDao
import com.example.dexify.core.network.PokeApiService
import com.example.dexify.feature.pokedex.data.repository.PokemonPagingSource
import com.example.dexify.feature.pokedex.model.PokedexFilterState
import com.example.dexify.feature.pokedex.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val apiService: PokeApiService,
    private val pokemonDao: PokemonDao
) : ViewModel() {

    private val _filterState = MutableStateFlow(PokedexFilterState())
    val filterState: StateFlow<PokedexFilterState> = _filterState.asStateFlow()

    val pokemonPagingFlow: Flow<PagingData<Pokemon>> = _filterState
        .flatMapLatest { filters ->
                Pager(
                    config = PagingConfig(
                        pageSize = 20,
                        initialLoadSize = 20,
                        prefetchDistance = 10,
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = { 
                        PokemonPagingSource(
                            apiService = apiService,
                            pokemonDao = pokemonDao,
                            query = filters.query
                        )
                    }
                ).flow
        }
        .map { pagingData ->
            pagingData.map { entity ->
                Pokemon(
                    id = entity.id,
                    name = entity.name,
                    imageUrl = entity.imageUrl
                )
            }
        }
        .cachedIn(viewModelScope)

    fun applyFilters(newState: PokedexFilterState) {
        _filterState.value = newState
    }
}
