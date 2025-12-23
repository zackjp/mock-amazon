package com.zackjp.mockamazon.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Composable
fun rememberNav3Controller(
    tabs: Set<Nav.Route>,
    initialTab: Nav.Route,
): TabbedNavController {
    val backStacks: Map<NavKey, NavBackStack<NavKey>> = tabs.associateWith { key ->
        rememberNavBackStack()
    }

    val groupHistory = rememberSerializable(
        tabs, initialTab,
        serializer = SnapshotStateListSerializer(NavKeySerializer())
    ) {
        mutableStateListOf()
    }

    val navState = remember(tabs, initialTab) {
        NavState(
            defaultGroup = initialTab,
            groupHistory = groupHistory,
            groupStacks = backStacks,
        )
    }

    val tabbedNavController = remember(tabs, initialTab) {
        AmazonNav3Controller(navState)
    }

    return tabbedNavController
}

/**
 * A navigator that handles separate backstacks for each tab/group.
 *
 * Nav3 team's recommended approach is for the navigator to simply modify
 * an external mutable list, eg NavBackStack, and let serialization be
 * handled external to the Navigator.
 *
 * This controller remains independent of Compose-specific implementation
 * details so that it can be unit tested.
 */
class AmazonNav3Controller(
    private val navState: NavState,
) : TabbedNavController {

    private val defaultGroup = navState.defaultGroup
    private val groupOrder = navState.groupHistory
    private val groupStacks = navState.groupStacks
    private val currentGroup
        get() = navState.groupHistory.last()

    private val _currentBackStack: MutableStateFlow<BackStackState>
    override val currentBackStack: StateFlow<BackStackState>

    init {
        val nonNavRoutes = navState.groupHistory.filter { it !is Nav.Route }
        require(nonNavRoutes.isEmpty()) { "Group history should only top-level start routes and not Nav.Tab: $nonNavRoutes" }
        require(defaultGroup !is Nav.Tab) { "Default group should be a top-level start route and not Nav.Tab: $defaultGroup" }

        // Set the initial group and start route if either are missing
        navState.groupHistory.run { if (isEmpty()) add(defaultGroup) }
        groupStacks[currentGroup]?.run { if (isEmpty()) add(currentGroup) }
        validateState()

        _currentBackStack = MutableStateFlow(mergeBackStacks())
        currentBackStack = _currentBackStack.asStateFlow()
    }

    override fun navigateTo(target: Nav) {
        val targetRoute: Nav.Route = when (target) {
            is Nav.Tab -> {
                val startRoute = target.startRouteFactory()
                check(startRoute in groupStacks.keys) { "Group's start route is not a valid group key: $target / $startRoute" }
                startRoute
            }

            is Nav.Route -> target
        }

        val groupOwner = targetRoute.groupOwner()
        val targetGroup = groupOwner?.startRouteFactory() ?: currentGroup
        val isTargetTopLevel = targetRoute in groupStacks.keys
        if (isTargetTopLevel && targetGroup != targetRoute) {
            error("Top level route does not match its group owner's start route ($targetRoute != $groupOwner/$targetGroup)")
        }

        val isGroupChange = targetGroup != currentGroup
        moveGroupToMostRecent(targetGroup)

        groupStacks[targetGroup]?.run {
            if (isGroupChange) {
                // If the new stack is empty or we didn't request
                // a group switch, then add the target route
                if (isEmpty() || !isTargetTopLevel) {
                    add(targetRoute)
                }
            } else {
                // If requesting the current group, reset it first
                if (isTargetTopLevel) clear()
                add(targetRoute)
            }
        }

        _currentBackStack.value = mergeBackStacks()
    }

    override fun navigateUp() {
        popBackStack()
    }

    override fun popBackStack() {
        val prePopGroup = currentGroup
        val prePopStack = groupStacks.getOrDefault(prePopGroup, emptyList())

        // The default "home" group should always be the last remaining route.
        // If it is, then return early and do nothing.
        if (groupOrder.size == 1 && listOf(defaultGroup) == prePopStack) {
            return
        }

        // Then pop the most recent group's stack and if necessary pop the group as well
        groupStacks[prePopGroup]?.run {
            // Pop route from current stack
            if (isNotEmpty()) removeAt(size - 1)

            // If stack is now empty, pop current group
            if (isEmpty()) popMostRecentGroup()
        }

        // After popping, if we only have one final item, and it is not the default start route,
        // then prepend the default group & start route so that it is
        if (groupOrder.size == 1) {
            val postPopGroup = currentGroup
            val postPopStack = groupStacks[postPopGroup]

            if (postPopStack?.size == 1 && listOf(defaultGroup) != postPopStack) {
                groupOrder.add(0, defaultGroup)
                groupStacks[defaultGroup]?.run {
                    add(defaultGroup)
                }
            }
        }

        _currentBackStack.value = mergeBackStacks()
    }

    private fun mergeBackStacks(): BackStackState {
        validateState()
        val updatedBackStack = groupOrder.flatMap { groupStacks[it] ?: emptyList() }

        return BackStackState(
            backStack = updatedBackStack,
            currentGroup = currentGroup,
        )
    }

    private fun moveGroupToMostRecent(group: NavKey) {
        groupOrder.remove(group)
        groupOrder.add(group)
    }

    private fun popMostRecentGroup() {
        if (groupOrder.size > 1) {
            groupOrder.removeAt(groupOrder.size - 1)
        }
    }

    private fun validateState() {
        check(groupStacks.containsKey(defaultGroup)) {
            "Default group is not a valid key in group stacks: $defaultGroup not found in ${groupStacks.keys}"
        }
        val validGroups = groupStacks.keys
        val invalidGroups = groupOrder.filter { it !in validGroups }
        check(invalidGroups.isEmpty()) {
            "Group history contains group keys not found in group stacks: $invalidGroups not found in ${groupStacks.keys}"
        }
    }

}

@Serializable
data class NavState(
    val defaultGroup: NavKey,
    val groupHistory: MutableList<NavKey>,
    val groupStacks: Map<NavKey, MutableList<NavKey>>,
)
