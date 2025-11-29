package com.example.fakeamazon.features.cart

import app.cash.turbine.test
import com.example.fakeamazon.SetMainCoroutineDispatcher
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
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class) // advanceUntilIdle()
@ExtendWith(SetMainCoroutineDispatcher::class)
class CartViewModelTest {

    private val repository = mockk<CartRepository>()
    private val expectedCartItems = listOf(CartItem.fakeItem(123).copy(quantity = 7))

    private lateinit var viewModel: CartViewModel

    @BeforeEach
    fun setUp() {
        coEvery { repository.getCartItems() } returns expectedCartItems
        coEvery { repository.addToCart(any()) } just Runs
        coEvery { repository.removeByProductId(any()) } just Runs
        coEvery { repository.decrementByProductId(any()) } just Runs
        coEvery { repository.setQuantity(any(), any()) } just Runs

        viewModel = CartViewModel(cartRepository = repository)
    }

    @Test
    fun init_StartsAsLoading() = runTest {
        viewModel.screenState.test {
            awaitItem() shouldBe CartScreenState.Loading
        }
    }

    @Test
    fun load_WhenNotLoaded_LoadsDataFromRepository() = runTest {
        viewModel.screenState.test {
            viewModel.load()

            awaitItem() shouldBe CartScreenState.Loading
            awaitItem() shouldBe CartScreenState.Loaded(expectedCartItems)
        }
    }

    @Test
    fun load_WhenAlreadyLoaded_EmitsReloadingStateAndReloadsDataFromRepository() = runTest {
        val updatedCartItems = listOf(CartItem.fakeItem(987), CartItem.fakeItem(654))

        viewModel.screenState.test {
            viewModel.load()

            awaitItem() shouldBe CartScreenState.Loading
            awaitItem() shouldBe CartScreenState.Loaded(expectedCartItems)

            coEvery { repository.getCartItems() } returns updatedCartItems
            viewModel.load()
            awaitItem() shouldBe CartScreenState.Loaded(expectedCartItems, isReloading = true)
            awaitItem() shouldBe CartScreenState.Loaded(updatedCartItems, isReloading = false)
        }
    }

    @Test
    fun removeByProductId_RemovesFromRepositoryAndUpdatesState() = runTest {
        val newCartItems = listOf(mockk<CartItem>())
        coEvery { repository.getCartItems() } returns newCartItems

        viewModel.screenState.test {
            awaitItem() shouldBe CartScreenState.Loading

            viewModel.removeByProductId(123)

            awaitItem() shouldBe CartScreenState.Loaded(newCartItems)
            coVerify { repository.removeByProductId(123) }
        }
    }

    @Test
    fun incrementCartItem_SetsRepositoryQuantityAndUpdatesState() = runTest {
        val cartItem = expectedCartItems[0]
        val updatedCartItems = listOf(
            CartItem.fakeItem(987),
            CartItem.fakeItem(654),
        )

        viewModel.load()
        advanceUntilIdle()

        viewModel.screenState.test {
            awaitItem() shouldBe CartScreenState.Loaded(expectedCartItems)

            coEvery { repository.getCartItems() } returns updatedCartItems
            viewModel.incrementCartItem(cartItem.id)

            awaitItem() shouldBe CartScreenState.Loaded(updatedCartItems)
            coVerify { repository.setQuantity(cartItem.id, cartItem.quantity + 1) }
        }
    }

    @Test
    fun decrementCartItem_SetsRepositoryQuantityAndUpdatesState() = runTest {
        val cartItem = expectedCartItems[0]
        val updatedCartItems = listOf(
            CartItem.fakeItem(987),
            CartItem.fakeItem(654),
        )

        viewModel.load()
        advanceUntilIdle()

        viewModel.screenState.test {
            awaitItem() shouldBe instanceOf<CartScreenState.Loaded>()

            coEvery { repository.getCartItems() } returns updatedCartItems
            viewModel.decrementCartItem(cartItem.id)

            awaitItem() shouldBe CartScreenState.Loaded(updatedCartItems)
            coVerify { repository.setQuantity(cartItem.id, cartItem.quantity - 1) }
        }
    }

    @Test
    fun incrementCartItem_WithUnknownCartItem_DoesNothing() = runTest {
        val cartItem = expectedCartItems[0]
        val unknownCartItemId = cartItem.id * 10

        viewModel.load()
        advanceUntilIdle()

        viewModel.screenState.test {
            awaitItem() shouldBe instanceOf<CartScreenState.Loaded>()

            viewModel.incrementCartItem(unknownCartItemId)

            expectNoEvents()
            coVerify(exactly = 0) { repository.setQuantity(any(), any()) }
        }
    }

    @Test
    fun decrementCartItem_WithUnknownCartItem_DoesNothing() = runTest {
        val cartItem = expectedCartItems[0]
        val unknownCartItemId = cartItem.id * 10

        viewModel.load()
        advanceUntilIdle()

        viewModel.screenState.test {
            awaitItem() shouldBe instanceOf<CartScreenState.Loaded>()

            viewModel.incrementCartItem(unknownCartItemId)

            expectNoEvents()
            coVerify(exactly = 0) { repository.setQuantity(any(), any()) }
        }
    }

}
