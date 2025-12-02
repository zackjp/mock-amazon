package com.example.mockamazon.data

import com.example.mockamazon.TestDispatcherProvider
import com.example.mockamazon.shared.model.ProductInfo
import com.example.mockamazon.shared.model.fakeInfo
import com.example.mockamazon.shared.model.toCartItem
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CartFakeApiDataSourceTest {

    private companion object {
        private val PRODUCT_IN_CART = ProductInfo.fakeInfo(123)
        private val PRODUCT_NOT_IN_CART = ProductInfo.fakeInfo(456)
        private const val PRODUCT_IN_CART_QUANTITY = 7
        private val EXISTING_CART_ITEMS =
            listOf(PRODUCT_IN_CART.toCartItem(quantity = PRODUCT_IN_CART_QUANTITY))
        private val EXISTING_CART_PRICE_USD = EXISTING_CART_ITEMS.sumOf {
            (it.priceUSD.toDouble() * it.quantity)
        }.toFloat()
    }

    private val testDispatcherProvider = TestDispatcherProvider()
    private val testDispatcher = testDispatcherProvider.default

    private val productInMemoryDb = mockk<ProductInMemoryDb>()
    private lateinit var dataSource: CartFakeApiDataSource

    @BeforeEach
    fun setUp() {
        listOf(PRODUCT_IN_CART, PRODUCT_NOT_IN_CART).forEach {
            coEvery { productInMemoryDb.getProductById(it.id) } returns it
        }

        dataSource = CartFakeApiDataSource(
            dispatcherProvider = testDispatcherProvider,
            productInMemoryDb = productInMemoryDb,
        )

        runTest(testDispatcher) {
            repeat(PRODUCT_IN_CART_QUANTITY) {
                dataSource.addToCart(PRODUCT_IN_CART.id)
            }
        }
    }

    @Test
    fun addToCart_WithValidProductId_ReturnsTrue() = runTest(testDispatcher) {
        val productInfo = ProductInfo.fakeInfo(123)
        every { productInMemoryDb.getProductById(productInfo.id) } returns productInfo

        dataSource.addToCart(productInfo.id) shouldBe true
    }

    @Test
    fun addToCart_WithInvalidProductId_ReturnsFalse() = runTest(testDispatcher) {
        every { productInMemoryDb.getProductById(0) } returns null

        dataSource.addToCart(0) shouldBe false
    }

    @Test
    fun addToCart_WithDistinctValidProductIds_ReturnsCartItems() = runTest(testDispatcher) {
        val products = listOf(
            ProductInfo.fakeInfo(11),
            ProductInfo.fakeInfo(13),
            ProductInfo.fakeInfo(17),
        )
        products.forEach {
            every { productInMemoryDb.getProductById(it.id) } returns it
        }

        dataSource.addToCart(products[0].id)
        dataSource.addToCart(products[1].id)
        dataSource.addToCart(products[2].id)

        val cartItems = dataSource.getCart().cartItems
        cartItems shouldContainExactlyInAnyOrder products.map { it.toCartItem(1) } + EXISTING_CART_ITEMS
    }

    @Test
    fun addToCart_WithRepeatedValidProductIds_ReturnsCartItemsWithQuantity() =
        runTest(testDispatcher) {
            val products = listOf(
                ProductInfo.fakeInfo(11),
                ProductInfo.fakeInfo(13),
            )
            products.forEach {
                every { productInMemoryDb.getProductById(it.id) } returns it
            }

            repeat(3) { dataSource.addToCart(products[0].id) }
            repeat(7) { dataSource.addToCart(products[1].id) }

            val cartItems = dataSource.getCart().cartItems
            cartItems shouldContainExactlyInAnyOrder listOf(
                products[0].toCartItem(3),
                products[1].toCartItem(7),
            ) + EXISTING_CART_ITEMS
        }

    @Test
    fun setQuantity_WithZeroQuantityWhenProductNotInCart_DoesNothing() = runTest(testDispatcher) {
        val product = PRODUCT_NOT_IN_CART

        dataSource.setQuantity(product.id, 0)

        dataSource.getCart().cartItems shouldBe EXISTING_CART_ITEMS
    }

    @Test
    fun setQuantity_WithZeroQuantityWhenProductInCart_RemovesItem() = runTest(testDispatcher) {
        val product = PRODUCT_IN_CART

        dataSource.setQuantity(product.id, 0)

        dataSource.getCart().cartItems shouldBe emptyList()
    }

    @Test
    fun setQuantity_WithPositiveQuantityWhenProductNotInCart_SetsQuantity() = runTest(testDispatcher) {
        val product = PRODUCT_NOT_IN_CART
        val quantity = 13

        dataSource.setQuantity(product.id, quantity)

        dataSource.getCart().cartItems shouldContainExactly listOf(product.toCartItem(quantity = quantity)) + EXISTING_CART_ITEMS
    }

    @Test
    fun setQuantity_WithPositiveQuantityWhenProductInCart_SetsQuantity() = runTest(testDispatcher) {
        val product = PRODUCT_IN_CART
        val newQuantity = PRODUCT_IN_CART_QUANTITY * 10

        dataSource.setQuantity(product.id, newQuantity)

        dataSource.getCart().cartItems shouldContainExactly listOf(product.toCartItem(quantity = newQuantity))
    }

    @Test
    fun setQuantity_WithNegativeQuantityWhenProductNotInCart_DoesNothing() = runTest(testDispatcher) {
        val product = PRODUCT_NOT_IN_CART
        val quantity = -3

        dataSource.setQuantity(product.id, quantity)

        dataSource.getCart().cartItems shouldContainExactly EXISTING_CART_ITEMS
    }

    @Test
    fun setQuantity_WithNegativeQuantityWhenProductInCart_RemovesItem() = runTest(testDispatcher) {
        val product = PRODUCT_IN_CART

        dataSource.setQuantity(product.id, 0)

        dataSource.getCart().cartItems shouldBe emptyList()
    }

    @Test
    fun removeByProductId_RemovesProductFromCart() = runTest(testDispatcher) {
        val product = PRODUCT_IN_CART
        dataSource.getCart().cartItems shouldBe listOf(product.toCartItem(PRODUCT_IN_CART_QUANTITY))

        dataSource.removeByProductId(product.id)

        dataSource.getCart().cartItems shouldBe emptyList()
    }

    @Test
    fun getCart_ReturnsCartItemsInOrderWithMostRecentFirst() = runTest(testDispatcher) {
        val products = listOf(
            ProductInfo.fakeInfo(11),
            ProductInfo.fakeInfo(13),
            ProductInfo.fakeInfo(17),
        )
        products.forEach {
            every { productInMemoryDb.getProductById(it.id) } returns it
        }

        repeat(1) { dataSource.addToCart(products[0].id) }
        repeat(2) { dataSource.addToCart(products[1].id) }
        repeat(3) { dataSource.addToCart(products[2].id) }

        val cartItems = dataSource.getCart().cartItems
        cartItems shouldContainExactly listOf(
            products[2].toCartItem(3),
            products[1].toCartItem(2),
            products[0].toCartItem(1),
        ) + EXISTING_CART_ITEMS
    }

    @Test
    fun getCart_WithValidProductThatBecameInvalidProduct_ReturnsValidProductsOnly() =
        runTest(testDispatcher) {
            val productInfo = PRODUCT_NOT_IN_CART

            dataSource.addToCart(productInfo.id)
            dataSource.getCart().cartItems shouldContainExactly listOf(productInfo.toCartItem(1)) + EXISTING_CART_ITEMS

            every { productInMemoryDb.getProductById(productInfo.id) } returns null
            dataSource.getCart().cartItems shouldContainExactly EXISTING_CART_ITEMS
        }

    @Test
    fun getCart_CalculatesTotalPriceUSD() = runTest(testDispatcher) {
        val totalPriceUSD = dataSource.getCart().totalPriceUSD

        totalPriceUSD shouldBe EXISTING_CART_PRICE_USD
    }

    @Test
    fun decrementByProductId_WhenReachesZero_RemovesAsCartItem() = runTest(testDispatcher) {
        val productInfo = PRODUCT_NOT_IN_CART

        dataSource.addToCart(productInfo.id)
        dataSource.addToCart(productInfo.id)
        dataSource.addToCart(productInfo.id)

        dataSource.getCart().cartItems shouldContainExactly listOf(productInfo.toCartItem(3)) + EXISTING_CART_ITEMS

        dataSource.decrementByProductId(productInfo.id)
        dataSource.decrementByProductId(productInfo.id)
        dataSource.decrementByProductId(productInfo.id)

        dataSource.getCart().cartItems shouldBe EXISTING_CART_ITEMS
    }

    @Test
    fun decrementByProductId_WhenNonZero_DecrementsCartItemQuantity() = runTest(testDispatcher) {
        val productInfo = PRODUCT_NOT_IN_CART

        dataSource.addToCart(productInfo.id)
        dataSource.addToCart(productInfo.id)
        dataSource.addToCart(productInfo.id)

        dataSource.getCart().cartItems shouldContainExactly listOf(productInfo.toCartItem(3)) + EXISTING_CART_ITEMS

        dataSource.decrementByProductId(productInfo.id)

        dataSource.getCart().cartItems shouldContainExactly listOf(productInfo.toCartItem(2)) + EXISTING_CART_ITEMS
    }

    @Test
    fun decrementByProductId_WhenNotAlreadyInCart_DoesNothing() = runTest(testDispatcher) {
        val productInfo = PRODUCT_NOT_IN_CART

        dataSource.decrementByProductId(productInfo.id)

        dataSource.getCart().cartItems shouldBe EXISTING_CART_ITEMS
    }

}
