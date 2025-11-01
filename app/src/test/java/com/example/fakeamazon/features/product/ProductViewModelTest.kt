package com.example.fakeamazon.features.product

import com.example.fakeamazon.TestDispatcherProvider
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.data.ProductInMemoryDb
import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.fakeInfo
import io.kotest.assertions.withClue
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.beInstanceOf
import io.kotest.matchers.types.instanceOf
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
    val expectedProductInfo = ProductInfo.fakeInfo(VALID_PRODUCT_ID)

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
    fun addToCart_WithValidProductIdMultipleTimes_AddsToCartOnce() = runTest(dispatcher) {
        // Arrange: Init with non-null data
        viewModel.load(VALID_PRODUCT_ID)
        advanceUntilIdle()

        viewModel.addToCart()
        advanceUntilIdle()
        viewModel.addToCart()
        advanceUntilIdle()
        viewModel.addToCart()
        advanceUntilIdle()

        coVerify(exactly = 1) { cartRepository.addToCart(VALID_PRODUCT_ID) }
    }

    @Test
    fun addToCart_WhenLoaded_TransitionsAddToCartState() = runTest(dispatcher) {
        viewModel.load(VALID_PRODUCT_ID)
        advanceUntilIdle()

        val statesEmitted = mutableListOf<ProductUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.testCoroutineScheduler)) {
            viewModel.uiState.collect { statesEmitted.add(it) }
        }
        viewModel.addToCart()
        advanceUntilIdle()

        withClue("Expected three state transitions: " +
                "from ${AddToCartState.Inactive.name} " +
                "-> ${AddToCartState.Adding.name} " +
                "-> ${AddToCartState.Added.name}") {
            statesEmitted.size shouldBe 3
            statesEmitted[0].shouldBeInstanceOf<ProductUiState.Loaded> {
                it.addToCartState shouldBe AddToCartState.Inactive
            }
            statesEmitted[1].shouldBeInstanceOf<ProductUiState.Loaded> {
                it.addToCartState shouldBe AddToCartState.Adding
            }
            statesEmitted[2].shouldBeInstanceOf<ProductUiState.Loaded> {
                it.addToCartState shouldBe AddToCartState.Added
            }
        }
    }

    @Test
    fun onCartAddedViewed_WhenInCartAddingState_TransitionsToInactive() = runTest(dispatcher) {
        viewModel.load(VALID_PRODUCT_ID)
        advanceUntilIdle()
        viewModel.addToCart()
        advanceUntilIdle()

        val statesEmitted = mutableListOf<ProductUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.testCoroutineScheduler)) {
            viewModel.uiState.collect { statesEmitted.add(it) }
        }
        viewModel.onCartAddedViewed()

        withClue("Expected two state transitions: from ${AddToCartState.Added.name} -> ${AddToCartState.Inactive.name}") {
            statesEmitted.size shouldBe 2
            statesEmitted[0].shouldBeInstanceOf<ProductUiState.Loaded> {
                it.addToCartState shouldBe AddToCartState.Added
            }
            statesEmitted[1].shouldBeInstanceOf<ProductUiState.Loaded> {
                it.addToCartState shouldBe AddToCartState.Inactive
            }
        }
    }

}
