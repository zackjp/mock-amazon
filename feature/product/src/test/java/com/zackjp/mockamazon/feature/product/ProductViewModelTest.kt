package com.zackjp.mockamazon.feature.product

import app.cash.turbine.test
import com.zackjp.mockamazon.shared.data.CartRepository
import com.zackjp.mockamazon.shared.data.ProductRepository
import com.zackjp.mockamazon.shared.model.ProductInfo
import com.zackjp.mockamazon.shared.testutils.SetMainCoroutineDispatcher
import com.zackjp.mockamazon.shared.testutils.model.fakeInfo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class) // advanceUntilIdle()
@ExtendWith(SetMainCoroutineDispatcher::class)
class ProductViewModelTest {

    private companion object {
        private const val VALID_PRODUCT_ID: Int = 123
        private const val INVALID_PRODUCT_ID: Int = -321
    }

    private val cartRepository = mockk<CartRepository>()
    private val productRepository = mockk<ProductRepository>()
    private val expectedProductInfo = ProductInfo.Companion.fakeInfo(VALID_PRODUCT_ID)
    private val expectedSimilarProducts = listOf(
        ProductInfo.Companion.fakeInfo(VALID_PRODUCT_ID + 1),
        ProductInfo.Companion.fakeInfo(VALID_PRODUCT_ID + 2),
    )

    private lateinit var viewModel: ProductViewModel

    @BeforeEach
    fun setUp() {
        coEvery { productRepository.getProductById(VALID_PRODUCT_ID) } returns expectedProductInfo
        coEvery { productRepository.getProductById(INVALID_PRODUCT_ID) } returns null
        coEvery { productRepository.getSimilarProducts(VALID_PRODUCT_ID) } returns expectedSimilarProducts
        coEvery { productRepository.getSimilarProducts(INVALID_PRODUCT_ID) } returns emptyList()
        coEvery { cartRepository.addToCart(any()) } just Runs

        viewModel = ProductViewModel(
            cartRepository = cartRepository,
            productRepository = productRepository,
            productId = VALID_PRODUCT_ID,
        )
    }

    @Test
    fun init_StartsAsLoading() = runTest {
        advanceUntilIdle()
        viewModel.uiState.value shouldBe ProductUiState.Loading
    }

    @Test
    fun load_WithValidProductId_LoadsProductInfoAndSimilarProducts() = runTest {
        startUiState(viewModel)

        viewModel.uiState.value shouldBe ProductUiState.Loaded(
            productInfo = expectedProductInfo,
            similarProducts = expectedSimilarProducts,
        )
    }

    @Test
    fun load_WithInvalidProductId_EmitsError() = runTest {
        val viewModelWithInvalidId = ProductViewModel(
            cartRepository = cartRepository,
            productRepository = productRepository,
            productId = INVALID_PRODUCT_ID,
        )
        startUiState(viewModelWithInvalidId)

        viewModelWithInvalidId.uiState.value shouldBe ProductUiState.Error
    }

    @Test
    fun addToCart_WhileAddedStateNotYetAcknowledged_AddsToCartOnce() = runTest {
        startUiState(viewModel)

        viewModel.addToCart()
        viewModel.addToCart()
        viewModel.addToCart()
        advanceUntilIdle()

        coVerify(exactly = 1) { cartRepository.addToCart(VALID_PRODUCT_ID) }
        viewModel.uiState.value.shouldBeInstanceOf<ProductUiState.Loaded> {
            it.addToCartState shouldBe AddToCartState.Added
        }
    }

    @Test
    fun addToCart_WhenLoaded_TransitionsAddToCartState() = runTest {
        val cartJob = CompletableDeferred<Unit>()
        coEvery { cartRepository.addToCart(VALID_PRODUCT_ID) } coAnswers { cartJob.await() }
        startUiState(viewModel)
        viewModel.addToCart()
        advanceUntilIdle()

        viewModel.uiState.test {
            awaitItem().shouldBeInstanceOf<ProductUiState.Loaded> {
                it.addToCartState shouldBe AddToCartState.Adding
            }

            cartJob.complete(Unit)

            awaitItem().shouldBeInstanceOf<ProductUiState.Loaded> {
                it.addToCartState shouldBe AddToCartState.Added
            }
        }
    }

    @Test
    fun onCartAddedViewed_WhenInCartAddingState_TransitionsToInactive() = runTest {
        startUiState(viewModel)
        viewModel.addToCart()
        advanceUntilIdle()
        viewModel.onCartAddedViewed()
        advanceUntilIdle()

        viewModel.uiState.value.shouldBeInstanceOf<ProductUiState.Loaded> {
            it.addToCartState shouldBe AddToCartState.Inactive
        }
    }

    private fun TestScope.startUiState(viewModel: ProductViewModel) {
        viewModel.uiState.launchIn(backgroundScope)
        advanceUntilIdle()
    }
}