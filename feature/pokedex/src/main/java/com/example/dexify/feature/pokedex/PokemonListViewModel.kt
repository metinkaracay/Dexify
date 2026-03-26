package com.example.dexify.feature.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dexify.core.network.PokeApiService
import com.example.dexify.feature.pokedex.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val apiService: PokeApiService
) : ViewModel() {

    val pokemonPagingFlow: Flow<PagingData<Pokemon>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PokemonPagingSource(apiService) }
    ).flow.cachedIn(viewModelScope)
}
