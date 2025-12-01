package com.example.mockamazon.app.ui.view

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.mockamazon.app.navigation.TopRoute


@Composable
fun AmazonBottomAppBar(
    selectedTab: TopRoute?,
    modifier: Modifier = Modifier,
    navItems: List<BottomNavItem>,
) {
    NavigationBar(modifier) {
        navItems.forEach { navItem ->
            NavigationBarItem(
                selected = navItem.topRoute == selectedTab,
                onClick = navItem.onClick,
                icon = { Icon(contentDescription = null, painter = painterResource(navItem.iconId)) },
            )
        }
    }
}

data class BottomNavItem(
    @param:DrawableRes val iconId: Int,
    val topRoute: TopRoute,
    val onClick: () -> Unit
)
