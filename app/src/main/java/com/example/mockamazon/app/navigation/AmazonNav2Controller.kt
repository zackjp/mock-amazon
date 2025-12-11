package com.example.mockamazon.app.navigation

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import androidx.savedstate.read
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

private const val KEY_TAB_HISTORY = "amazon-nav:controller:tabHistory"

@Composable
fun rememberNav2Controller(): AmazonNav2Controller {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()

    val amazonNav2Controller = rememberSaveable(
        navController,
        coroutineScope,
        saver = tabbedRouteControllerSaver(coroutineScope, navController),
    ) {
        AmazonNav2Controller(
            coroutineScope = coroutineScope,
            navController = navController,
            validRoutes = VALID_ROUTES_SET,
        )
    }

    return amazonNav2Controller
}

/**
 * A wrapper for NavController that accomplishes two main things.
 * 1. Ensure that each tab's backstack only contains destinations that belong
 * to that tab's navgraph hierarchy or do not belong to any tab navgraph at all. If
 * the destination belongs hierarchically to another tab, it will navigate
 * to and restore that tab's backstack first.
 * 1. Maintain a history of tab selection so that when using System Back while on
 * one of the start destinations, it will navigate back in the same order the user
 * visited each tab and restore that tab's backstack.
 */
class AmazonNav2Controller(
    coroutineScope: CoroutineScope,
    val navController: NavHostController,
    private val validRoutes: Set<KClass<out Nav.Route>>,
) : TabbedNavController {

    private val tabHistory = MutableStateFlow(emptyList<BottomTab>())

    private val _backHandlerForTabs = MutableStateFlow<@Composable () -> Unit>({})
    val backHandlerForTabs = _backHandlerForTabs.asStateFlow()

    private val _currentBackStack = MutableStateFlow(
        BackStackState(
            backStack = emptyList(),
            currentGroup = BottomTab.Home,
        )
    )
    override val currentBackStack: StateFlow<BackStackState> = _currentBackStack.asStateFlow()

    init {
        coroutineScope.launch {
            // Need to check the full backstack (not just visible) to determine
            // what tab we're on because in case of process death, when NavController
            // restores its backstack, the root graph might not be "visible"
            @SuppressLint("RestrictedApi") // NavController.currentBackStack
            navController.currentBackStack.collect { backstack ->
                processTabChanges(backstack)
                _currentBackStack.update {
                    BackStackState(
                        // Map NavDestination routes into Nav.Routes. Ignore NavGraphDestinations,
                        // which aren't part of the displayed backstack.
                        backStack = backstack
                            .filter { entry -> validRoutes.any { entry.destination.hasRoute(it) } }
                            .map { entry ->
                                validRoutes.forEach { routeType ->
                                    if (entry.destination.hasRoute(routeType)) {
                                        return@map entry.toRoute<Nav.Route>(routeType)
                                    }
                                }
                                error("Unrecognized route: ${entry.destination.route}")
                            },
                        currentGroup = tabHistory.value.last(),
                    )
                }
            }
        }

        coroutineScope.launch {
            tabHistory.collect { history ->
                _currentBackStack.update { current ->
                    current.copy(currentGroup = history.last<BottomTab>())
                }
                _backHandlerForTabs.value = calculateBackHandler(history)
            }
        }
    }

    @SuppressLint("RestrictedApi") // NavController.currentBackStack, NavGraph.matchRouteComprehensive()
    override fun navigateTo(target: Nav) {
        navController.apply {
            val currentTopRoute = currentBackStack.value.nearestTopRoute()
            val targetDestination = graph.matchRouteComprehensive(
                route = target::class.qualifiedName!!,
                searchChildren = true,
                searchParent = false,
                graph
            )?.destination
            val targetTopRoute = targetDestination?.findTopRoute()

            if (targetTopRoute == null) {
                // If the target's tab route is null (ie, the target route does not belong to a
                // particular bottom tab's navgraph), then just open it in the current tab.
                navigate(target as Object)
                return
            }

            val isDestinationCurrentTab = currentTopRoute == targetTopRoute
            if (isDestinationCurrentTab) { // User is reloading current tab
                // Clear the backstack, inclusive, for the current tab
                // before navigating to and reloading it
                popBackStack(
                    destinationId = targetDestination.id,
                    inclusive = true,
                    saveState = false,
                )
                navigate(target)
            } else {
                // If we are navigating to a new tab, save current tab and restore the new one
                popBackStack(
                    destinationId = graph.findStartDestination().id,
                    inclusive = false,
                    saveState = true,
                )

                navigate(targetTopRoute) {
                    launchSingleTop = true
                    restoreState = true
                }
            }

            val isDestinationATopRoute = target == targetTopRoute
            // In any case, as long as the destination is not itself a
            // tab route, we launch a new instance of it on top
            if (!isDestinationATopRoute) {
                navigate(target)
            }
        }
    }

    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun popBackStack() {
        navController.popBackStack()
    }

    fun saveState(): SavedState {
        return SavedState().apply {
            putString(KEY_TAB_HISTORY, Json.encodeToString(tabHistory.value))
        }
    }

    fun restoreState(savedState: SavedState) {
        val restoredHistory = savedState.read {
            Json.decodeFromString<List<BottomTab>>(getString(KEY_TAB_HISTORY))
        }
        tabHistory.value = restoredHistory
    }

    private fun processTabChanges(backstack: List<NavBackStackEntry>) {
        val nearestTopRoute: BottomTab? = backstack.nearestTopRoute()

        nearestTopRoute?.let {
            val isTabChanged = tabHistory.value.lastOrNull() != nearestTopRoute
            if (isTabChanged) {
                val newHistory = tabHistory.value.toMutableList()
                // Move the tab to end of list (most recent)
                newHistory.remove(it)
                newHistory.add(it)
                tabHistory.value = newHistory
            }
        }
    }

    private fun calculateBackHandler(currentTabHistory: List<BottomTab>): @Composable () -> Unit =
        @Composable {
            BackHandler(enabled = currentTabHistory.size > 1) {
                var newHistory = currentTabHistory.dropLast(1)
                val removedTopRoute = currentTabHistory.last()
                // Home should always be the last remaining item. So if
                // navigating away from Home, prepend it instead
                if (removedTopRoute == BottomTab.Home) {
                    newHistory = listOf(BottomTab.Home) + newHistory
                }
                tabHistory.value = newHistory

                if (newHistory.isNotEmpty()) {
                    val prevTopRoute = newHistory.last()
                    navigateTo(prevTopRoute)
                }
            }
        }

}

private fun tabbedRouteControllerSaver(
    coroutineScope: CoroutineScope,
    navController: NavHostController
): Saver<AmazonNav2Controller, *> =
    Saver(
        save = { it.saveState() },
        restore = {
            AmazonNav2Controller(
                coroutineScope,
                navController,
                VALID_ROUTES_SET,
            ).apply { restoreState(it) }
        },
    )
