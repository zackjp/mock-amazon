package com.example.fakeamazon.base

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.serialization.Serializable

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

fun NavController.navigateToTopRoute(topRoute: TopRoute) {
    navigate(topRoute) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
