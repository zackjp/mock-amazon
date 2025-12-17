package com.example.mockamazon.app.navigation

import app.cash.turbine.test
import com.example.mockamazon.SetMainCoroutineDispatcher
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
        private val DEFAULT_TAB = FakeGroupA
        private val BOTTOM_TABS = setOf(FakeGroupA, FakeGroupB, FakeGroupC)
        private val NON_MATCHING_DEFAULT_TAB = InvalidGroup
        private val INITIAL_BACKSTACK = listOf(DEFAULT_TAB.startRouteFactory())
    }

    private lateinit var navController: AmazonNav3Controller

    @BeforeEach
    fun setUp() {
        navController = AmazonNav3Controller(
            defaultGroup = DEFAULT_TAB,
            tabs = BOTTOM_TABS,
        )
    }

    @Test
    fun init_WithValidDefaultTab_SetsBackstackToItsStartRoute() = runTest {
        navController.currentBackStack.test {
            awaitItem() shouldBe BackStackState(
                backStack = INITIAL_BACKSTACK,
                currentGroup = DEFAULT_TAB,
            )
        }
    }

    @Test
    fun init_WithNonMatchingDefaultTab_ThrowsError() = runTest {
        shouldThrowExactly<IllegalStateException> {
            AmazonNav3Controller(
                defaultGroup = NON_MATCHING_DEFAULT_TAB,
                tabs = BOTTOM_TABS,
            )
        }
    }

    @Test
    fun init_WithNavStatePopulatedAndValid_InitializesWithNavState() = runTest {
        val validNavState = NavState(
            defaultGroup = FakeGroupA,
            groupHistory = listOf(FakeGroupA, FakeGroupB),
            groupStacks = mapOf(
                FakeGroupA to listOf(FakeRouteA1, FakeRouteA2),
                FakeGroupB to listOf(FakeRouteB1),
                FakeGroupC to emptyList()
            )
        )

        val navController = AmazonNav3Controller(validNavState)

        navController.getNavState() shouldBe validNavState
    }

    @Test
    fun init_WithNavStateHavingNonMatchingDefaultGroup_ThrowsError() = runTest {
        shouldThrowExactly<IllegalStateException> {
            val invalidDefaultGroupNavState = NavState(
                defaultGroup = FakeGroupC,
                groupHistory = listOf(FakeGroupA),
                groupStacks = mapOf(
                    FakeGroupA to listOf(FakeRouteA1, FakeRouteA2),
                    FakeGroupB to listOf(FakeRouteB1),
                )
            )

            AmazonNav3Controller(invalidDefaultGroupNavState)
        }
    }

    @Test
    fun init_WithNavStateHavingNonMatchingGroupHistory_ThrowsError() = runTest {
        shouldThrowExactly<IllegalStateException> {
            val invalidGroupHistoryNavState = NavState(
                defaultGroup = FakeGroupA,
                groupHistory = listOf(FakeGroupA, FakeGroupC),
                groupStacks = mapOf(
                    FakeGroupA to listOf(FakeRouteA1, FakeRouteA2),
                    FakeGroupB to listOf(FakeRouteB1),
                )
            )

            AmazonNav3Controller(invalidGroupHistoryNavState)
        }
    }

    @Test
    fun init_WithNavStateHavingEmptyBackStackForCurrentGroup_AddsTheStartRoute() = runTest {
        val emptyDefaultGroupStackNavState = NavState(
            defaultGroup = FakeGroupA,
            groupHistory = listOf(FakeGroupA, FakeGroupB),
            groupStacks = mapOf(
                FakeGroupA to listOf(FakeRouteA1),
                FakeGroupB to emptyList(),
            )
        )

        val navController = AmazonNav3Controller(emptyDefaultGroupStackNavState)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(FakeRouteA1, FakeRouteB1),
            currentGroup = FakeGroupB
        )
    }

    @Test
    fun navigateTo_DifferentGroupWithNoHistory_AddsStartRoute() = runTest {
        navController.navigateTo(FakeGroupB)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = INITIAL_BACKSTACK + FakeGroupB.startRouteFactory(),
            currentGroup = FakeGroupB,
        )
    }

    @Test
    fun navigateTo_DifferentGroupWithHistory_RestoresItsBackStackToFront() = runTest {
        val groupABackStack = INITIAL_BACKSTACK + FakeRouteA2 + FakeRouteA3
        val groupBBackStack = listOf(FakeGroupB.startRouteFactory())

        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeRouteA3)
        navController.currentBackStack.value shouldBe BackStackState(
            backStack = groupABackStack,
            currentGroup = FakeGroupA,
        )

        navController.navigateTo(FakeGroupB)
        navController.currentBackStack.value shouldBe BackStackState(
            backStack = groupABackStack + groupBBackStack,
            currentGroup = FakeGroupB,
        )

        navController.navigateTo(FakeGroupA)
        navController.currentBackStack.value shouldBe BackStackState(
            backStack = groupBBackStack + groupABackStack,
            currentGroup = FakeGroupA,
        )
    }

    @Test
    fun navigateTo_CurrentGroup_ResetsBackStackToStartRoute() = runTest {
        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeRouteA3)
        navController.navigateTo(FakeGroupB)
        navController.navigateTo(FakeGroupA)

        navController.navigateTo(FakeGroupA)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(FakeRouteB1, FakeGroupA.startRouteFactory()),
            currentGroup = FakeGroupA,
        )
    }

    @Test
    fun navigateTo_RouteBelongingToCurrentGroup_AppendsRoute() = runTest {
        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeRouteA3)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = INITIAL_BACKSTACK + FakeRouteA2 + FakeRouteA3,
            currentGroup = FakeGroupA,
        )
    }

    @Test
    fun navigateTo_RouteBelongingToNullGroup_AppendsRouteInCurrentGroup() = runTest {
        navController.navigateTo(FakeRouteNoGroup1)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = INITIAL_BACKSTACK + FakeRouteNoGroup1,
            currentGroup = DEFAULT_TAB,
        )

        navController.navigateTo(FakeGroupB)
        navController.navigateTo(FakeRouteNoGroup1)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(
                FakeRouteA1,
                FakeRouteNoGroup1,
                FakeRouteB1,
                FakeRouteNoGroup1,
            ),
            currentGroup = FakeGroupB,
        )
    }

    @Test
    fun navigateTo_RouteBelongingToInvalidGroup_ThrowsException() = runTest {
        shouldThrowExactly<IllegalStateException> {
            navController.navigateTo(FakeRouteInvalidGroup1)
        }
    }

    @Test
    fun navigateTo_RouteBelongingToDifferentGroup_RestoresGroupToFrontAndAppendsRoute() = runTest {
        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeGroupB)
        navController.navigateTo(FakeRouteA3)

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(
                FakeRouteB1,
                FakeRouteA1,
                FakeRouteA2,
                FakeRouteA3,
            ),
            currentGroup = FakeGroupA,
        )
    }

    @Test
    fun popBackStack_WhenCurrentGroupHasOneItem_SetsCurrentGroupToPreviousGroup() = runTest {
        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeRouteA3)
        navController.navigateTo(FakeGroupB)

        navController.popBackStack()

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(
                FakeRouteA1,
                FakeRouteA2,
                FakeRouteA3,
            ),
            currentGroup = FakeGroupA,
        )
    }

    @Test
    fun popBackStack_WhenAllPriorVisitedGroupsAreEmpty_RestoresDefaultStartRoute() = runTest {
        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeGroupB)
        navController.navigateTo(FakeGroupC)
        navController.navigateTo(FakeGroupA)

        navController.popBackStack() // pop A2
        navController.popBackStack() // pop A1
        navController.popBackStack() // pop C1
        navController.popBackStack() // pop B1

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(DEFAULT_TAB.startRouteFactory()),
            currentGroup = DEFAULT_TAB,
        )
    }

    @Test
    fun popBackStack_WhenLastRemainingItemIsDefaultStartRoute_DoesNothing() = runTest {
        navController.navigateTo(FakeRouteA2)
        navController.navigateTo(FakeGroupB)
        navController.navigateTo(FakeGroupC)

        navController.popBackStack() // pop C1
        navController.popBackStack() // pop B1
        navController.popBackStack() // pop A2
        navController.popBackStack() // pop A1
        navController.popBackStack() // retains A1
        navController.popBackStack() // retains A1 again

        navController.currentBackStack.value shouldBe BackStackState(
            backStack = listOf(DEFAULT_TAB.startRouteFactory()),
            currentGroup = DEFAULT_TAB,
        )
    }

}

data object FakeGroupA : Nav.Tab() { override val startRouteFactory = { FakeRouteA1 } }
data object FakeGroupB : Nav.Tab() { override val startRouteFactory = { FakeRouteB1 } }
data object FakeGroupC : Nav.Tab() { override val startRouteFactory = { FakeRouteB1 } }
data object InvalidGroup : Nav.Tab() { override val startRouteFactory = { FakeRouteInvalidGroup1 } }

data object FakeRouteA1 : Nav.Route() { override fun tabOwner() = FakeGroupA }
data object FakeRouteA2 : Nav.Route() { override fun tabOwner() = FakeGroupA }
data object FakeRouteA3 : Nav.Route() { override fun tabOwner() = FakeGroupA }
data object FakeRouteB1 : Nav.Route() { override fun tabOwner() = FakeGroupB }
data object FakeRouteC1 : Nav.Route() { override fun tabOwner() = FakeGroupC }
data object FakeRouteNoGroup1 : Nav.Route() { override fun tabOwner() = null }
data object FakeRouteInvalidGroup1 : Nav.Route() { override fun tabOwner() = InvalidGroup }
