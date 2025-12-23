package com.zackjp.mockamazon.app.navigation

import androidx.navigation3.runtime.NavKey
import app.cash.turbine.test
import com.zackjp.mockamazon.shared.testutils.SetMainCoroutineDispatcher
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class) // advanceUntilIdle()
@ExtendWith(SetMainCoroutineDispatcher::class)
class AmazonNav3ControllerTest {

    private companion object {
        private val DEFAULT_TAB = FakeRouteA1
        private val BOTTOM_TABS = setOf(FakeRouteA1, FakeRouteB1, FakeRouteC1)
        private val INITIAL_BACKSTACK = listOf(DEFAULT_TAB)
    }

    private lateinit var navController: AmazonNav3Controller

    private val navStateSubject = NavState(
        defaultGroup = DEFAULT_TAB,
        groupHistory = mutableListOfWithoutStructuralEquality(DEFAULT_TAB),
        groupStacks = BOTTOM_TABS.associateWith { mutableListOfWithoutStructuralEquality() },
    )

    @BeforeEach
    fun setUp() {
        navController = AmazonNav3Controller(navStateSubject)
    }

    @Test
    fun init_WithInvalidDefaultGroup_ThrowsError() = runTest {
        val invalidGroup = FakeRouteC1

        shouldThrowExactly<IllegalStateException> {
            val invalidDefaultGroupNavState = NavState(
                defaultGroup = invalidGroup,
                groupHistory = mutableListOfWithoutStructuralEquality(FakeRouteA1),
                groupStacks = mapOf(
                    FakeRouteA1 to mutableListOfWithoutStructuralEquality(FakeRouteA1, FakeRouteA2),
                    FakeRouteB1 to mutableListOfWithoutStructuralEquality(FakeRouteB1),
                ),
            )

            AmazonNav3Controller(invalidDefaultGroupNavState)
        }
    }

    @Test
    fun init_WithNonStartRouteInGroupHistory_ThrowsError() = runTest {
        val nonStartRoute = FakeRouteA3

        shouldThrowExactly<IllegalStateException> {
            val invalidGroupHistoryNavState = NavState(
                defaultGroup = FakeRouteA1,
                groupHistory = mutableListOfWithoutStructuralEquality(FakeRouteA1, nonStartRoute),
                groupStacks = mapOf(
                    FakeRouteA1 to mutableListOfWithoutStructuralEquality(FakeRouteA1, FakeRouteA2),
                    FakeRouteB1 to mutableListOfWithoutStructuralEquality(FakeRouteB1),
                ),
            )

            AmazonNav3Controller(invalidGroupHistoryNavState)
        }
    }

    @Test
    fun init_WithTabInGroupHistory_ThrowsError() = runTest {
        val tabGroup = FakeGroupA

        shouldThrowExactly<IllegalArgumentException> {
            val invalidGroupHistoryNavState = NavState(
                defaultGroup = FakeRouteA1,
                groupHistory = mutableListOfWithoutStructuralEquality(FakeRouteA1, tabGroup),
                groupStacks = mapOf(
                    FakeRouteA1 to mutableListOfWithoutStructuralEquality(FakeRouteA1, FakeRouteA2),
                    FakeRouteB1 to mutableListOfWithoutStructuralEquality(FakeRouteB1),
                ),
            )

            AmazonNav3Controller(invalidGroupHistoryNavState)
        }
    }

    @Test
    fun init_WithZeroGroups_ThrowsError() = runTest {
        shouldThrowExactly<IllegalStateException> {
            val invalidGroupHistoryNavState = NavState(
                defaultGroup = FakeRouteA1,
                groupHistory = mutableListOfWithoutStructuralEquality(FakeRouteA1),
                groupStacks = mapOf(),
            )

            AmazonNav3Controller(invalidGroupHistoryNavState)
        }
    }

    @Test
    fun init_WithEmptyGroupHistory_AddsDefaultGroup() = runTest {
        val emptyHistory = mutableListOf<NavKey>()
        val navState = NavState(
            DEFAULT_TAB,
            groupHistory = emptyHistory,
            groupStacks = BOTTOM_TABS.associateWith { mutableListOfWithoutStructuralEquality() }
        )

        val navController = AmazonNav3Controller(navState) // init

        navController.currentBackStack.test {
            awaitItem() shouldBe BackStackState(
                backStack = listOf(DEFAULT_TAB),
                currentGroup = DEFAULT_TAB,
            )
        }
    }

