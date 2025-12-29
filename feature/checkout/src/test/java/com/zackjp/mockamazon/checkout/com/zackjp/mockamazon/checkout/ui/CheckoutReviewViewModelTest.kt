package com.zackjp.mockamazon.checkout.com.zackjp.mockamazon.checkout.ui

import com.zackjp.mockamazon.checkout.ui.CheckoutReviewViewModel
import com.zackjp.mockamazon.checkout.ui.model.CheckoutState
import com.zackjp.mockamazon.shared.data.CartRepository
import com.zackjp.mockamazon.shared.data.UserRepository
import com.zackjp.mockamazon.shared.model.Cart
import com.zackjp.mockamazon.shared.model.CartItem
import com.zackjp.mockamazon.shared.model.User
import com.zackjp.mockamazon.shared.testutils.model.fakeCart
import com.zackjp.mockamazon.shared.testutils.model.fakeItem
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.orbitmvi.orbit.test.TestSettings
import org.orbitmvi.orbit.test.test
import java.util.concurrent.CancellationException

class CheckoutReviewViewModelTest {

    private val expectedCart = Cart.fakeCart(
        listOf(
            CartItem.fakeItem(123),
            CartItem.fakeItem(456),
        )
    )
    private val expectedUser = User(
        deliveryAddress = "Delivery Address 123",
        deliveryFullName = "Full Name 123",
        paymentMethodText = "Payment 123",
    )

    private val cartRepository = mockk<CartRepository>()
    private val userRepository = mockk<UserRepository>()

    private lateinit var viewModel: CheckoutReviewViewModel

    @BeforeEach
    fun setUp() {
        coEvery { cartRepository.getCart() } returns expectedCart
        coEvery { userRepository.getUser() } returns expectedUser

        viewModel = CheckoutReviewViewModel(
            cartRepository = cartRepository,
            userRepository = userRepository,
        )
    }

    @Test
    fun init_StartsAsLoading() = runTest {
        viewModel.test(this, settings = TestSettings(autoCheckInitialState = false)) {
            awaitState() shouldBe CheckoutState.Loading
        }
    }

    @Test
    fun load_WhenCartAndUserLoads_SetsLoadedState() = runTest {
        viewModel.test(this) {
            viewModel.load()

            awaitState() shouldBe CheckoutState.Loaded(
                cart = expectedCart,
                user = expectedUser,
            )
        }
    }

    @Test
    fun load_WhenCartLoadingFailsWithNonCancellation_SetsErrorState() = runTest {
        coEvery { cartRepository.getCart() } throws Exception("exception test")

        viewModel.test(this) {
            viewModel.load()

            awaitState() shouldBe CheckoutState.Error
        }
    }

    @Test
    fun load_WhenUserLoadingFailsWithNonCancellation_SetsErrorState() = runTest {
        coEvery { userRepository.getUser() } throws Exception("exception test")

        viewModel.test(this) {
            viewModel.load()

            awaitState() shouldBe CheckoutState.Error
        }
    }

    @Test
    fun load_WhenCartLoadingThrowsCancellation_DoesNothing() = runTest {
        coEvery { cartRepository.getCart() } throws CancellationException("cancellation test")

        viewModel.test(this) {
            viewModel.load()

            expectNoItems()
        }
    }

    @Test
    fun load_WhenUserLoadingThrowsCancellation_DoesNothing() = runTest {
        coEvery { userRepository.getUser() } throws CancellationException("cancellation test")

        viewModel.test(this) {
            viewModel.load()

            expectNoItems()
        }
    }

}
