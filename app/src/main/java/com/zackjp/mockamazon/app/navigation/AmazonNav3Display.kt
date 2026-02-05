package com.zackjp.mockamazon.app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.zackjp.mockamazon.checkout.ui.CheckoutReviewScreenRoot
import com.zackjp.mockamazon.feature.home.HomeScreenRoot
import com.zackjp.mockamazon.features.cart.CartScreenRoot
import com.zackjp.mockamazon.feature.product.ProductScreenRoot
import com.zackjp.mockamazon.features.search.SearchResultsScreenRoot
import com.zackjp.mockamazon.features.search.SearchScreenRoot
import com.zackjp.mockamazon.shared.ui.screen.ComingSoonScreen


@Composable
fun AmazonNav3Display(
    backStackState: BackStackState,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onStartCheckout: () -> Unit = {},
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
        transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
        popTransitionSpec = { EnterTransition.None togetherWith slideOutHorizontally { it } },
        predictivePopTransitionSpec = { EnterTransition.None togetherWith slideOutHorizontally { it } },
        entryProvider = entryProvider {
            entry<HomeStart>(metadata = NO_BACK_TRANSITION) {
                HomeScreenRoot(
                    innerPadding = innerPadding,
                    modifier = Modifier,
                    onViewProduct = onViewProduct,
                )
            }
            entry<ProfileStart>(metadata = NO_BACK_TRANSITION) {
                ComingSoonScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    title = "Profile",
                )
            }
            entry<CartStart>(metadata = NO_BACK_TRANSITION) {
                CartScreenRoot(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    onStartCheckout = onStartCheckout,
                    onViewProduct = onViewProduct,
                )
            }
            entry<CheckoutReview> {
                CheckoutReviewScreenRoot(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
            entry<ShortcutsStart>(metadata = NO_BACK_TRANSITION) {
                ComingSoonScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    title = "Shortcuts",
                )
            }
            entry<ViewProduct> { key ->
                ProductScreenRoot(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
                    onViewProduct = onViewProduct,
                    productId = key.productId,
                )
            }
            entry<Search> {
                SearchScreenRoot(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
                    onPerformSearch = onPerformSearch,
                )
            }
            entry<SearchResults> { key ->
                SearchResultsScreenRoot(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
                    onViewProduct = onViewProduct,
                    searchString = key.searchString,
                )
            }
        }
    )
}

private val NO_BACK_TRANSITION =
    NavDisplay.popTransitionSpec { EnterTransition.None togetherWith ExitTransition.None } +
    NavDisplay.predictivePopTransitionSpec { EnterTransition.None togetherWith ExitTransition.None }