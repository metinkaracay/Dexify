package com.example.dexify.feature.pokedex.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.dexify.core.database.dao.PokemonDao
import com.example.dexify.core.database.entity.PokemonEntity
import com.example.dexify.core.network.Constants
import com.example.dexify.core.network.PokeApiService

class PokemonPagingSource(
    private val apiService: PokeApiService,
    private val pokemonDao: PokemonDao,
    private val query: String = ""
) : PagingSource<Int, PokemonEntity>() {

    override fun getRefreshKey(state: PagingState<Int, PokemonEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(state.config.pageSize) ?: anchorPage?.nextKey?.minus(state.config.pageSize)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonEntity> {
        val offset = params.key ?: 0
        val limit = params.loadSize

        return try {
            // first control for local db
            var localData = if (query.isNotBlank()) {
                pokemonDao.searchByNamePaged(query, limit, offset)
            } else {
                pokemonDao.getPagedPokemon(limit, offset)
            }

            if (localData.isEmpty() && query.isBlank()) {
                val response = apiService.getPokemonList(limit, offset)
                val entities = response.results.map { entry ->
                    val id = entry.url.trimEnd('/').substringAfterLast('/').toInt()
                    PokemonEntity(
                        id = id,
                        name = entry.name,
                        imageUrl = "${Constants.ARTWORK_BASE_URL}${id}.png"
                    )
                }

                if (entities.isNotEmpty()) {
                    pokemonDao.insertAll(entities)
                    localData = entities
                }
            }

            LoadResult.Page(
                data = localData,
                prevKey = if (offset == 0) null else offset - limit,
                nextKey = if (localData.isEmpty()) null else offset + limit
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
