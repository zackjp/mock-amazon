package com.example.mockamazon.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mockamazon.R
import com.example.mockamazon.app.navigation.AmazonNavGraph
import com.example.mockamazon.app.navigation.BackStackState
import com.example.mockamazon.app.navigation.BottomTab
import com.example.mockamazon.app.navigation.HomeStart
import com.example.mockamazon.app.navigation.Search
import com.example.mockamazon.app.navigation.SearchResults
import com.example.mockamazon.app.navigation.ViewProduct
import com.example.mockamazon.app.navigation.rememberNav2Controller
import com.example.mockamazon.app.ui.view.AmazonBottomAppBar
import com.example.mockamazon.app.ui.view.AmazonTopAppBar
import com.example.mockamazon.app.ui.view.AmazonTopAppBarWithNavChips
import com.example.mockamazon.app.ui.view.BottomNavItem
import com.example.mockamazon.features.home.HomeScreenRoot
import com.example.mockamazon.ui.theme.AmazonOutlineLight
import com.example.mockamazon.ui.theme.MockAmazonTheme

val AMAZON_BEIGE = Color(0xFFF5BE89)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var navChipsHeightPx by remember { mutableFloatStateOf(0f) }
    val collapsibleState = rememberCollapsibleState(maxCollapseHeightPx = -navChipsHeightPx)

    val amazonNavController = rememberNav2Controller()

    val bottomNavItems = remember(amazonNavController) {
        listOf(
            BottomNavItem(R.drawable.ic_outline_home_24, BottomTab.Home) {
                amazonNavController.navigateTo(BottomTab.Home)
            },
            BottomNavItem(R.drawable.ic_outline_person_24, BottomTab.Profile) {
                amazonNavController.navigateTo(BottomTab.Profile)
            },
            BottomNavItem(R.drawable.ic_outline_shopping_cart_24, BottomTab.Cart) {
                amazonNavController.navigateTo(BottomTab.Cart)
            },
            BottomNavItem(R.drawable.ic_outline_menu_24, BottomTab.Shortcuts) {
                amazonNavController.navigateTo(BottomTab.Shortcuts)
            },
        )
    }

    val backStackState: BackStackState by amazonNavController.currentBackStack.collectAsStateWithLifecycle()
    val currentRoute = backStackState.backStack.lastOrNull()
    val isStartRoute = currentRoute?.isStartRoute() == true
    val isInSearchSuggestionsMode = currentRoute?.isRoute<Search>() == true
    val isInSearchResultsMode = currentRoute?.isRoute<SearchResults>() == true
    val onUpNavigation: (() -> Unit)? =
        if (isStartRoute) null else ({ amazonNavController.navigateUp() })

    val onOpenSearch: (String) -> Unit =
        if (isInSearchSuggestionsMode) ({}) else ({ initialSearchText ->
            amazonNavController.navigateTo(Search(initialSearchText))
        })
    val onViewProduct = { productId: Int ->
        amazonNavController.navigateTo(ViewProduct(productId))
    }
    val onPerformSearch = { searchString: String ->
        // First, clear Search Screen from backstack
        if (isInSearchSuggestionsMode) {
            amazonNavController.popBackStack()
        }
        amazonNavController.navigateTo(SearchResults(searchString))
    }
    val currentSearchTextContext = when {
        isInSearchSuggestionsMode -> (currentRoute as? Search)?.initialSearchText ?: ""
        isInSearchResultsMode -> (currentRoute as? SearchResults)?.searchString ?: ""
        else -> ""
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(collapsibleState.scrollObserver),
        topBar = {
            val isHome = currentRoute?.isRoute<HomeStart>() == true
            val windowPadding = WindowInsets.systemBars.asPaddingValues()

            if (isHome && !isInSearchSuggestionsMode) {
                AmazonTopAppBarWithNavChips(
                    modifier = Modifier.fillMaxWidth(),
                    navChipsOffset = collapsibleState.currentOffsetPx.value,
                    offsetFraction = collapsibleState.offsetFraction.value,
                    onNavChipsSizeChange = { intSize ->
                        navChipsHeightPx = intSize.height.toFloat()
                    },
                    onOpenSearch = { onOpenSearch("") },
                    windowPadding = windowPadding,
                )
            } else {
                AmazonTopAppBar(
                    initialSearchText = currentSearchTextContext,
                    isSearchEditable = isInSearchSuggestionsMode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AMAZON_BEIGE),
                    onOpenSearch = { onOpenSearch(currentSearchTextContext) },
                    onPerformSearch = onPerformSearch,
                    onUpNavigation = onUpNavigation,
                    windowPadding = windowPadding,
                )
            }
        },
        bottomBar = {
            if (!isInSearchSuggestionsMode) {
                AmazonBottomAppBar(
                    selectedTab = backStackState.currentGroup,
                    modifier = Modifier
                        .height(80.dp)
                        .topBorder(AmazonOutlineLight, 1.dp),
                    navItems = bottomNavItems
                )
            }
        },
    ) { innerPadding ->
        AmazonNavGraph(
            backHandlerForTabs = amazonNavController.backHandlerForTabs.collectAsStateWithLifecycle().value,
            innerPadding = innerPadding,
            navController = amazonNavController.navController,
            modifier = Modifier.fillMaxSize(),
            onViewProduct = onViewProduct,
            onPerformSearch = onPerformSearch,
        )
    }
}

private fun Modifier.topBorder(color: Color, borderWidth: Dp): Modifier {
    return drawWithContent {
        drawContent()
        val start = Offset(0f, 0f)
        val end = Offset(size.width, 0f)
        drawLine(color, start, end, borderWidth.toPx())
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    MockAmazonTheme {
        HomeScreenRoot(modifier = Modifier.fillMaxSize())
    }
}
