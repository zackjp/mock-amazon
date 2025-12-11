package com.example.mockamazon.app.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.mockamazon.features.cart.CartScreenRoot
import com.example.mockamazon.features.home.HomeScreenRoot
import com.example.mockamazon.features.product.ProductScreenRoot
import com.example.mockamazon.features.search.SearchResultsScreenRoot
import com.example.mockamazon.features.search.SearchScreenRoot
import com.example.mockamazon.shared.ui.ComingSoonScreen


@Composable
fun AmazonNav3Display(
    backStackState: BackStackState,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onPerformSearch: (String) -> Unit = {},
    onViewProduct: (Int) -> Unit = {},
) {
    NavDisplay(
        backStack = backStackState.backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        modifier = modifier,
        onBack = onBack,
        transitionSpec = {
            ContentTransform(
                EnterTransition.None,
                ExitTransition.None,
            )
        },
        popTransitionSpec = {
            ContentTransform(
                EnterTransition.None,
                slideOutHorizontally { it },
            )
        },
        predictivePopTransitionSpec = {
            ContentTransform(
                EnterTransition.None,
                slideOutHorizontally { it },
            )
        },
    ) { key ->
        when (key) {
            HomeStart -> {
                NavEntry(
                    key,
                    metadata = NO_BACK_TRANSITION,
                ) {
                    HomeScreenRoot(
                        innerPadding = innerPadding,
                        modifier = Modifier,
                        onViewProduct = onViewProduct,
                    )
                }
            }

            ProfileStart ->
                NavEntry(
                    key,
                    metadata = NO_BACK_TRANSITION,
                ) {
                    ComingSoonScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        title = "Profile",
                    )
                }

            CartStart ->
                NavEntry(
                    key,
                    metadata = NO_BACK_TRANSITION,
                ) {
                    CartScreenRoot(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        onViewProduct = onViewProduct,
                    )
                }

            ShortcutsStart ->
                NavEntry(
                    key,
                    metadata = NO_BACK_TRANSITION,
                ) {
                    ComingSoonScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        title = "Shortcuts",
                    )
                }

            is ViewProduct -> NavEntry(key) {
                ProductScreenRoot(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
                    onViewProduct = onViewProduct,
                    productId = key.productId,
                )
            }

            is Search -> NavEntry(key) {
                SearchScreenRoot(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
                    onPerformSearch = onPerformSearch,
                )
            }

            is SearchResults -> NavEntry(key) {
                SearchResultsScreenRoot(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
                    onViewProduct = onViewProduct,
                    searchString = key.searchString,
                )
            }


            else -> NavEntry(key) {
                error("Route not recognized: $key")
            }
        }
    }
}

private val NO_BACK_TRANSITION = NavDisplay.popTransitionSpec {
    ContentTransform(EnterTransition.None, ExitTransition.None)
} + NavDisplay.predictivePopTransitionSpec {
    ContentTransform(EnterTransition.None, ExitTransition.None)
}