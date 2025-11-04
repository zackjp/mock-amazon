package com.example.fakeamazon.app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.fakeamazon.features.cart.CartScreenRoot
import com.example.fakeamazon.features.home.HomeScreenRoot
import com.example.fakeamazon.features.product.ProductScreenRoot
import com.example.fakeamazon.shared.ui.ComingSoonScreen


@Composable
fun AmazonNavGraph(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    backHandlerForTabs: @Composable () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    onViewProduct: (Int) -> Unit = {},
) {
    NavHost(
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        popEnterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popExitTransition = { ExitTransition.None },
        navController = navController,
        route = RootGraph::class,
        startDestination = TopRoute.HomeGraph,
    ) {
        navigation(
            route = TopRoute.HomeGraph::class,
            startDestination = HomeStart,
        ) {
            composable<HomeStart> {
                backHandlerForTabs()
                HomeScreenRoot(
                    innerPadding = innerPadding,
                    modifier = Modifier,
                    onViewProduct = onViewProduct,
                )
            }
        }

        navigation(
            route = TopRoute.ProfileGraph::class,
            startDestination = ProfileStart,
        ) {
            composable<ProfileStart> {
                backHandlerForTabs()
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
                backHandlerForTabs()
                CartScreenRoot(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    onViewProduct = onViewProduct,
                )
            }
        }

        navigation(
            route = TopRoute.ShortcutsGraph::class,
            startDestination = ShortcutsStart
        ) {
            composable<ShortcutsStart> {
                backHandlerForTabs()
                ComingSoonScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    title = "Shortcuts",
                )
            }
        }

        composable<ViewProduct> { destination ->
            val route = destination.toRoute<ViewProduct>()
            ProductScreenRoot(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                onViewProduct = onViewProduct,
                productId = route.productId,
            )
        }
    }
}
