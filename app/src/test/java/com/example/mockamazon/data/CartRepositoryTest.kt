package com.example.mockamazon.data

import com.example.mockamazon.shared.model.CartItem
import com.example.mockamazon.shared.model.ProductInfo
import com.example.mockamazon.shared.model.fakeInfo
import com.example.mockamazon.shared.model.toCartItem
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CartRepositoryTest {

    val cartFakeApiDataSource = mockk<CartFakeApiDataSource>()

    lateinit var repo: CartRepository

    @BeforeEach
    fun setUp() {
        repo = CartRepository(cartFakeApiDataSource)
    }

    @Test
    fun getCartItems_HavingMultipleCartItemsFromApi_ReturnsItems() = runTest {
        val cartItem1 = fakeCartItem(123)
        val cartItem2 = fakeCartItem(456)
        coEvery { cartFakeApiDataSource.getCartItems() } returns listOf(
            cartItem1,
            cartItem2,
        )

        repo.getCartItems() shouldBe listOf(cartItem1, cartItem2)
    }

    @Test
    fun getCartItems_HavingNoCartItemsFromApi_ReturnsEmptyList() = runTest {
        coEvery { cartFakeApiDataSource.getCartItems() } returns emptyList()

        repo.getCartItems() shouldBe emptyList()
    }

    @Test
    fun addToCart_AddsToCartApi() = runTest {
        coEvery { cartFakeApiDataSource.addToCart(any()) } returns true

        repo.addToCart(123)

        coVerify { cartFakeApiDataSource.addToCart(123) }
    }

    @Test
    fun removeByProductId_RemovesProductIdFromCartApi() = runTest {
        coEvery { cartFakeApiDataSource.removeByProductId(123) } just Runs

        repo.removeByProductId(123)

        coVerify { cartFakeApiDataSource.removeByProductId(123) }
    }

    @Test
    fun decrementByProductId_DecrementsProductIdFromCartApi() = runTest {
        coEvery { cartFakeApiDataSource.decrementByProductId(123) } just Runs

        repo.decrementByProductId(123)

        coVerify { cartFakeApiDataSource.decrementByProductId(123) }
    }

    @Test
    fun setQuantity_WhenQuantityIsZero_setsQuantityFromCartApi() = runTest {
        coEvery { cartFakeApiDataSource.setQuantity(123, 0) } just Runs

        repo.setQuantity(123, 0)

        coVerify { cartFakeApiDataSource.setQuantity(123, 0) }
    }

    @Test
    fun setQuantity_WhenQuantityIsGreaterThanZero_setsQuantityFromCartApi() = runTest {
        val productId = 123
        val quantity = 5
        coEvery { cartFakeApiDataSource.setQuantity(productId, quantity) } just Runs

        repo.setQuantity(productId, quantity)

        coVerify { cartFakeApiDataSource.setQuantity(productId, quantity) }
    }

    @Test
    fun setQuantity_WhenQuantityIsNegative_setsQuantityFromCartApi() = runTest {
        val productId = 123
        val quantity = -5
        coEvery { cartFakeApiDataSource.setQuantity(productId, quantity) } just Runs

        repo.setQuantity(productId, quantity)

        coVerify { cartFakeApiDataSource.setQuantity(productId, quantity) }
    }

    private fun fakeCartItem(number: Int): CartItem =
        ProductInfo.fakeInfo(number).toCartItem()

}
