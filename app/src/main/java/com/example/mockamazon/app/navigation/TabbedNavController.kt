package com.example.mockamazon.app.navigation

import kotlinx.coroutines.flow.StateFlow

interface TabbedNavController {

    val currentBackStack: StateFlow<BackStackState>

    fun navigateTo(target: Nav)

    fun navigateUp()

    fun popBackStack()

}

sealed interface Nav {
    abstract class Tab : Nav {
        abstract val startRouteFactory: () -> Route
    }
    abstract class Route : Nav {
        /**
         * Returns the tab owner. Providing via function avoids serializing this data.
         */
        abstract fun tabOwner(): Tab?

        inline fun <reified R : Route> isRoute(): Boolean = R::class == this::class
        fun isStartRoute(): Boolean {
            val tabOwner = tabOwner()
            if (tabOwner == null) return false
            return tabOwner.startRouteFactory()::class == this::class
        }
    }
}

data class BackStackState(
    val backStack: List<Nav.Route>,
    val currentGroup: Nav.Tab,
)
