package com.example.fakeamazon.features.search

import app.cash.turbine.test
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.data.SearchApiDataSource
import com.example.fakeamazon.shared.model.CartItem
import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.fakeInfo
import com.example.fakeamazon.shared.model.fakeItem
import io.kotest.matchers.maps.shouldNotContainKey
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchResultsViewModelTest {

    private companion object {
        private const val VALID_SEARCH_STRING = "valid search string"
        private const val THROWING_SEARCH_STRING = "throwing search string"
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
        coEvery { cartRepository.removeByProductId(any()) } just runs
        coEvery { cartRepository.getCartItems() } returns emptyList()

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

        coVerify { cartRepository.addToCart(expectedProductId) }
    }

    @Test
    fun addToCart_WithValidProductId_UpdatesCartCountsOptimisticallyForRequestedProductIdsOnly() = runTest {
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
        val productId1 = expectedSearchResults[0].id
        val productId2 = expectedSearchResults[1].id
        val productIdNotInCart = productId1 + productId2
        val cartItem1 = CartItem.fakeItem(productId1)
        val cartItem2 = CartItem.fakeItem(productId2)
        coEvery { cartRepository.getCartItems() } returns listOf(
            cartItem1,
            cartItem2,
        )

        viewModel.screenState.test {
            viewModel.load(VALID_SEARCH_STRING)
            awaitItem() shouldBe SearchResultsScreenState.Loading
            awaitItem() shouldBe instanceOf<SearchResultsScreenState.Loaded>()

            viewModel.addToCart(productIdNotInCart)
            awaitItem().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldBe mapOf(productIdNotInCart to 1)
            }

            viewModel.addToCart(productIdNotInCart)
            awaitItem().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldBe mapOf(productIdNotInCart to 2)
            }

            expectNoEvents()
        }
    }

    @Test
    fun removeFromCart_WithValidProductId_RemovesFromCartFromRepository() = runTest {
        val expectedProductId = expectedSearchResults[0].id
        viewModel.load(VALID_SEARCH_STRING)

        viewModel.removeFromCart(expectedProductId)

        coVerify { cartRepository.removeByProductId(expectedProductId) }
    }

    @Test
    fun removeFromCart_WithValidProductId_RemovesFromCartOptimistically() = runTest {
        val expectedProductId = expectedSearchResults[0].id

        viewModel.screenState.test {
            viewModel.load(VALID_SEARCH_STRING)
            awaitItem() shouldBe SearchResultsScreenState.Loading
            awaitItem() shouldBe instanceOf<SearchResultsScreenState.Loaded>()

            viewModel.addToCart(expectedProductId)
            awaitItem() shouldBe instanceOf<SearchResultsScreenState.Loaded>()

            viewModel.removeFromCart(expectedProductId)

            awaitItem().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldNotContainKey expectedProductId
            }
        }
    }

    @Test
    fun removeFromCart_WithProductNotAlreadyInCart_DoesNotEmitNewState() = runTest {
        val expectedProductId = expectedSearchResults[0].id

        viewModel.screenState.test {
            viewModel.load(VALID_SEARCH_STRING)
            awaitItem() shouldBe SearchResultsScreenState.Loading
            awaitItem() shouldBe instanceOf<SearchResultsScreenState.Loaded>()

            viewModel.removeFromCart(expectedProductId)

            expectNoEvents()
        }
    }

}
