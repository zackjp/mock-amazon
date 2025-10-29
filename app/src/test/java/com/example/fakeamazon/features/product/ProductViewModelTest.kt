package com.example.fakeamazon.features.product

import com.example.fakeamazon.TestDispatcherProvider
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.data.ProductInMemoryDb
import com.example.fakeamazon.shared.model.ProductInfo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class) // advanceUntilIdle()
class ProductViewModelTest {

    companion object {
        const val VALID_PRODUCT_ID: Int = 123
        const val INVALID_PRODUCT_ID: Int = -321
    }

    val testDispatcherProvider = TestDispatcherProvider()
    val dispatcher = testDispatcherProvider.default
    val cartRepository = mockk<CartRepository>()

    lateinit var viewModel: ProductViewModel

    @BeforeEach
    fun setUp() {
        coEvery { cartRepository.addToCart(any()) } just Runs
        val productInMemoryDb = mockk<ProductInMemoryDb>()
        every { productInMemoryDb.getProductById(VALID_PRODUCT_ID) } returns mockk<ProductInfo>()
        every { productInMemoryDb.getProductById(INVALID_PRODUCT_ID) } returns null

        viewModel = ProductViewModel(
            cartRepository = cartRepository,
            dispatcherProvider = testDispatcherProvider,
            productInMemoryDb = productInMemoryDb,
        )
    }

    @Test
    fun init_DoesNotLoadInfo() = runTest(dispatcher) {
        advanceUntilIdle()
        viewModel.productInfo.first() shouldBe null
    }

    @Test
    fun load_WithValidProductId_LoadsProductInfoAsync() = runTest(dispatcher) {
        viewModel.load(VALID_PRODUCT_ID)

        viewModel.productInfo.first() shouldBe null
        advanceUntilIdle()
        viewModel.productInfo.first() shouldNotBe null
    }

    @Test
    fun load_WithInvalidProductId_ReturnsNullAsync() = runTest(dispatcher) {
        // Arrange: Init with non-null data
        viewModel.load(VALID_PRODUCT_ID)
        advanceUntilIdle()

        viewModel.load(INVALID_PRODUCT_ID)

        viewModel.productInfo.first() shouldNotBe null
        advanceUntilIdle()
        viewModel.productInfo.first() shouldBe null
    }

    @Test
    fun addToCart_WithValidProductId_AddsToCartAsync() = runTest(dispatcher) {
        viewModel.addToCart(123)
        coVerify(exactly = 0) { cartRepository.addToCart(123) }

        advanceUntilIdle()
        coVerify { cartRepository.addToCart(123) }
    }
}
