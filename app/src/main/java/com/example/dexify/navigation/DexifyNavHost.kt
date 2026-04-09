package com.example.dexify.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dexify.feature.pokedex.detail.PokemonDetailScreen
import com.example.dexify.feature.pokedex.pokelist.PokedexScreen
import com.example.dexify.feature.pokedex.splash.SplashScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DexifyNavHost() {
    val navController = rememberNavController()

    SharedTransitionLayout {
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
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }
            composable(
                route = "detail/{pokemonId}",
                arguments = listOf(
                    navArgument("pokemonId") { type = NavType.IntType }
                )
            ) {
                PokemonDetailScreen(
                    onBackClick = { navController.navigateUp() },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }
        }
    }
}
