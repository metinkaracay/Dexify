package com.example.dexify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dexify.feature.pokedex.PokedexScreen

@Composable
fun DexifyNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "pokedex"
    ) {
        composable("pokedex") {
            PokedexScreen()
        }
    }
}
