package com.example.fakeamazon.features.search

import app.cash.turbine.test
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.data.SearchApiDataSource
import com.example.fakeamazon.shared.model.CartItem
import com.example.fakeamazon.shared.model.ProductInfo
import com.example.fakeamazon.shared.model.fakeInfo
import com.example.fakeamazon.shared.model.fakeItem
import io.kotest.matchers.maps.shouldHaveSize
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
        expectedSearchResults.forEach {
            coEvery { cartRepository.addToCart(it.id) } just runs
        }
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
    fun addToCart_WithValidProductId_EmitsNewCartCountForRequestedProductIdsOnly() = runTest {
        val expectedProductId1 = expectedSearchResults[0].id
        val expectedProductId2 = expectedSearchResults[1].id
        val expectedCartItem1 = CartItem.fakeItem(expectedProductId1)
        val expectedCartItem2 = CartItem.fakeItem(expectedProductId2)
        val unexpectedCartItem3 = CartItem.fakeItem(expectedProductId1 + expectedProductId2)
        coEvery { cartRepository.getCartItems() } returns listOf(
            expectedCartItem1,
            expectedCartItem2,
            unexpectedCartItem3,
        )
        viewModel.load(VALID_SEARCH_STRING)

        viewModel.screenState.test {
            awaitItem() shouldBe instanceOf<SearchResultsScreenState.Loaded>()

            viewModel.addToCart(expectedProductId1)
            awaitItem().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldHaveSize 1
                it.requestedCartCounts[expectedCartItem1.id] shouldBe expectedCartItem1.quantity
            }

            viewModel.addToCart(expectedProductId2)
            awaitItem().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldHaveSize 2
                it.requestedCartCounts[expectedCartItem1.id] shouldBe expectedCartItem1.quantity
                it.requestedCartCounts[expectedCartItem2.id] shouldBe expectedCartItem2.quantity
            }
        }
    }

}
