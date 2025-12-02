package com.example.mockamazon.features.search

import app.cash.turbine.test
import com.example.mockamazon.SetMainCoroutineDispatcher
import com.example.mockamazon.data.CartRepository
import com.example.mockamazon.data.SearchApiDataSource
import com.example.mockamazon.shared.model.Cart
import com.example.mockamazon.shared.model.CartItem
import com.example.mockamazon.shared.model.ProductInfo
import com.example.mockamazon.shared.model.fakeCart
import com.example.mockamazon.shared.model.fakeInfo
import com.example.mockamazon.shared.model.fakeItem
import com.example.mockamazon.shared.model.toCartItem
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
        coEvery { cartRepository.getCart() } returns Cart.fakeCart(listOf(CART_ITEM))
        coEvery { cartRepository.setQuantity(any(), any()) } just runs

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
    fun load_WhenAlreadyLoaded_DoesNotReloadSearchResults() = runTest {
        viewModel.screenState.test {
            val searchResult1 = listOf(ProductInfo.fakeInfo(987))
            val searchResult2 = listOf(ProductInfo.fakeInfo(654))
            val expectedState = SearchResultsScreenState.Loaded(
                requestedCartCounts = emptyMap(),
                searchResults = searchResult1,
            )

            coEvery { searchApiDataSource.getSearchResults(VALID_SEARCH_STRING) } returns searchResult1
            viewModel.load(VALID_SEARCH_STRING)
            awaitItem() shouldBe SearchResultsScreenState.Loading
            awaitItem() shouldBe expectedState

            coEvery { searchApiDataSource.getSearchResults(VALID_SEARCH_STRING) } returns searchResult2
            viewModel.load(VALID_SEARCH_STRING)
            advanceUntilIdle()

            expectNoEvents()
            viewModel.screenState.value shouldBe expectedState
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
    fun addToCart_WhenNotLoaded_DoesNotSetRepositoryQuantity() = runTest {
        val expectedProduct = PRODUCT_IN_CART

        viewModel.addToCart(expectedProduct.id)
        advanceUntilIdle()

        coVerify(exactly = 0) { cartRepository.setQuantity(expectedProduct.id, 1) }
    }

    @Test
    fun addToCart_MultipleTimesWithProductNotAlreadyInCart_SetsRepositoryQuantity() = runTest {
        val expectedProduct = PRODUCT_NOT_IN_CART

        viewModel.load(VALID_SEARCH_STRING)
        viewModel.addToCart(expectedProduct.id)
        viewModel.addToCart(expectedProduct.id)
        advanceUntilIdle()

        coVerify { cartRepository.setQuantity(expectedProduct.id, 1) }
        coVerify { cartRepository.setQuantity(expectedProduct.id, 2) }
    }

    @Test
    fun addToCart_MultipleTimesWithProductAlreadyInCart_SetsRepositoryQuantity() = runTest {
        val expectedProduct = PRODUCT_IN_CART
        val cartItem = CART_ITEM

        viewModel.load(VALID_SEARCH_STRING)
        viewModel.addToCart(expectedProduct.id)
        viewModel.addToCart(expectedProduct.id)
        advanceUntilIdle()

        coVerify { cartRepository.setQuantity(expectedProduct.id, cartItem.quantity + 1) }
        coVerify { cartRepository.setQuantity(expectedProduct.id, cartItem.quantity + 2) }
    }

    @Test
    fun addToCart_UpdatesCartCountsOptimisticallyForRequestedProductIdsOnly() =
        runTest {
            val product1 = ProductInfo.fakeInfo(11)
            val product2 = ProductInfo.fakeInfo(23)
            val cartItem1 = CartItem.fakeItem(product1.id)
            val cartItem2 = CartItem.fakeItem(product2.id)
            val cartItem3 = CartItem.fakeItem(product1.id + product2.id)
            coEvery { cartRepository.getCart() } returns Cart.fakeCart(
                listOf(
                    cartItem1,
                    cartItem2,
                    cartItem3,
                )
            )

            viewModel.screenState.test {
                viewModel.load(VALID_SEARCH_STRING)
                awaitItem() shouldBe SearchResultsScreenState.Loading
                awaitItem() shouldBe instanceOf<SearchResultsScreenState.Loaded>()

                viewModel.addToCart(product1.id)
                awaitItem().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                    it.requestedCartCounts shouldBe mapOf(cartItem1.id to cartItem1.quantity + 1)
                }

                viewModel.addToCart(product2.id)
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
    fun decrementFromCart_MultipleTimesWithProductAlreadyInCart_SetsRepositoryQuantity() = runTest {
        val expectedProduct = PRODUCT_IN_CART
        val cartItem = CART_ITEM

        viewModel.load(VALID_SEARCH_STRING)
        viewModel.decrementFromCart(expectedProduct.id)
        viewModel.decrementFromCart(expectedProduct.id)
        advanceUntilIdle()

        coVerify { cartRepository.setQuantity(expectedProduct.id, cartItem.quantity - 1) }
        coVerify { cartRepository.setQuantity(expectedProduct.id, cartItem.quantity - 2) }
    }

    @Test
    fun decrementFromCart_MultipleTimesWithProductNotAlreadyInCart_SetsRepositoryQuantityToZero() =
        runTest {
            val expectedProduct = PRODUCT_NOT_IN_CART

            viewModel.load(VALID_SEARCH_STRING)
            viewModel.decrementFromCart(expectedProduct.id)
            viewModel.decrementFromCart(expectedProduct.id)
            advanceUntilIdle()

            coVerify(exactly = 2) { cartRepository.setQuantity(expectedProduct.id, 0) }
        }

    @Test
    fun decrementFromCart_WithProductAlreadyInCart_DecrementsFromCartOptimistically() = runTest {
        val expectedProduct = PRODUCT_IN_CART
        val cartItem = CART_ITEM

        viewModel.load(VALID_SEARCH_STRING)
        advanceUntilIdle()

        viewModel.screenState.test {
            awaitItem().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldContainExactly emptyMap()
            }

            viewModel.decrementFromCart(expectedProduct.id)
            awaitItem().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldContainExactly mapOf(expectedProduct.id to cartItem.quantity - 1)
            }

            viewModel.decrementFromCart(expectedProduct.id)
            awaitItem().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldContainExactly mapOf(expectedProduct.id to cartItem.quantity - 2)
            }
        }
    }

    @Test
    fun decrementFromCart_WithProductNotAlreadyInCart_SetsCountToZeroOptimistically() = runTest {
        val expectedProduct = PRODUCT_NOT_IN_CART

        viewModel.screenState.test {
            viewModel.load(VALID_SEARCH_STRING)
            awaitItem() shouldBe SearchResultsScreenState.Loading
            awaitItem() shouldBe instanceOf<SearchResultsScreenState.Loaded>()

            viewModel.decrementFromCart(expectedProduct.id)
            advanceUntilIdle()

            awaitItem().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldBe mapOf(expectedProduct.id to 0)
            }
        }
    }

    @Test
    fun decrementFromCart_WhenQuantityIsAlreadyZero_ReturnsZeroInsteadOfRemoving() = runTest {
        val expectedProduct = PRODUCT_NOT_IN_CART

        viewModel.load(VALID_SEARCH_STRING)
        viewModel.addToCart(expectedProduct.id)
        viewModel.decrementFromCart(expectedProduct.id)
        viewModel.decrementFromCart(expectedProduct.id)
        advanceUntilIdle()

        viewModel.screenState.value.shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
            it.requestedCartCounts shouldBe mapOf(expectedProduct.id to 0)
        }
    }

}