    @Test
    fun init_WithEmptyBackStackForCurrentGroup_AddsTheStartRoute() = runTest {
        val emptyDefaultGroupStackNavState = NavState(
            defaultGroup = FakeRouteA1,
            groupHistory = mutableListOfWithoutStructuralEquality(FakeRouteA1, FakeRouteB1),
            groupStacks = mapOf(
                FakeRouteA1 to mutableListOfWithoutStructuralEquality(FakeRouteA1),
                FakeRouteB1 to mutableListOfWithoutStructuralEquality(),
            )
        )

        val navController = AmazonNav3Controller(emptyDefaultGroupStackNavState)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(FakeRouteA1, FakeRouteB1),
            currentGroup = FakeRouteB1
        )
    }

    @Test
    fun init_WithValidPopulatedNavState_InitializesWithNavState() = runTest {
        val validPopulatedNavState = NavState(
            defaultGroup = FakeRouteA1,
            groupHistory = mutableListOfWithoutStructuralEquality(FakeRouteA1, FakeRouteB1),
            groupStacks = mapOf(
                FakeRouteA1 to mutableListOfWithoutStructuralEquality(FakeRouteA1, FakeRouteA2),
                FakeRouteB1 to mutableListOfWithoutStructuralEquality(FakeRouteB1),
                FakeRouteC1 to mutableListOfWithoutStructuralEquality()
            )
        )

        val navController = AmazonNav3Controller(validPopulatedNavState)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(FakeRouteA1, FakeRouteA2, FakeRouteB1),
            currentGroup = FakeRouteB1,
        )
    }

    @Test
    fun navigateTo_DifferentGroupWithNoHistory_AddsStartRoute() = runTest {
        navController.navigateTo(FakeRouteB1)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = INITIAL_BACKSTACK + FakeRouteB1,
            currentGroup = FakeRouteB1,
        )
    }

    @Test
    fun navigateTo_DifferentGroupWithHistory_RestoresItsBackStackToFront() = runTest {
        val groupABackStack = INITIAL_BACKSTACK + FakeRouteA2 + FakeRouteA3
        val groupBBackStack = listOf(FakeRouteB1)

        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeRouteA3)
        navController.currentBackStack.value shouldBe BackStackState(
            backStack = groupABackStack,
            currentGroup = FakeRouteA1,
        )

        navController.navigateTo(FakeRouteB1)
        navController.currentBackStack.value shouldBe BackStackState(
            backStack = groupABackStack + groupBBackStack,
            currentGroup = FakeRouteB1,
        )

        navController.navigateTo(FakeRouteA1)
        navController.currentBackStack.value shouldBe BackStackState(
            backStack = groupBBackStack + groupABackStack,
            currentGroup = FakeRouteA1,
        )
    }

    @Test
    fun navigateTo_CurrentGroup_ResetsBackStackToStartRoute() = runTest {
        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeRouteA3)
        navController.navigateTo(FakeRouteB1)
        navController.navigateTo(FakeRouteA1)

        navController.navigateTo(FakeRouteA1)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(FakeRouteB1, FakeRouteA1),
            currentGroup = FakeRouteA1,
        )
    }

    @Test
    fun navigateTo_RouteBelongingToCurrentGroup_AppendsRoute() = runTest {
        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeRouteA3)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = INITIAL_BACKSTACK + FakeRouteA2 + FakeRouteA3,
            currentGroup = FakeRouteA1,
        )
    }

    @Test
    fun navigateTo_RouteBelongingToNullGroup_AppendsRouteInCurrentGroup() = runTest {
        navController.navigateTo(FakeRouteNoGroup1)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = INITIAL_BACKSTACK + FakeRouteNoGroup1,
            currentGroup = DEFAULT_TAB,
        )

        navController.navigateTo(FakeRouteB1)
        navController.navigateTo(FakeRouteNoGroup1)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(
                FakeRouteA1,
                FakeRouteNoGroup1,
                FakeRouteB1,
                FakeRouteNoGroup1,
            ),
            currentGroup = FakeRouteB1,
        )
    }

    @Test
    fun navigateTo_RouteBelongingToDifferentGroup_RestoresGroupToFrontAndAppendsRoute() = runTest {
        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeRouteB1)
        navController.navigateTo(FakeRouteA3)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(
                FakeRouteB1,
                FakeRouteA1,
                FakeRouteA2,
                FakeRouteA3,
            ),
            currentGroup = FakeRouteA1,
        )
    }

    @Test
    fun navigateTo_StartRouteHavingGroupOwnerWithDifferentRoute_ThrowsError() = runTest {
        val navState = NavState(
            defaultGroup = DEFAULT_TAB,
            groupHistory = mutableListOfWithoutStructuralEquality(DEFAULT_TAB),
            groupStacks = listOf(DEFAULT_TAB, FakeRouteWithMismatchedGroupOwner).associateWith {
                mutableListOfWithoutStructuralEquality()
            }
        )

        val navController = AmazonNav3Controller(navState)

        shouldThrowExactly<IllegalStateException> {
            navController.navigateTo(FakeRouteWithMismatchedGroupOwner)
        }
    }

    @Test
    fun popBackStack_WhenCurrentGroupHasOneItem_SetsCurrentGroupToPreviousGroup() = runTest {
        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeRouteA3)
        navController.navigateTo(FakeRouteB1)

        navController.popBackStack()

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(
                FakeRouteA1,
                FakeRouteA2,
                FakeRouteA3,
            ),
            currentGroup = FakeRouteA1,
        )
    }

    @Test
    fun popBackStack_WhenAllPriorVisitedGroupsAreEmpty_RestoresDefaultStartRoute() = runTest {
        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeRouteB1)
        navController.navigateTo(FakeRouteC1)
        navController.navigateTo(FakeRouteA1)

        navController.popBackStack() // pop A2
        navController.popBackStack() // pop A1
        navController.popBackStack() // pop C1
        navController.popBackStack() // pop B1

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(DEFAULT_TAB),
            currentGroup = DEFAULT_TAB,
        )
    }

    @Test
    fun popBackStack_WhenLastRemainingItemIsDefaultStartRoute_DoesNothing() = runTest {
        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeRouteB1)
        navController.navigateTo(FakeRouteC1)

        navController.popBackStack() // pop C1
        navController.popBackStack() // pop B1
        navController.popBackStack() // pop A2
        navController.popBackStack() // pop A1
        navController.popBackStack() // retains A1
        navController.popBackStack() // retains A1 again

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(DEFAULT_TAB),
            currentGroup = DEFAULT_TAB,
        )
    }

}


