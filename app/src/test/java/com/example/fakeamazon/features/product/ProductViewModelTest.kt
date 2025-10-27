package com.example.fakeamazon.features.product

import com.example.fakeamazon.TestDispatcherProvider
import com.example.fakeamazon.data.ProductStaticDataSource
import com.example.fakeamazon.shared.model.ProductInfo
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
    val scheduler = dispatcher.testCoroutineScheduler

    lateinit var viewModel: ProductViewModel

    @BeforeEach
    fun setUp() {
        val productStaticDataSource = mockk<ProductStaticDataSource>()
        every { productStaticDataSource.getProductById(VALID_PRODUCT_ID) } returns mockk<ProductInfo>()
        every { productStaticDataSource.getProductById(INVALID_PRODUCT_ID) } returns null

        viewModel = ProductViewModel(testDispatcherProvider, productStaticDataSource)
    }

    @Test
    fun init_DoesNotLoadInfo() = runTest(dispatcher) {
        scheduler.advanceUntilIdle()
        viewModel.productInfo.first() shouldBe null
    }

    @Test
    fun load_WithValidProductId_LoadsProductInfoAsync() = runTest(dispatcher) {
        viewModel.load(VALID_PRODUCT_ID)

        viewModel.productInfo.first() shouldBe null
        scheduler.advanceUntilIdle()
        viewModel.productInfo.first() shouldNotBe null
    }

    @Test
    fun load_WithInvalidProductId_ReturnsNullAsync() = runTest(dispatcher) {
        // Arrange: Init with non-null data
        viewModel.load(VALID_PRODUCT_ID)
        scheduler.advanceUntilIdle()

        viewModel.load(INVALID_PRODUCT_ID)

        viewModel.productInfo.first() shouldNotBe null
        scheduler.advanceUntilIdle()
        viewModel.productInfo.first() shouldBe null
    }

}
