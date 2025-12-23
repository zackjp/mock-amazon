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
import com.example.mockamazon.app.navigation.AmazonNav2Controller
import com.example.mockamazon.app.navigation.AmazonNav3Controller
import com.example.mockamazon.app.navigation.AmazonNav3Display
import com.example.mockamazon.app.navigation.AmazonNavGraph
import com.example.mockamazon.app.navigation.BackStackState
import com.example.mockamazon.app.navigation.BottomTab
import com.example.mockamazon.app.navigation.HomeStart
import com.example.mockamazon.app.navigation.Search
import com.example.mockamazon.app.navigation.SearchResults
import com.example.mockamazon.app.navigation.TOP_ROUTES_SET
import com.example.mockamazon.app.navigation.TabbedNavController
import com.example.mockamazon.app.navigation.ViewProduct
import com.example.mockamazon.app.navigation.rememberNav2Controller
import com.example.mockamazon.app.navigation.rememberNav3Controller
import com.example.mockamazon.app.ui.view.AmazonBottomAppBar
import com.example.mockamazon.app.ui.view.AmazonTopAppBar
import com.example.mockamazon.app.ui.view.AmazonTopAppBarWithNavChips
import com.example.mockamazon.app.ui.view.BottomNavItem
import com.example.mockamazon.features.home.HomeScreenRoot
import com.example.mockamazon.shared.model.FeatureFlags
import com.example.mockamazon.shared.theme.AmazonBeige
import com.example.mockamazon.shared.theme.AmazonOutlineLight
import com.example.mockamazon.ui.theme.MockAmazonTheme
import com.example.mockamazon.shared.R as SharedR


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val amazonNavController = when (FeatureFlags.USE_NAV3) {
        true -> rememberNav3Controller(TOP_ROUTES_SET, HomeStart)
        false -> rememberNav2Controller()
    }

    val bottomNavItems = remember(amazonNavController) {
        createBottomNavItems(amazonNavController)
    }

    val backStackState: BackStackState by amazonNavController.currentBackStack.collectAsStateWithLifecycle()
    val currentRoute = backStackState.backStack.lastOrNull()
    val isStartRoute = currentRoute != null && TOP_ROUTES_SET.contains(currentRoute)
    val isHome = currentRoute is HomeStart
    val searchMode = when (currentRoute) {
        is Search -> SearchMode.Suggestions(currentRoute.initialSearchText)
        is SearchResults -> SearchMode.Results(currentRoute.searchString)
        else -> SearchMode.None("")
    }

    var navChipsHeightPx by remember { mutableFloatStateOf(0f) }
    val collapsibleState = rememberCollapsibleState(maxCollapseHeightPx = -navChipsHeightPx)

    val onNavigateUp: (() -> Unit)? = { if (!isStartRoute) amazonNavController.navigateUp() }
    val onOpenSearch: (String) -> Unit = { searchText: String ->
        if (searchMode !is SearchMode.Suggestions) {
            amazonNavController.navigateTo(Search(searchText))
        }
    }
    val onViewProduct = { productId: Int ->
        amazonNavController.navigateTo(ViewProduct(productId))
    }
    val onPerformSearch = { searchString: String ->
        // First, clear Search Screen from backstack
        if (currentRoute is Search) {
            amazonNavController.popBackStack()
        }
        amazonNavController.navigateTo(SearchResults(searchString))
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .let {
                // Keep track of the scroll position just for Home
                if (isHome)
                    it.nestedScroll(collapsibleState.scrollObserver)
                else
                    it
            },
        topBar = {
            val windowPadding = WindowInsets.systemBars.asPaddingValues()

            if (isHome) {
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
                    initialSearchText = searchMode.searchText,
                    isSearchEditable = searchMode is SearchMode.Suggestions,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AmazonBeige),
                    onOpenSearch = { onOpenSearch(searchMode.searchText) },
                    onPerformSearch = onPerformSearch,
                    onNavigateUp = if (isStartRoute) null else onNavigateUp,
                    windowPadding = windowPadding,
                )
            }
        },
        bottomBar = {
            if (searchMode !is SearchMode.Suggestions) {
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
        if (amazonNavController is AmazonNav3Controller) {
            AmazonNav3Display(
                backStackState = amazonNavController.currentBackStack.collectAsStateWithLifecycle().value,
                innerPadding = innerPadding,
                modifier = Modifier.fillMaxSize(),
                onBack = { amazonNavController.popBackStack() },
                onViewProduct = onViewProduct,
                onPerformSearch = onPerformSearch,
            )
        } else {
            val amazonNav2Controller = (amazonNavController as AmazonNav2Controller)
            AmazonNavGraph(
                backHandlerForTabs = amazonNav2Controller.backHandlerForTabs.collectAsStateWithLifecycle().value,
                innerPadding = innerPadding,
                navController = amazonNav2Controller.navController,
                modifier = Modifier.fillMaxSize(),
                onViewProduct = onViewProduct,
                onPerformSearch = onPerformSearch,
            )
        }
    }
}

private fun createBottomNavItems(amazonNavController: TabbedNavController): List<BottomNavItem> =
    listOf(
        BottomNavItem(SharedR.drawable.ic_outline_home_24, BottomTab.Home) {
            amazonNavController.navigateTo(BottomTab.Home)
        },
        BottomNavItem(SharedR.drawable.ic_outline_person_24, BottomTab.Profile) {
            amazonNavController.navigateTo(BottomTab.Profile)
        },
        BottomNavItem(SharedR.drawable.ic_outline_shopping_cart_24, BottomTab.Cart) {
            amazonNavController.navigateTo(BottomTab.Cart)
        },
        BottomNavItem(SharedR.drawable.ic_outline_menu_24, BottomTab.Shortcuts) {
            amazonNavController.navigateTo(BottomTab.Shortcuts)
        },
    )

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

private sealed class SearchMode {
    abstract val searchText: String

    data class None(override val searchText: String) : SearchMode()
    data class Suggestions(override val searchText: String) : SearchMode()
    data class Results(override val searchText: String) : SearchMode()
}
