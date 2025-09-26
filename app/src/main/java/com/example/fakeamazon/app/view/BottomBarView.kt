package com.example.fakeamazon.app.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun AmazonBottomAppBar(
    modifier: Modifier = Modifier,
    navItems: List<BottomNavItem>,
) {
    NavigationBar(modifier) {
        navItems.forEach { navItem ->
            NavigationBarItem(
                selected = (navItem.icon === Icons.Outlined.Home),
                onClick = {},
                icon = { Icon(contentDescription = null, imageVector = navItem.icon) },
            )
        }
    }
}

data class BottomNavItem(
    val icon: ImageVector
)
