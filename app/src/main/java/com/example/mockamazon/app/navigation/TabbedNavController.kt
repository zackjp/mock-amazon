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

sealed interface Nav: NavKey {
    @Serializable
    abstract class Tab : Nav {
        abstract val startRouteFactory: () -> Route
    }

    @Serializable
    abstract class Route : Nav {
        /**
         * Returns the tab owner. Providing via function avoids serializing this data.
         */
        open fun groupOwner(): Tab? = null
    }
}

data class BackStackState(
    val backStack: List<NavKey>,
    val currentGroup: NavKey,
)