/**
 * Emulates NavBackStack/SnapshotStateList, which do not implement structural
 * equality. We can test and account for this by delegating to a regular
 * List implementation, and thereby exclude the equals method.
 */
private fun mutableListOfWithoutStructuralEquality(vararg keys: NavKey): MutableList<NavKey> {
    val impl = mutableListOf(*keys)
    return object : MutableList<NavKey> by impl {}
}

data object FakeGroupA : Nav.Tab() { override val startRouteFactory = { FakeRouteA1 } }
data object FakeGroupB : Nav.Tab() { override val startRouteFactory = { FakeRouteB1 } }
data object FakeGroupC : Nav.Tab() { override val startRouteFactory = { FakeRouteC1 } }
data object FakeMismatchedGroup : Nav.Tab() { override val startRouteFactory = { object : Nav.Route() {} } }

data object FakeRouteA1 : Nav.Route() { override fun groupOwner() = FakeGroupA }
data object FakeRouteA2 : Nav.Route() { override fun groupOwner() = FakeGroupA }
data object FakeRouteA3 : Nav.Route() { override fun groupOwner() = FakeGroupA }
data object FakeRouteB1 : Nav.Route() { override fun groupOwner() = FakeGroupB }
data object FakeRouteC1 : Nav.Route() { override fun groupOwner() = FakeGroupC }
data object FakeRouteNoGroup1 : Nav.Route() { override fun groupOwner() = null }
data object FakeRouteWithMismatchedGroupOwner : Nav.Route() { override fun groupOwner() = FakeMismatchedGroup }
