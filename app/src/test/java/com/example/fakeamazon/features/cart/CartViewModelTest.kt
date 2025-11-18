package com.example.fakeamazon.features.cart

import app.cash.turbine.test
import com.example.fakeamazon.TestDispatcherProvider
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.shared.model.CartItem
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class) // advanceUntilIdle()
class CartViewModelTest {

    private val testDispatcherProvider = TestDispatcherProvider()
    private val dispatcher = testDispatcherProvider.default
    private val scheduler = dispatcher.testCoroutineScheduler
    private val repository = mockk<CartRepository>()
    private val expectedCartItems = listOf(mockk<CartItem>())

    private lateinit var viewModel: CartViewModel

    @BeforeEach
    fun setUp() {
        coEvery { repository.getCartItems() } returns expectedCartItems
        coEvery { repository.removeByProductId(any()) } just Runs

        viewModel = CartViewModel(
            cartRepository = repository,
            dispatcherProvider = testDispatcherProvider,
        )
    }

    @Test
    fun init_StartsAsLoading() = runTest(scheduler) {
        viewModel.screenState.test {
            awaitItem() shouldBe CartScreenState.Loading
        }
    }

    @Test
    fun load_LoadsDataFromRepository() = runTest(scheduler) {
        viewModel.screenState.test {
            viewModel.load()

            awaitItem() shouldBe CartScreenState.Loading
            awaitItem() shouldBe CartScreenState.Loaded(expectedCartItems)
        }
    }

    @Test
    fun removeByProductId_RemovesFromRepositoryAndUpdatesState() = runTest(scheduler) {
        val newCartItems = listOf(mockk<CartItem>())
        coEvery { repository.getCartItems() } returns newCartItems

        viewModel.screenState.test {
            awaitItem() shouldBe CartScreenState.Loading

            viewModel.removeByProductId(123)
            advanceUntilIdle()

            coVerify { repository.removeByProductId(123) }
            awaitItem() shouldBe CartScreenState.Loaded(newCartItems)
        }
    }

}
