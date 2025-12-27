package com.zackjp.mockamazon.features.product

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
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val expectedProductInfo = ProductInfo.fakeInfo(VALID_PRODUCT_ID)
    private val expectedSimilarProducts = listOf(
        ProductInfo.fakeInfo(VALID_PRODUCT_ID + 1),
        ProductInfo.fakeInfo(VALID_PRODUCT_ID + 2),
    )

    private lateinit var viewModel: ProductViewModel

    @BeforeEach
    fun setUp() {
        val productRepository = mockk<ProductRepository>()
        coEvery { productRepository.getProductById(VALID_PRODUCT_ID) } returns expectedProductInfo
        coEvery { productRepository.getProductById(INVALID_PRODUCT_ID) } returns null
        coEvery { productRepository.getSimilarProducts(VALID_PRODUCT_ID) } returns expectedSimilarProducts
        coEvery { productRepository.getSimilarProducts(INVALID_PRODUCT_ID) } returns emptyList()
        coEvery { cartRepository.addToCart(any()) } just Runs

        viewModel = ProductViewModel(
            cartRepository = cartRepository,
            productRepository = productRepository,
        )
    }

    @Test
    fun init_StartsAsLoading() = runTest {
        advanceUntilIdle()
        viewModel.uiState.value shouldBe ProductUiState.Loading
    }

    @Test
    fun load_WithValidProductId_LoadsProductInfoAndSimilarProducts() = runTest {
        viewModel.load(VALID_PRODUCT_ID)
        advanceUntilIdle()

        viewModel.uiState.value shouldBe ProductUiState.Loaded(
            productInfo = expectedProductInfo,
            similarProducts = expectedSimilarProducts,
        )
    }

    @Test
    fun load_WithInvalidProductId_EmitsError() = runTest {
        viewModel.load(INVALID_PRODUCT_ID)
        advanceUntilIdle()

        viewModel.uiState.value shouldBe ProductUiState.Error
    }

    @Test
    fun addToCart_WhileAddedStateNotYetAcknowledged_AddsToCartOnce() = runTest {
        viewModel.load(VALID_PRODUCT_ID)
        advanceUntilIdle()

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
        viewModel.uiState.test {
            awaitItem() shouldBe ProductUiState.Loading

            viewModel.load(VALID_PRODUCT_ID)
            awaitItem().shouldBeInstanceOf<ProductUiState.Loaded> {
                it.addToCartState shouldBe AddToCartState.Inactive
            }

            viewModel.addToCart()
            awaitItem().shouldBeInstanceOf<ProductUiState.Loaded> {
                it.addToCartState shouldBe AddToCartState.Adding
            }
            awaitItem().shouldBeInstanceOf<ProductUiState.Loaded> {
                it.addToCartState shouldBe AddToCartState.Added
            }
        }
    }

    @Test
    fun onCartAddedViewed_WhenInCartAddingState_TransitionsToInactive() = runTest {
        viewModel.load(VALID_PRODUCT_ID)
        advanceUntilIdle()
        viewModel.addToCart()
        advanceUntilIdle()

        viewModel.uiState.test {
            viewModel.onCartAddedViewed()

            awaitItem().shouldBeInstanceOf<ProductUiState.Loaded> {
                it.addToCartState shouldBe AddToCartState.Added
            }
            awaitItem().shouldBeInstanceOf<ProductUiState.Loaded> {
                it.addToCartState shouldBe AddToCartState.Inactive
            }
        }
    }
}
