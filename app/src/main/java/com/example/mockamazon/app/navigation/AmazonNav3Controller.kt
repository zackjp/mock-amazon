package com.example.mockamazon.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.savedstate.read
import androidx.savedstate.savedState
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import java.util.Collections

@OptIn(InternalSerializationApi::class)
@Composable
fun rememberNav3Controller(
    tabs: Set<BottomTab>,
    initialTab: BottomTab,
): TabbedNavController {
    val tabbedNavController = rememberSaveable(
        tabs,
        initialTab,
        saver = Saver(
            save = {
                val navState = it.getNavState()
                savedState {
                    putString("nav3:navState", jsonSerializer.encodeToString(navState))
                }
            },
            restore = { savedState ->
                val restoredNavState = jsonSerializer.decodeFromString<NavState>(
                    savedState.read { getString("nav3:navState") }
                )
                AmazonNav3Controller(restoredNavState)
            },
        )
    ) {
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
    navState: NavState,
) : TabbedNavController {

    constructor(
        defaultGroup: Nav.Tab,
        tabs: Set<Nav.Tab>,
    ) : this(
        navState = NavState(
            defaultGroup = defaultGroup,
            groupHistory = listOf(defaultGroup),
            groupStacks = tabs.associateWith { emptyList() },
        )
    )

    private val defaultGroup: Nav.Tab

    private val groupStacks: Map<Nav.Tab, MutableList<Nav.Route>>
    private val groupOrder: ArrayDeque<Nav.Tab>
    private val currentGroup: Nav.Tab
        get() = groupOrder.lastOrNull()!!

    private val _currentBackStack: MutableStateFlow<BackStackState>
    override val currentBackStack: StateFlow<BackStackState>

    init {
        defaultGroup = navState.defaultGroup
        groupOrder = ArrayDeque(navState.groupHistory)
        groupStacks = navState.groupStacks.mapValues { (group, stack) ->
            stack.toMutableList().apply {
                if (group == groupOrder.last() && isEmpty()) {
                    add(group.startRouteFactory())
                }
            }
        }
        validateState()

        _currentBackStack = MutableStateFlow(createSingleBackStack())
        currentBackStack = _currentBackStack.asStateFlow()
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
        check(currentGroup in groupStacks.keys) { "Cannot navigate to invalid group: $currentGroup" }
        moveGroupToMostRecent(currentGroup)

        // Add routes to appropriate stacks
        if (isGroupChange) {
            groupStacks[currentGroup]?.run {
                // Always add the route if navigation wasn't targeting
                // a tab, or if the new tab has no routes yet
                if (target !is Nav.Tab || isEmpty()) {
                    add(targetRouteCandidate)
                }
            }
        } else {
            groupStacks[currentGroup]?.run {
                val isTargetingGroupStart =
                    targetRouteCandidate::class == targetGroup.startRouteFactory()::class
                val resetGroupStack = isTargetingGroupStart

                if (resetGroupStack) {
                    clear()
                    add(targetGroup.startRouteFactory())
                } else {
                    add(targetRouteCandidate)
                }
            }
        }

        _currentBackStack.value = createSingleBackStack()
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
        groupStacks[prePopGroup]?.run {
            // Pop route from current group stack
            if (isNotEmpty()) removeAt(size - 1)

            // Pop group if current group stack is now empty
            if (isEmpty()) popMostRecentGroup()
        }

        // After popping, if we only have one final item, and it is not the default start route,
        // then prepend the default group & start route so that it is
        if (groupOrder.size == 1) {
            val postPopGroup = currentGroup
            val postPopStack = groupStacks[postPopGroup]

            if (postPopStack?.size == 1 && postPopStack != listOf(defaultGroup.startRouteFactory())) {
                groupOrder.addFirst(defaultGroup)
                groupStacks[defaultGroup]?.run {
                    add(defaultGroup.startRouteFactory())
                }
            }
        }

        _currentBackStack.value = createSingleBackStack()
    }

    private fun createSingleBackStack(): BackStackState {
        validateState()
        val updatedBackStack = groupOrder.fold(listOf<Nav.Route>()) { backStackAccumulator, group ->
            backStackAccumulator + groupStacks.getOrDefault(group, emptyList())
        }

        return BackStackState(
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

    private fun validateState() {
        check(groupStacks.containsKey(defaultGroup)) {
            "Default group is not a valid key in group stacks: $defaultGroup"
        }
        val validGroups = groupStacks.keys
        val invalidGroups = groupOrder.filter { it !in validGroups }
        check(invalidGroups.isEmpty()) {
            "Group history contains group keys not found in group stacks: $invalidGroups"
        }
    }

    fun getNavState() = NavState(
        defaultGroup = defaultGroup,
        groupHistory = groupOrder.toList(),
        groupStacks = groupStacks.mapValues { (_, stack) ->
            Collections.unmodifiableList(stack) // TODO: consider toImmutableList from Jetbrains immutable library
        },
    )

}

@Serializable
data class NavState(
    val defaultGroup: Nav.Tab,
    val groupHistory: List<Nav.Tab>,
    val groupStacks: Map<Nav.Tab, List<Nav.Route>>,
)

@OptIn(InternalSerializationApi::class)
private val savedNavStateConfiguration = SavedStateConfiguration {
    val jsonModule = SerializersModule {
        polymorphic(Nav.Tab::class) {
            subclass(BottomTab.Home::class, BottomTab.Home::class.serializer())
            subclass(BottomTab.Profile::class, BottomTab.Profile::class.serializer())
            subclass(BottomTab.Cart::class, BottomTab.Cart::class.serializer())
            subclass(BottomTab.Shortcuts::class, BottomTab.Shortcuts::class.serializer())
        }
        polymorphic(Nav.Route::class) {
            subclass(HomeStart::class, HomeStart::class.serializer())
            subclass(ProfileStart::class, ProfileStart::class.serializer())
            subclass(CartStart::class, CartStart::class.serializer())
            subclass(ShortcutsStart::class, ShortcutsStart::class.serializer())
            subclass(ViewProduct::class, ViewProduct::class.serializer())
            subclass(Search::class, Search::class.serializer())
            subclass(SearchResults::class, SearchResults::class.serializer())
        }
    }
    serializersModule += jsonModule
}

private val jsonSerializer = Json {
    allowStructuredMapKeys = true
    serializersModule = savedNavStateConfiguration.serializersModule
}
