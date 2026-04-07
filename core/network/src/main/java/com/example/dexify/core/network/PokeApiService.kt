package com.example.dexify.core.network

import com.example.dexify.core.network.model.PokemonDetailResponse
import com.example.dexify.core.network.model.PokemonListResponse
import com.example.dexify.core.network.model.PokemonSpeciesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetailResponse

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): PokemonSpeciesResponse

    companion object {
        const val BASE_URL = Constants.POKE_API_BASE_URL
    }
}
