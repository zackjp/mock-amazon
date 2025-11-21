package com.example.fakeamazon.app.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import kotlinx.serialization.Serializable

val TOP_ROUTES_SET = setOfNotNull(
    TopRoute.HomeGraph,
    TopRoute.ProfileGraph,
    TopRoute.CartGraph,
    TopRoute.ShortcutsGraph,
)

@Serializable
object RootGraph

@Serializable
sealed interface TopRoute {
    @Serializable
    object HomeGraph: TopRoute
    @Serializable
    object ProfileGraph: TopRoute
    @Serializable
    object CartGraph: TopRoute
    @Serializable
    object ShortcutsGraph: TopRoute
}

@Serializable
object HomeStart
@Serializable
object ProfileStart
@Serializable
object CartStart
@Serializable
object ShortcutsStart

@Serializable
data class Search(val initialSearchText: String)
@Serializable
data class SearchResults(val searchString: String)
@Serializable
data class ViewProduct(val productId: Int)

fun List<NavBackStackEntry>.nearestTopRoute(): TopRoute? {
    for (entry in reversed()) {
        val candidate = TOP_ROUTES_SET.firstOrNull { topRoute ->
            entry.destination.topDestination()?.hasRoute(topRoute::class) == true
        }
        if (candidate != null) {
            return candidate
        }
    }

    return null
}

fun NavDestination.topDestination(): NavDestination? {
    return hierarchy.firstOrNull { destination -> TOP_ROUTES_SET.any { destination.hasRoute(it::class) } }
}

fun NavDestination.findTopRoute(): TopRoute? {
    val topDestination = topDestination()
    return TOP_ROUTES_SET.firstOrNull { topDestination?.hasRoute(it::class) == true }
}
