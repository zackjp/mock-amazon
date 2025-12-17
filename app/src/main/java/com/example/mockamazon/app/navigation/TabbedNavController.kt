package com.example.mockamazon.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface TabbedNavController {

    val currentBackStack: StateFlow<BackStackState>

    fun navigateTo(target: Nav)

    fun navigateUp()

    fun popBackStack()

}

sealed interface Nav {
    @Serializable
    abstract class Tab : Nav {
        abstract val startRouteFactory: () -> Route
    }

    @Serializable
    abstract class Route : Nav {
        /**
         * Returns the tab owner. Providing via function avoids serializing this data.
         */
        abstract fun tabOwner(): Tab?

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
