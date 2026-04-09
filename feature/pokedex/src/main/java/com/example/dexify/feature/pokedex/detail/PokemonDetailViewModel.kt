package com.example.dexify.feature.pokedex.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dexify.core.database.dao.PokemonDao
import com.example.dexify.core.network.PokeApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.dexify.core.database.entity.PokemonEntity
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val pokemonDao: PokemonDao,
    private val apiService: PokeApiService
) : ViewModel() {

    private val pokemonId: Int = savedStateHandle.get<Int>("pokemonId")
        ?: throw IllegalArgumentException("pokemonId is required")

    val pokemon: StateFlow<PokemonEntity?> = pokemonDao.getPokemonByIdFlow(pokemonId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        loadDetailIfNeeded()
    }

    private fun loadDetailIfNeeded() {
        viewModelScope.launch {
            val entity = pokemonDao.getPokemonByIdFlow(pokemonId).firstOrNull()
            if (entity != null && entity.types == null) {
                fetchAndUpdateDetail(entity)
            }
        }
    }

    private suspend fun fetchAndUpdateDetail(entity: PokemonEntity) {
        try {
            val detailDeferred = viewModelScope.async {
                apiService.getPokemonDetail(pokemonId)
            }
            val speciesDeferred = viewModelScope.async {
                apiService.getPokemonSpecies(pokemonId)
            }

            val detail = detailDeferred.await()
            val species = speciesDeferred.await()

            val types = detail.types.sortedBy { it.slot }.map { it.type.name }
            val abilities = detail.abilities.map { it.ability.name }

            val statMap = detail.stats.associate { it.stat.name to it.baseStat }

            val flavorText = species.flavorTextEntries
                .firstOrNull { it.language.name == "en" }
                ?.flavorText
                ?.replace("\n", " ")
                ?.replace("\u000c", " ")
                ?: ""

            val genus = species.genera
                .firstOrNull { it.language.name == "en" }
                ?.genus ?: ""

            val updatedEntity = entity.copy(
                types = types,
                height = detail.height,
                weight = detail.weight,
                flavorText = flavorText,
                genus = genus,
                abilities = abilities,
                statHp = statMap["hp"],
                statAttack = statMap["attack"],
                statDefense = statMap["defense"],
                statSpAtk = statMap["special-attack"],
                statSpDef = statMap["special-defense"],
                statSpeed = statMap["speed"]
            )

            pokemonDao.update(updatedEntity)
        } catch (_: Exception) {
            // Silently fail — UI shows what's available from Room
        }
    }
}
