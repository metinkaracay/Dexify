package com.example.dexify.feature.pokedex.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.example.dexify.core.database.PokemonDatabase
import com.example.dexify.core.database.dao.PokemonDao
import com.example.dexify.core.database.entity.PokemonEntity
import com.example.dexify.core.network.Constants
import com.example.dexify.core.network.PokeApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val apiService: PokeApiService,
    private val database: PokemonDatabase
) : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            val count = pokemonDao.getCount()
            if (count > 0) {
                _isReady.value = true
            } else {
                seedInitialData()
            }
        }
    }

    private suspend fun seedInitialData() {
        try {
            val response = apiService.getPokemonList(limit = 40, offset = 0)
            val entities = response.results.map { entry ->
                val id = entry.url.trimEnd('/').substringAfterLast('/').toInt()
                PokemonEntity(
                    id = id,
                    name = entry.name,
                    imageUrl = "${Constants.ARTWORK_BASE_URL}${id}.png"
                )
            }
            database.withTransaction {
                pokemonDao.insertAll(entities)
            }
            _isReady.value = true
        } catch (_: Exception) {
            pokemonDao.getCountFlow().collect { count ->
                if (count > 0) _isReady.value = true
            }
        }
    }
}
