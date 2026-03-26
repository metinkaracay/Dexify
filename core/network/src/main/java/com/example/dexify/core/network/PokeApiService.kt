package com.example.dexify.core.network

import com.example.dexify.core.network.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
    }
}
