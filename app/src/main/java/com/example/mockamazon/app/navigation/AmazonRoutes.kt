package com.example.mockamazon.app.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass


val TAB_ROUTES_SET = setOfNotNull(
    BottomTab.Home,
    BottomTab.Profile,
    BottomTab.Cart,
    BottomTab.Shortcuts,
)

val VALID_ROUTES_SET = setOf<KClass<out Nav.Route>>(
    HomeStart::class,
    ProfileStart::class,
    CartStart::class,
    ShortcutsStart::class,
    ViewProduct::class,
    Search::class,
    SearchResults::class
)

@Serializable
sealed class BottomTab : Nav.Tab() {

    @Serializable
    data object Home : BottomTab() {
        override val startRouteFactory = { HomeStart }
    }

    @Serializable
    data object Profile : BottomTab() {
        override val startRouteFactory = { ProfileStart }
    }

    @Serializable
    data object Cart : BottomTab() {
        override val startRouteFactory = { CartStart }
    }

    @Serializable
    data object Shortcuts : BottomTab() {
        override val startRouteFactory = { ShortcutsStart }
    }

}

@Serializable
data object RootGraph

@Serializable
abstract class AmazonRoute : Nav.Route()

@Serializable
data object HomeStart : AmazonRoute() {
    override fun tabOwner() = BottomTab.Home
}

@Serializable
data object ProfileStart : AmazonRoute() {
    override fun tabOwner() = BottomTab.Profile
}

@Serializable
data object CartStart : AmazonRoute() {
    override fun tabOwner() = BottomTab.Cart
}

@Serializable
data object ShortcutsStart : AmazonRoute() {
    override fun tabOwner() = BottomTab.Home
}

@Serializable
data class Search(val initialSearchText: String) : AmazonRoute() {
    override fun tabOwner() = null
}

@Serializable
data class SearchResults(val searchString: String) : AmazonRoute() {
    override fun tabOwner() = null
}

@Serializable
data class ViewProduct(val productId: Int) : AmazonRoute() {
    override fun tabOwner() = null
}

fun List<NavBackStackEntry>.nearestTopRoute(): BottomTab? {
    for (entry in reversed()) {
        val candidate = TAB_ROUTES_SET.firstOrNull { topRoute ->
            entry.destination.topDestination()?.hasRoute(topRoute::class) == true
        }
        if (candidate != null) {
            return candidate
        }
    }

    return null
}

fun NavDestination.topDestination(): NavDestination? {
    return hierarchy.firstOrNull { destination -> TAB_ROUTES_SET.any { destination.hasRoute(it::class) } }
}

fun NavDestination.findTopRoute(): BottomTab? {
    val topDestination = topDestination()
    return TAB_ROUTES_SET.firstOrNull { topDestination?.hasRoute(it::class) == true }
}

fun <R : Any> NavDestination.hasRoute(routeCandidates: Collection<R>): R? {
    return routeCandidates.firstOrNull { hasRoute(it::class) }
}