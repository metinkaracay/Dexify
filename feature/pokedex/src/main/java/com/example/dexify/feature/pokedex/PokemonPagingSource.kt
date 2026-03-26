package com.example.dexify.feature.pokedex

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.dexify.core.network.PokeApiService
import com.example.dexify.feature.pokedex.model.Pokemon

class PokemonPagingSource(
    private val apiService: PokeApiService
) : PagingSource<Int, Pokemon>() {

    companion object {
        private const val STARTING_OFFSET = 0
        private const val PAGE_SIZE = 20
        private const val ARTWORK_BASE_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
    }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(PAGE_SIZE) ?: anchorPage?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val offset = params.key ?: STARTING_OFFSET

        return try {
            val response = apiService.getPokemonList(
                limit = PAGE_SIZE,
                offset = offset
            )

            val pokemonList = response.results.map { entry ->
                val id = extractIdFromUrl(entry.url)
                Pokemon(
                    id = id,
                    name = entry.name,
                    imageUrl = "${ARTWORK_BASE_URL}${id}.png"
                )
            }

            LoadResult.Page(
                data = pokemonList,
                prevKey = if (offset == STARTING_OFFSET) null else offset - PAGE_SIZE,
                nextKey = if (response.next == null) null else offset + PAGE_SIZE
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun extractIdFromUrl(url: String): Int {
        return url.trimEnd('/').substringAfterLast('/').toInt()
    }
}
