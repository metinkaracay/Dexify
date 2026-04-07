package com.example.dexify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dexify.feature.pokedex.detail.PokemonDetailScreen
import com.example.dexify.feature.pokedex.pokelist.PokedexScreen
import com.example.dexify.feature.pokedex.splash.SplashScreen

@Composable
fun DexifyNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToPokedex = {
                    navController.navigate("pokedex") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("pokedex") {
            PokedexScreen(
                onPokemonClick = { pokemonId ->
                    navController.navigate("detail/$pokemonId")
                }
            )
        }
        composable(
            route = "detail/{pokemonId}",
            arguments = listOf(
                navArgument("pokemonId") { type = NavType.IntType }
            )
        ) {
            PokemonDetailScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
