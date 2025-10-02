package com.example.fakeamazon.app.view

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.fakeamazon.base.TopRoute


@Composable
fun AmazonBottomAppBar(
    topRouteChecker: (TopRoute) ->  Boolean,
    modifier: Modifier = Modifier,
    navItems: List<BottomNavItem>,
) {
    NavigationBar(modifier) {
        navItems.forEach { navItem ->
            NavigationBarItem(
                selected = topRouteChecker(navItem.topRoute),
                onClick = navItem.onClick,
                icon = { Icon(contentDescription = null, imageVector = navItem.icon) },
            )
        }
    }
}

data class BottomNavItem(
    val icon: ImageVector,
    val topRoute: TopRoute,
    val onClick: () -> Unit
)
