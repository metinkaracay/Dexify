package com.example.dexify.core.network

import com.example.dexify.core.network.model.BerryDetailResponse
import com.example.dexify.core.network.model.BerryListResponse
import com.example.dexify.core.network.model.ItemDetailResponse
import com.example.dexify.core.network.model.ItemListResponse
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

    @GET("item")
    suspend fun getItems(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ItemListResponse

    @GET("item/{id}")
    suspend fun getItemDetail(
        @Path("id") id: Int
    ): ItemDetailResponse

    @GET("berry")
    suspend fun getBerries(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): BerryListResponse

    @GET("berry/{id}")
    suspend fun getBerryDetail(
        @Path("id") id: Int
    ): BerryDetailResponse

    companion object {
        const val BASE_URL = Constants.POKE_API_BASE_URL
    }
}
