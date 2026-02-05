package com.zackjp.mockamazon.feature.cart

import app.cash.turbine.test
import com.zackjp.mockamazon.shared.data.CartRepository
import com.zackjp.mockamazon.shared.model.Cart
import com.zackjp.mockamazon.shared.model.CartItem
import com.zackjp.mockamazon.shared.testutils.SetMainCoroutineDispatcher
import com.zackjp.mockamazon.shared.testutils.model.fakeCart
import com.zackjp.mockamazon.shared.testutils.model.fakeItem
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
    private val expectedCartItems = listOf(CartItem.Companion.fakeItem(123).copy(quantity = 7))
    private val expectedCart = Cart.Companion.fakeCart(expectedCartItems)

    private lateinit var viewModel: CartViewModel

    @BeforeEach
    fun setUp() {
        coEvery { repository.getCart() } returns expectedCart
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
            awaitItem() shouldBe loaded(expectedCart)
        }
    }

    @Test
    fun load_WhenAlreadyLoaded_EmitsReloadingStateAndReloadsDataFromRepository() = runTest {
        val updatedCartItems =
            listOf(CartItem.Companion.fakeItem(987), CartItem.Companion.fakeItem(654))
        val updatedCart = Cart.Companion.fakeCart(updatedCartItems)

        viewModel.screenState.test {
            viewModel.load()

            awaitItem() shouldBe CartScreenState.Loading
            awaitItem() shouldBe loaded(expectedCart)

            coEvery { repository.getCart() } returns updatedCart
            viewModel.load()
            awaitItem() shouldBe loaded(expectedCart, isReloading = true)
            awaitItem() shouldBe loaded(updatedCart, isReloading = false)
        }
    }

    @Test
    fun removeByProductId_RemovesFromRepositoryAndUpdatesState() = runTest {
        val newCartItems = listOf(CartItem.Companion.fakeItem(987))
        val newCart = Cart.Companion.fakeCart(newCartItems)
        coEvery { repository.getCart() } returns newCart

        viewModel.screenState.test {
            awaitItem() shouldBe CartScreenState.Loading

            viewModel.removeByProductId(123)

            awaitItem() shouldBe loaded(newCart)
            coVerify { repository.removeByProductId(123) }
        }
    }

    @Test
    fun incrementCartItem_SetsRepositoryQuantityAndUpdatesState() = runTest {
        val cartItem = expectedCartItems[0]
        val updatedCartItems = listOf(
            CartItem.Companion.fakeItem(987),
            CartItem.Companion.fakeItem(654),
        )
        val updatedCart = Cart.Companion.fakeCart(updatedCartItems)

        viewModel.load()
        advanceUntilIdle()

        viewModel.screenState.test {
            awaitItem() shouldBe loaded(expectedCart)

            coEvery { repository.getCart() } returns updatedCart
            viewModel.incrementCartItem(cartItem.id)

            awaitItem() shouldBe loaded(updatedCart)
            coVerify { repository.setQuantity(cartItem.id, cartItem.quantity + 1) }
        }
    }

    @Test
    fun decrementCartItem_SetsRepositoryQuantityAndUpdatesState() = runTest {
        val cartItem = expectedCartItems[0]
        val updatedCartItems = listOf(
            CartItem.Companion.fakeItem(987),
            CartItem.Companion.fakeItem(654),
        )
        val updatedCart = Cart.Companion.fakeCart(updatedCartItems)

        viewModel.load()
        advanceUntilIdle()

        viewModel.screenState.test {
            awaitItem() shouldBe instanceOf<CartScreenState.Loaded>()

            coEvery { repository.getCart() } returns updatedCart
            viewModel.decrementCartItem(cartItem.id)

            awaitItem() shouldBe loaded(updatedCart)
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

    private fun loaded(
        cart: Cart,
        isReloading: Boolean = false,
    ): CartScreenState.Loaded = CartScreenState.Loaded(
        cartItems = cart.cartItems,
        isReloading = isReloading,
        totalPriceUSD = cart.totalPriceUSD
    )
}