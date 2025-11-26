package com.example.fakeamazon.features.cart

import app.cash.turbine.test
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.shared.model.CartItem
import com.example.fakeamazon.shared.model.fakeItem
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class) // advanceUntilIdle()
class CartViewModelTest {

    private val repository = mockk<CartRepository>()
    private val expectedCartItems = listOf(mockk<CartItem>())

    private lateinit var viewModel: CartViewModel

    @BeforeEach
    fun setUp() {
        coEvery { repository.getCartItems() } returns expectedCartItems
        coEvery { repository.addToCart(any()) } just Runs
        coEvery { repository.removeByProductId(any()) } just Runs
        coEvery { repository.decrementByProductId(any()) } just Runs

        viewModel = CartViewModel(cartRepository = repository)
    }

    @Test
    fun init_StartsAsLoading() = runTest {
        viewModel.screenState.test {
            awaitItem() shouldBe CartScreenState.Loading
        }
    }

    @Test
    fun load_LoadsDataFromRepository() = runTest {
        viewModel.screenState.test {
            viewModel.load()

            awaitItem() shouldBe CartScreenState.Loading
            awaitItem() shouldBe CartScreenState.Loaded(expectedCartItems)
        }
    }

    @Test
    fun removeByProductId_RemovesFromRepositoryAndUpdatesState() = runTest {
        val newCartItems = listOf(mockk<CartItem>())
        coEvery { repository.getCartItems() } returns newCartItems

        viewModel.screenState.test {
            awaitItem() shouldBe CartScreenState.Loading

            viewModel.removeByProductId(123)

            coVerify { repository.removeByProductId(123) }
            awaitItem() shouldBe CartScreenState.Loaded(newCartItems)
        }
    }

    @Test
    fun incrementByProductId_IncrementsToRepositoryAndUpdatesState() = runTest {
        val updatedCartItems = listOf(
            CartItem.fakeItem(987),
            CartItem.fakeItem(654),
        )

        viewModel.screenState.test {
            awaitItem() shouldBe CartScreenState.Loading
            viewModel.load()
            awaitItem() shouldBe CartScreenState.Loaded(expectedCartItems)

            coEvery { repository.getCartItems() } returns updatedCartItems
            viewModel.incrementCartItem(123)

            coVerify { repository.addToCart(123) }
            awaitItem() shouldBe CartScreenState.Loaded(updatedCartItems)
        }
    }

    @Test
    fun decrementCartItem_DecrementsFromRepositoryAndUpdatesState() = runTest {
        val updatedCartItems = listOf(
            CartItem.fakeItem(987),
            CartItem.fakeItem(654),
        )

        viewModel.screenState.test {
            awaitItem() shouldBe CartScreenState.Loading
            viewModel.load()
            awaitItem() shouldBe instanceOf<CartScreenState.Loaded>()

            coEvery { repository.getCartItems() } returns updatedCartItems
            viewModel.decrementCartItem(123)

            coVerify { repository.decrementByProductId(123) }
            awaitItem() shouldBe CartScreenState.Loaded(updatedCartItems)
        }
    }

}
