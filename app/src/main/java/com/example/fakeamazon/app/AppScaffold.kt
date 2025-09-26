package com.example.fakeamazon.app

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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fakeamazon.app.view.AmazonBottomAppBar
import com.example.fakeamazon.app.view.AmazonTopAppBar
import com.example.fakeamazon.app.view.BottomNavItem
import com.example.fakeamazon.features.home.HomeScreenRoot
import com.example.fakeamazon.ui.theme.FakeAmazonTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val bottomNavItems = listOf(
        BottomNavItem(Icons.Outlined.Home),
        BottomNavItem(Icons.Outlined.Person),
        BottomNavItem(Icons.Outlined.ShoppingCart),
        BottomNavItem(Icons.Outlined.Menu),
    )

    var navChipsHeightPx by remember { mutableFloatStateOf(0f) }
    val collapsibleState = rememberCollapsibleState(maxCollapseHeightPx = -navChipsHeightPx)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(collapsibleState.scrollObserver),
        topBar = {
            AmazonTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                navChipsOffset = collapsibleState.currentOffsetPx.value,
                offsetFraction = collapsibleState.offsetFraction.value,
                onNavChipsSizeChange = { intSize ->
                    navChipsHeightPx = intSize.height.toFloat()
                },
            )
        },
        bottomBar = {
            AmazonBottomAppBar(
                modifier = Modifier.height(80.dp),
                navItems = bottomNavItems,
            )
        },
    ) { innerPadding ->
        HomeScreenRoot(
            innerPadding = innerPadding,
            modifier = Modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    FakeAmazonTheme {
        HomeScreenRoot(modifier = Modifier.fillMaxSize())
    }
}
