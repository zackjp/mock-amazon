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
import androidx.navigation.compose.rememberNavController
import com.example.fakeamazon.app.view.AmazonBottomAppBar
import com.example.fakeamazon.app.view.AmazonTopAppBar
import com.example.fakeamazon.app.view.AmazonTopAppBarWithNavChips
import com.example.fakeamazon.app.view.BottomNavItem
import com.example.fakeamazon.base.HomeStart
import com.example.fakeamazon.base.TopRoute
import com.example.fakeamazon.base.navigateToTopRoute
import com.example.fakeamazon.base.topDestination
import com.example.fakeamazon.features.home.HomeScreenRoot
import com.example.fakeamazon.ui.theme.AmazonOutlineLight
import com.example.fakeamazon.ui.theme.FakeAmazonTheme

val AMAZON_BEIGE = Color(0xeFF5BE89)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var navChipsHeightPx by remember { mutableFloatStateOf(0f) }
    val collapsibleState = rememberCollapsibleState(maxCollapseHeightPx = -navChipsHeightPx)
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val bottomNavItems = remember(navController) {
        listOf(
            BottomNavItem(Icons.Outlined.Home, TopRoute.HomeGraph) {
                navController.navigateToTopRoute(TopRoute.HomeGraph)
            },
            BottomNavItem(Icons.Outlined.Person, TopRoute.ProfileGraph) {
                navController.navigateToTopRoute(TopRoute.ProfileGraph)
            },
            BottomNavItem(Icons.Outlined.ShoppingCart, TopRoute.CartGraph) {
                navController.navigateToTopRoute(TopRoute.CartGraph)
            },
            BottomNavItem(Icons.Outlined.Menu, TopRoute.ShortcutsGraph) {
                navController.navigateToTopRoute(TopRoute.ShortcutsGraph)
            },
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(collapsibleState.scrollObserver),
        topBar = {
            val isHome = currentDestination?.hasRoute<HomeStart>() ?: false

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
                topRouteChecker = { topRoute: TopRoute ->
                    currentDestination?.topDestination()?.hasRoute(topRoute::class) ?: false
                },
                modifier = Modifier
                    .height(80.dp)
                    .topBorder(AmazonOutlineLight, 1.dp),
                navItems = bottomNavItems
            )
        },
    ) { innerPadding ->
        AmazonNavGraph(
            innerPadding = innerPadding,
            modifier = Modifier.fillMaxSize(),
            navController = navController
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
