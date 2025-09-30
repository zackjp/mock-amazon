package com.example.fakeamazon.app

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.fakeamazon.base.CartStart
import com.example.fakeamazon.base.HomeStart
import com.example.fakeamazon.base.ProfileStart
import com.example.fakeamazon.base.ShortcutsStart
import com.example.fakeamazon.base.TopRoute
import com.example.fakeamazon.base.ui.ComingSoonScreen
import com.example.fakeamazon.features.home.HomeScreenRoot


@Composable
fun AmazonNavGraph(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        popEnterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popExitTransition = { ExitTransition.None },
        navController = navController,
        startDestination = TopRoute.HomeGraph,
    ) {
        navigation(
            route = TopRoute.HomeGraph::class,
            startDestination = HomeStart,
        ) {
            composable<HomeStart> {
                HomeScreenRoot(
                    innerPadding = innerPadding,
                    modifier = Modifier,
                )
            }
        }

        navigation(
            route = TopRoute.ProfileGraph::class,
            startDestination = ProfileStart,
        ) {
            composable<ProfileStart> {
                ComingSoonScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    title = "Profile",
                )
            }
        }

        navigation(
            route = TopRoute.CartGraph::class,
            startDestination = CartStart,
        ) {
            composable<CartStart> {
                ComingSoonScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    title = "Cart",
                )
            }
        }

        navigation(
            route = TopRoute.ShortcutsGraph::class,
            startDestination = ShortcutsStart
        ) {
            composable<ShortcutsStart> {
                ComingSoonScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    title = "Shortcuts",
                )
            }
        }
    }
}