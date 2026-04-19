package com.example.dexify.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.outlined.Backpack
import androidx.compose.material.icons.outlined.CatchingPokemon
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dexify.feature.inventory.InventoryScreen
import com.example.dexify.feature.pokedex.detail.PokemonDetailScreen
import com.example.dexify.feature.pokedex.pokelist.PokedexScreen
import com.example.dexify.feature.splash.SplashScreen

// ─── Bottom Nav Items ──────────────────────────────────────
private data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem("pokedex", "Pokédex", Icons.Filled.CatchingPokemon, Icons.Outlined.CatchingPokemon),
    BottomNavItem("inventory", "Items", Icons.Filled.Backpack, Icons.Outlined.Backpack)
)

// Routes where bottom bar should be hidden
private val noBottomBarRoutes = setOf("splash", "detail/{pokemonId}")

// ─── Main Nav Host ─────────────────────────────────────────
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DexifyNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute !in noBottomBarRoutes && currentRoute != null && currentRoute != "splash"

    var isBottomBarVisible by rememberSaveable { mutableStateOf(true) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -10f) {
                    isBottomBarVisible = false
                } else if (available.y > 10f) {
                    isBottomBarVisible = true
                }
                return Offset.Zero
            }
        }
    }

    SharedTransitionLayout {
        Scaffold(
            modifier = Modifier.nestedScroll(nestedScrollConnection),
            bottomBar = {
                AnimatedVisibility(
                    visible = showBottomBar && isBottomBarVisible,
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it }
                ) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp
                    ) {
                        bottomNavItems.forEach { item ->
                            val isSelected = navBackStackEntry?.destination?.hierarchy?.any {
                                it.route == item.route
                            } == true || currentRoute == item.route

                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    if (!isSelected) {
                                        navController.navigate(item.route) {
                                            popUpTo("pokedex") { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.label,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            @Suppress("UNUSED_VARIABLE")
            val bottomBarPadding = innerPadding.calculateBottomPadding()
            NavHost(
                navController = navController,
                startDestination = "splash",
                modifier = Modifier.fillMaxSize()
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

                composable("inventory") {
                    InventoryScreen()
                }
            }
        }
    }
}
