package com.example.fakeamazon.features.product

import com.example.fakeamazon.TestDispatcherProvider
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.data.ProductInMemoryDb
import com.example.fakeamazon.shared.model.ProductInfo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.beInstanceOf
import io.kotest.matchers.types.instanceOf
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
    val expectedProductInfo = mockk<ProductInfo>()

    lateinit var viewModel: ProductViewModel

    @BeforeEach
    fun setUp() {
        coEvery { cartRepository.addToCart(any()) } just Runs
        val productInMemoryDb = mockk<ProductInMemoryDb>()
        every { productInMemoryDb.getProductById(VALID_PRODUCT_ID) } returns expectedProductInfo
        every { productInMemoryDb.getProductById(INVALID_PRODUCT_ID) } returns null

        viewModel = ProductViewModel(
            cartRepository = cartRepository,
            dispatcherProvider = testDispatcherProvider,
            productInMemoryDb = productInMemoryDb,
        )
    }

    @Test
    fun init_StartsAsLoading() = runTest(dispatcher) {
        advanceUntilIdle()
        viewModel.uiState.first() shouldBe ProductUiState.Loading
    }

    @Test
    fun load_WithValidProductId_LoadsProductInfo() = runTest(dispatcher) {
        viewModel.load(VALID_PRODUCT_ID)

        viewModel.uiState.first() shouldNotBe instanceOf<ProductUiState.Loaded>()
        advanceUntilIdle()
        viewModel.uiState.first() shouldBe ProductUiState.Loaded(expectedProductInfo)
    }

    @Test
    fun load_WithInvalidProductId_EmitsError() = runTest(dispatcher) {
        // Arrange: Init with non-null data
        viewModel.load(VALID_PRODUCT_ID)
        advanceUntilIdle()

        viewModel.load(INVALID_PRODUCT_ID)

        viewModel.uiState.first() shouldNot beInstanceOf<ProductUiState.Error>()
        advanceUntilIdle()
        viewModel.uiState.first() shouldBe ProductUiState.Error
    }

    @Test
    fun addToCart_WithValidProductId_AddsToCart() = runTest(dispatcher) {
        viewModel.addToCart(123)
        coVerify(exactly = 0) { cartRepository.addToCart(123) }

        advanceUntilIdle()
        coVerify { cartRepository.addToCart(123) }
    }
}
