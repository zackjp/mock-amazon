package com.example.fakeamazon.features.search

import app.cash.turbine.test
import com.example.fakeamazon.SetMainCoroutineDispatcher
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.data.SearchApiDataSource
import com.example.fakeamazon.shared.model.CartItem
import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.fakeInfo
import com.example.fakeamazon.shared.model.fakeItem
import com.example.fakeamazon.shared.model.toCartItem
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@OptIn(ExperimentalCoroutinesApi::class) // advanceUntilIdle()
@ExtendWith(SetMainCoroutineDispatcher::class)
class SearchResultsViewModelTest {

    private companion object {
        private const val VALID_SEARCH_STRING = "valid search string"
        private const val THROWING_SEARCH_STRING = "throwing search string"
        private val PRODUCT_IN_CART = ProductInfo.fakeInfo(123)
        private val PRODUCT_NOT_IN_CART = ProductInfo.fakeInfo(456)
        private val CART_ITEM = PRODUCT_IN_CART.toCartItem(quantity = 7)

        private val expectedSearchResults = listOf(
            ProductInfo.fakeInfo(123),
            ProductInfo.fakeInfo(456),
        )
    }

    private val cartRepository = mockk<CartRepository>()

    private val searchApiDataSource = mockk<SearchApiDataSource>()

    private lateinit var viewModel: SearchResultsViewModel

    @BeforeEach
    fun setUp() {
        coEvery { searchApiDataSource.getSearchResults(VALID_SEARCH_STRING) } returns expectedSearchResults
        coEvery { searchApiDataSource.getSearchResults(THROWING_SEARCH_STRING) } throws Exception("search error exception")
        coEvery { cartRepository.addToCart(any()) } just runs
        coEvery { cartRepository.decrementByProductId(any()) } just runs
        coEvery { cartRepository.getCartItems() } returns listOf(CART_ITEM)

        viewModel = SearchResultsViewModel(
            cartRepository = cartRepository,
            searchApiDataSource = searchApiDataSource,
        )
    }

    @Test
    fun init_StartsAsLoading() = runTest {
        viewModel.screenState.test {
            awaitItem() shouldBe SearchResultsScreenState.Loading
        }
    }

    @Test
    fun load_WithValidSearch_LoadsSearchResults() = runTest {
        viewModel.screenState.test {
            viewModel.load(VALID_SEARCH_STRING)

            awaitItem() shouldBe SearchResultsScreenState.Loading
            awaitItem() shouldBe SearchResultsScreenState.Loaded(
                requestedCartCounts = emptyMap(),
                searchResults = expectedSearchResults,
            )
        }
    }

    @Test
    fun load_WithSearchException_EmitsError() = runTest {
        viewModel.screenState.test {
            viewModel.load(THROWING_SEARCH_STRING)

            awaitItem() shouldBe SearchResultsScreenState.Loading
            awaitItem() shouldBe SearchResultsScreenState.Error
        }
    }

    @Test
    fun addToCart_WithValidProductId_AddsToCart() = runTest {
        val expectedProductId = expectedSearchResults[0].id

        viewModel.load(VALID_SEARCH_STRING)
        viewModel.addToCart(expectedProductId)
        advanceUntilIdle()

        coVerify { cartRepository.addToCart(expectedProductId) }
    }

    @Test
    fun addToCart_WithValidProductId_UpdatesCartCountsOptimisticallyForRequestedProductIdsOnly() =
        runTest {
            val productId1 = expectedSearchResults[0].id
            val productId2 = expectedSearchResults[1].id
            val cartItem1 = CartItem.fakeItem(productId1)
            val cartItem2 = CartItem.fakeItem(productId2)
            val cartItem3 = CartItem.fakeItem(productId1 + productId2)
            coEvery { cartRepository.getCartItems() } returns listOf(
                cartItem1,
                cartItem2,
                cartItem3,
            )

            viewModel.screenState.test {
                viewModel.load(VALID_SEARCH_STRING)
                awaitItem() shouldBe SearchResultsScreenState.Loading
                awaitItem() shouldBe instanceOf<SearchResultsScreenState.Loaded>()

                viewModel.addToCart(productId1)
                awaitItem().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                    it.requestedCartCounts shouldBe mapOf(cartItem1.id to cartItem1.quantity + 1)
                }

                viewModel.addToCart(productId2)
                awaitItem().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                    it.requestedCartCounts shouldBe mapOf(
                        cartItem1.id to cartItem1.quantity + 1,
                        cartItem2.id to cartItem2.quantity + 1,
                    )
                }

                expectNoEvents()
            }
        }

    @Test
    fun addToCart_WithProductNotAlreadyInCart_UpdatesCartCountOptimistically() = runTest {
        val productNotAlreadyInCart = PRODUCT_NOT_IN_CART

        viewModel.load(VALID_SEARCH_STRING)
        viewModel.addToCart(productNotAlreadyInCart.id)
        viewModel.addToCart(productNotAlreadyInCart.id)
        advanceUntilIdle()

        viewModel.screenState.value.shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
            it.requestedCartCounts shouldBe mapOf(productNotAlreadyInCart.id to 2)
        }
    }

    @Test
    fun decrementFromCart_WithValidProductId_DecrementsFromCartFromRepository() = runTest {
        val expectedProduct = PRODUCT_IN_CART

        viewModel.load(VALID_SEARCH_STRING)
        viewModel.decrementFromCart(expectedProduct.id)
        advanceUntilIdle()

        coVerify { cartRepository.decrementByProductId(expectedProduct.id) }
    }

    @Test
    fun decrementFromCart_WithValidProductId_DecrementsFromCartOptimistically() = runTest {
        val expectedProduct = PRODUCT_IN_CART
        val cartItem = CART_ITEM

        viewModel.load(VALID_SEARCH_STRING)
        viewModel.decrementFromCart(expectedProduct.id)
        advanceUntilIdle()

        viewModel.screenState.value.shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
            it.requestedCartCounts shouldContainExactly mapOf(expectedProduct.id to cartItem.quantity - 1)
        }
    }

    @Test
    fun decrementFromCart_WhenQuantityIsAlreadyZero_ReturnsEmptyCartCounts() = runTest {
        val expectedProduct = PRODUCT_NOT_IN_CART

        viewModel.load(VALID_SEARCH_STRING)
        viewModel.addToCart(expectedProduct.id)
        viewModel.decrementFromCart(expectedProduct.id)
        viewModel.decrementFromCart(expectedProduct.id)
        advanceUntilIdle()

        viewModel.screenState.value.shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
            it.requestedCartCounts shouldBe emptyMap()
        }
    }

    @Test
    fun decrementFromCart_WithProductNotAlreadyInCart_DoesNotEmitNewState() = runTest {
        val expectedProduct = PRODUCT_NOT_IN_CART

        viewModel.screenState.test {
            viewModel.load(VALID_SEARCH_STRING)
            awaitItem() shouldBe SearchResultsScreenState.Loading
            awaitItem() shouldBe instanceOf<SearchResultsScreenState.Loaded>()

            viewModel.decrementFromCart(expectedProduct.id)

            expectNoEvents()
        }
    }

}
