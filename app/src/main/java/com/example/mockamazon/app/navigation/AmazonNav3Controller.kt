package com.example.mockamazon.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@Composable
fun rememberNav3Controller(
    tabs: Set<BottomTab>,
    initialTab: BottomTab,
): TabbedNavController {
    val tabbedNavController = remember {
        AmazonNav3Controller(
            defaultGroup = initialTab,
            tabs = tabs,
        )
    }

    return tabbedNavController
}

/**
 * A navigation approach that maintains a backstack for each tab. When navigating to another
 * tab, it will move that tab's backstack to the front. Consequently, popping will always occur
 * from the top of the current tab's stack until it is empty.
 */
class AmazonNav3Controller(
    val defaultGroup: Nav.Tab,
    private val tabs: Set<Nav.Tab>,
) : TabbedNavController {

    private val _currentBackStack = MutableStateFlow(
        BackStackState(
            backStack = emptyList(),
            currentGroup = defaultGroup,
        )
    )
    override val currentBackStack = _currentBackStack.asStateFlow()

    private val groupStacks = tabs.associateWithTo(mutableMapOf()) { emptyList<Nav.Route>() }
    private val groupOrder = ArrayDeque<Nav.Tab>()
    private val currentGroup: Nav.Tab
        get() = groupOrder.lastOrNull()!!

    init {
        validateGroup(defaultGroup) // todo: not needed? validation in navigateTo()
        navigateTo(defaultGroup)
    }

    override fun navigateTo(target: Nav) {
        val targetGroup: Nav.Tab?
        val targetRouteCandidate: Nav.Route

        when (target) {
            is Nav.Tab -> {
                targetGroup = target
                targetRouteCandidate = targetGroup.startRouteFactory()
            }

            is Nav.Route -> {
                targetGroup = target.tabOwner()
                targetRouteCandidate = target
            }
        }

        // Move target tab to the front
        val priorGroup = groupOrder.lastOrNull() ?: defaultGroup
        val isGroupChange = targetGroup != priorGroup // TODO: targetGroup != null &&...
        val currentGroup = targetGroup ?: priorGroup
        validateGroup(currentGroup)
        moveGroupToMostRecent(currentGroup)

        // Add routes to appropriate stacks
        if (isGroupChange) {
            groupStacks.compute(currentGroup) { group, value ->
                val currentStack = value ?: listOf()
                if (target is Nav.Tab && currentStack.isNotEmpty()) {
                    currentStack
                } else {
                    currentStack + targetRouteCandidate
                }
            }
        } else {
            groupStacks.compute(currentGroup) { group, value ->
                val isTargetingGroupStart =
                    targetRouteCandidate::class == targetGroup.startRouteFactory()::class
                val resetGroupStack = isTargetingGroupStart

                if (resetGroupStack) {
                    listOf(targetGroup.startRouteFactory())
                } else {
                    val currentStack = value ?: listOf()
                    currentStack + targetRouteCandidate
                }
            }
        }

        notifyBackStackChanges()
    }

    override fun navigateUp() {
        popBackStack()
    }

    override fun popBackStack() {
        val prePopGroup = currentGroup
        val prePopStack = groupStacks.getOrDefault(prePopGroup, emptyList())

        // The default "home" group should always be the last remaining route.
        // So if it is, then return early and do nothing.
        if (groupOrder.size == 1 && prePopStack == listOf(defaultGroup.startRouteFactory())) {
            return
        }

        // Then pop the most recent group's stack and if necessary pop the group as well
        groupStacks.compute(prePopGroup) { group, stack ->
            // Pop from current group stack
            val updatedStack = stack?.dropLast(1)

            // Pop group if current group stack is now empty
            if (updatedStack?.isEmpty() == true) {
                popMostRecentGroup()
            }

            updatedStack
        }

        // After popping, if we only have one final item, and it is not the default start route,
        // then prepend the default group & start route so that it is
        if (groupOrder.size == 1) {
            val postPopGroup = currentGroup
            val postPopStack = groupStacks.getOrDefault(postPopGroup, emptyList())

            if (postPopStack.size == 1 && postPopStack != listOf(defaultGroup.startRouteFactory())) {
                groupOrder.addFirst(defaultGroup)
                groupStacks.compute(defaultGroup) { group, stack ->
                    (stack ?: emptyList()) + defaultGroup.startRouteFactory()
                }
            }
        }

        notifyBackStackChanges()
    }

    private fun notifyBackStackChanges() {
        val updatedBackStack = groupOrder.fold(listOf<Nav.Route>()) { backStackAccumulator, group ->
            backStackAccumulator + groupStacks.getOrDefault(group, emptyList())
        }

        _currentBackStack.value = BackStackState(
            backStack = updatedBackStack,
            currentGroup = currentGroup,
        )
    }

    private fun moveGroupToMostRecent(group: Nav.Tab) {
        groupOrder.remove(group)
        groupOrder.addLast(group)
    }

    private fun popMostRecentGroup() {
        if (groupOrder.size > 1) {
            groupOrder.removeAt(groupOrder.size - 1)
        }
    }

    private fun validateGroup(tab: Nav.Tab) {
        require(tabs.contains(tab)) { "Invalid tab: ${tab::class.simpleName}" }
    }

}
