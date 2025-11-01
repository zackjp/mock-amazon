package com.example.fakeamazon.data

import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.toCartItem
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainOnly
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
    fun removeByProductId_RemovesProductFromCart() = runTest {
        val productInfo = fakeProductInfo(123)
        every { productInMemoryDb.getProductById(productInfo.id) } returns productInfo
        repeat(3) { dataSource.addToCart(123) }
        dataSource.getCartItems() shouldBe listOf(productInfo.toCartItem(3))

        dataSource.removeByProductId(123)

        dataSource.getCartItems() shouldBe emptyList()
    }

    @Test
    fun getCartItems_WithDistinctValidProductIds_ReturnsCartItems() = runTest {
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
        cartItems shouldContainOnly products.map { it.toCartItem(1) }
    }

    @Test
    fun getCartItems_WithDuplicateValidProductIds_ReturnsCartItemsWithQuantity() = runTest {
        val products = listOf(
            fakeProductInfo(123),
            fakeProductInfo(456),
        )
        products.forEach {
            every { productInMemoryDb.getProductById(it.id) } returns it
        }

        repeat(3) { dataSource.addToCart(products[0].id) }
        repeat(7) { dataSource.addToCart(products[1].id) }

        val cartItems = dataSource.getCartItems()
        cartItems shouldContainOnly listOf(
            products[0].toCartItem(3),
            products[1].toCartItem(7),
        )
    }

    @Test
    fun getCartItems_ReturnsCartItemsInOrderWithMostRecentFirst() = runTest {
        val products = listOf(
            fakeProductInfo(123),
            fakeProductInfo(456),
            fakeProductInfo(789),
        )
        products.forEach {
            every { productInMemoryDb.getProductById(it.id) } returns it
        }

        repeat(1) { dataSource.addToCart(products[0].id) }
        repeat(2) { dataSource.addToCart(products[1].id) }
        repeat(3) { dataSource.addToCart(products[2].id) }

        val cartItems = dataSource.getCartItems()
        cartItems shouldContainExactly listOf(
            products[2].toCartItem(3),
            products[1].toCartItem(2),
            products[0].toCartItem(1),
        )
    }

    @Test
    fun getCartItems_WithValidProductIdThatBecameInvalidProductId_ReturnsNothing() = runTest {
        val productInfo = fakeProductInfo(123)

        every { productInMemoryDb.getProductById(productInfo.id) } returns productInfo
        dataSource.addToCart(productInfo.id)
        dataSource.getCartItems() shouldContainOnly listOf(productInfo.toCartItem(1))

        every { productInMemoryDb.getProductById(productInfo.id) } returns null
        dataSource.getCartItems() shouldContainOnly emptyList()
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
