package com.example.fakeamazon.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fakeamazon.app.navigation.rememberTabbedRouteController
import com.example.fakeamazon.app.view.AmazonBottomAppBar
import com.example.fakeamazon.app.view.AmazonTopAppBar
import com.example.fakeamazon.app.view.AmazonTopAppBarWithNavChips
import com.example.fakeamazon.app.view.BottomNavItem
import com.example.fakeamazon.base.HomeStart
import com.example.fakeamazon.base.TOP_ROUTES_SET
import com.example.fakeamazon.base.TopRoute
import com.example.fakeamazon.base.ViewProduct
import com.example.fakeamazon.features.home.HomeScreenRoot
import com.example.fakeamazon.ui.theme.AmazonOutlineLight
import com.example.fakeamazon.ui.theme.FakeAmazonTheme

val AMAZON_BEIGE = Color(0xeFF5BE89)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var navChipsHeightPx by remember { mutableFloatStateOf(0f) }
    val collapsibleState = rememberCollapsibleState(maxCollapseHeightPx = -navChipsHeightPx)

    val tabbedNavController = rememberTabbedRouteController(TOP_ROUTES_SET)
    val currentDestination by tabbedNavController.navController.currentBackStackEntryAsState()

    val bottomNavItems = remember(tabbedNavController) {
        listOf(
            BottomNavItem(Icons.Outlined.Home, TopRoute.HomeGraph) {
                tabbedNavController.navigateToRoute(TopRoute.HomeGraph)
            },
            BottomNavItem(Icons.Outlined.Person, TopRoute.ProfileGraph) {
                tabbedNavController.navigateToRoute(TopRoute.ProfileGraph)
            },
            BottomNavItem(Icons.Outlined.ShoppingCart, TopRoute.CartGraph) {
                tabbedNavController.navigateToRoute(TopRoute.CartGraph)
            },
            BottomNavItem(Icons.Outlined.Menu, TopRoute.ShortcutsGraph) {
                tabbedNavController.navigateToRoute(TopRoute.ShortcutsGraph)
            },
        )
    }

    val currentTab by tabbedNavController.currentTab.collectAsState()
    val onViewProduct = { productId: Int ->
        tabbedNavController.navigateToRoute(ViewProduct(productId))
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(collapsibleState.scrollObserver),
        topBar = {
            val isHome = currentDestination?.destination?.hasRoute<HomeStart>() ?: false

            if (isHome) {
                AmazonTopAppBarWithNavChips(
                    modifier = Modifier.fillMaxWidth(),
                    navChipsOffset = collapsibleState.currentOffsetPx.value,
                    offsetFraction = collapsibleState.offsetFraction.value,
                    onNavChipsSizeChange = { intSize ->
                        navChipsHeightPx = intSize.height.toFloat()
                    },
                )
            } else {
                AmazonTopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AMAZON_BEIGE)
                )
            }
        },
        bottomBar = {
            AmazonBottomAppBar(
                selectedTab = currentTab,
                modifier = Modifier
                    .height(80.dp)
                    .topBorder(AmazonOutlineLight, 1.dp),
                navItems = bottomNavItems
            )
        },
    ) { innerPadding ->
        AmazonNavGraph(
            backHandlerForTabs = tabbedNavController.backHandlerForTabs.collectAsState().value,
            innerPadding = innerPadding,
            navController = tabbedNavController.navController,
            modifier = Modifier.fillMaxSize(),
            onViewProduct = onViewProduct,
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
    FakeAmazonTheme {
        HomeScreenRoot(modifier = Modifier.fillMaxSize())
    }
}
