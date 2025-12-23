package com.zackjp.mockamazon.app.ui.view

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.NavKey
import com.zackjp.mockamazon.app.navigation.Nav


@Composable
fun AmazonBottomAppBar(
    selectedTab: NavKey,
    modifier: Modifier = Modifier,
    navItems: List<BottomNavItem>,
) {
    NavigationBar(modifier) {
        navItems.forEach { navItem ->
            NavigationBarItem(
                selected = when (selectedTab) {
                    is Nav.Tab -> selectedTab == navItem.bottomTab
                    is Nav.Route -> selectedTab.groupOwner() == navItem.bottomTab
                    else -> error("Unknown tab type: $selectedTab")
                },
                onClick = navItem.onClick,
                icon = {
                    Icon(
                        contentDescription = null,
                        painter = painterResource(navItem.iconId)
                    )
                },
            )
        }
    }
}

data class BottomNavItem(
    @param:DrawableRes val iconId: Int,
    val bottomTab: NavKey,
    val onClick: () -> Unit
)
