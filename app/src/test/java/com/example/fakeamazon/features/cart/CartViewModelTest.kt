package com.example.fakeamazon.features.cart

import com.example.fakeamazon.TestDispatcherProvider
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.shared.model.CartItem
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CartViewModelTest {

    private val testDispatcherProvider = TestDispatcherProvider()
    private val dispatcher = testDispatcherProvider.default
    private val scheduler = dispatcher.testCoroutineScheduler
    private val expectedCartItem = mockk<CartItem>()

    private lateinit var viewModel: CartViewModel

    @BeforeEach
    fun setUp() {
        val repository = mockk<CartRepository>()
        coEvery { repository.getCartItem() } returns expectedCartItem

        viewModel = CartViewModel(
            cartRepository = repository,
            dispatcherProvider = testDispatcherProvider,
        )
    }

    @Test
    fun init_DoesNotLoadData() = runTest(scheduler) {
        scheduler.advanceUntilIdle()

        viewModel.cartItem.first() should beNull()
    }

    @Test
    fun load_LoadsDataFromRepository() = runTest(scheduler) {
        viewModel.load()

        viewModel.cartItem.first() should beNull()
        scheduler.advanceUntilIdle()
        viewModel.cartItem.first { it != null } shouldBe expectedCartItem
    }

}
