package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.CartItem
import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.toCartItem
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
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
    fun getCartItem_HavingMultipleCartItemsFromApi_ReturnsLastItem() = runTest {
        val cartItem1 = fakeCartItem(123)
        val cartItem2 = fakeCartItem(456)
        coEvery { cartFakeApiDataSource.getCartItems() } returns listOf(
            cartItem1,
            cartItem2,
        )

        repo.getCartItem() shouldBe cartItem2
    }

    @Test
    fun getCartItem_HavingNoCartItemsFromApi_ReturnsNull() = runTest {
        coEvery { cartFakeApiDataSource.getCartItems() } returns emptyList()

        repo.getCartItem() should beNull()
    }

    @Test
    fun addToCart_AddsToCartApi() = runTest {
        coEvery { cartFakeApiDataSource.addToCart(any()) } returns true

        repo.addToCart(123)

        coVerify { cartFakeApiDataSource.addToCart(123) }
    }

    private fun fakeCartItem(number: Int): CartItem =
        ProductInfo(
            id = number,
            storeName = "Store Name $number",
            storeInitials = "SI$number",
            title = "Title $number",
            productRating = number % 5 + 0.5f,
            imageId = number,
            discount = number * .01f
        ).toCartItem()

}
