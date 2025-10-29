package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.toCartItem
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CartFakeApiDataSourceTest {

    private val productInMemoryDb = mockk<ProductInMemoryDb>()
    private lateinit var dataSource: CartFakeApiDataSource

    @BeforeEach
    fun setUp() {
        dataSource = CartFakeApiDataSource(productInMemoryDb)
    }

    @Test
    fun addToCart_WithValidProductId_ReturnsTrue() = runTest {
        val productInfo = fakeProductInfo(123)
        every { productInMemoryDb.getProductById(productInfo.id) } returns productInfo

        dataSource.addToCart(productInfo.id) shouldBe true
    }

    @Test
    fun addToCart_WithInvalidProductId_DoesNotReturnTrue() = runTest {
        every { productInMemoryDb.getProductById(0) } returns null

        dataSource.addToCart(0) shouldBe false
    }

    @Test
    fun getCartItems_WithValidProductIds_ReturnsCartItems() = runTest {
        val products = listOf(
            fakeProductInfo(123),
            fakeProductInfo(456),
            fakeProductInfo(789),
        )
        products.forEach {
            every { productInMemoryDb.getProductById(it.id) } returns it
        }

        dataSource.addToCart(products[0].id)
        dataSource.addToCart(products[1].id)
        dataSource.addToCart(products[2].id)

        val cartItems = dataSource.getCartItems()
        cartItems shouldBe products.map { it.toCartItem() }
    }

    @Test
    fun getCartItems_WithValidProductIdThatBecameInvalidProductId_ReturnsNothing() = runTest {
        val productInfo = fakeProductInfo(123)

        every { productInMemoryDb.getProductById(productInfo.id) } returns productInfo
        dataSource.addToCart(productInfo.id)
        dataSource.getCartItems() shouldBe listOf(productInfo.toCartItem())

        every { productInMemoryDb.getProductById(productInfo.id) } returns null
        dataSource.getCartItems() shouldBe emptyList()
    }

    private fun fakeProductInfo(number: Int): ProductInfo =
        ProductInfo(
            id = number,
            storeName = "Store Name $number",
            storeInitials = "SI$number",
            title = "Title $number",
            productRating = number % 5 + 0.5f,
            imageId = number,
            discount = number * .01f
        )
}