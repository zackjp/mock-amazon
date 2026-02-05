package com.zackjp.mockamazon.app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideOutHorizontally
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
import com.zackjp.mockamazon.checkout.ui.CheckoutReviewScreenRoot
import com.zackjp.mockamazon.feature.cart.CartScreenRoot
import com.zackjp.mockamazon.feature.home.HomeScreenRoot
import com.zackjp.mockamazon.feature.product.ProductScreenRoot
import com.zackjp.mockamazon.features.search.SearchResultsScreenRoot
import com.zackjp.mockamazon.features.search.SearchScreenRoot
import com.zackjp.mockamazon.shared.ui.screen.ComingSoonScreen


@Composable
fun AmazonNavGraph(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    backHandlerForTabs: @Composable () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    onStartCheckout: () -> Unit = {},
    onPerformSearch: (String) -> Unit = {},
    onViewProduct: (Int) -> Unit = {},
) {
    NavHost(
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        popEnterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popExitTransition = { slideOutHorizontally { it } },
        navController = navController,
        route = RootGraph::class,
        startDestination = BottomTab.Home,
    ) {
        navigation(
            route = BottomTab.Home::class,
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
            route = BottomTab.Profile::class,
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
            route = BottomTab.Cart::class,
            startDestination = CartStart,
        ) {
            composable<CartStart> {
                backHandlerForTabs()
                CartScreenRoot(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    onStartCheckout = onStartCheckout,
                    onViewProduct = onViewProduct,
                )
            }
            composable<CheckoutReview> {
                CheckoutReviewScreenRoot(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                )
            }
        }

        navigation(
            route = BottomTab.Shortcuts::class,
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

        composable<Search> { destination ->
            SearchScreenRoot(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                onPerformSearch = onPerformSearch,
            )
        }

        composable<SearchResults> { destination ->
            val route = destination.toRoute<SearchResults>()
            SearchResultsScreenRoot(
                modifier = Modifier.padding(innerPadding).fillMaxWidth(),
                onViewProduct = onViewProduct,
                searchString = route.searchString,
            )
        }
    }
}
