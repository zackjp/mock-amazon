package com.zackjp.mockamazon.features.search

import com.zackjp.mockamazon.shared.data.CartRepository
import com.zackjp.mockamazon.shared.data.SearchApiDataSource
import com.zackjp.mockamazon.shared.model.Cart
import com.zackjp.mockamazon.shared.model.CartItem
import com.zackjp.mockamazon.shared.model.ProductInfo
import com.zackjp.mockamazon.shared.model.toCartItem
import com.zackjp.mockamazon.shared.testutils.model.fakeCart
import com.zackjp.mockamazon.shared.testutils.model.fakeInfo
import com.zackjp.mockamazon.shared.testutils.model.fakeItem
import io.kotest.matchers.maps.shouldContainExactly
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
import org.orbitmvi.orbit.test.TestSettings
import org.orbitmvi.orbit.test.test


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

        private val LOADED_STATE = SearchResultsScreenState.Loaded(
            cartItems = listOf(CART_ITEM),
            requestedCartCounts = emptyMap(),//mapOf(PRODUCT_IN_CART.id to CART_ITEM.quantity)
            searchResults = expectedSearchResults,
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
        viewModel.test(this, settings = TestSettings(autoCheckInitialState = false)) {
            awaitState() shouldBe SearchResultsScreenState.Loading
        }
    }

    @Test
    fun load_WithValidSearchWhenLoading_LoadsSearchResults() = runTest {
        viewModel.test(this, SearchResultsScreenState.Loading) {
            containerHost.load(VALID_SEARCH_STRING)

            awaitState() shouldBe SearchResultsScreenState.Loaded(
                cartItems = listOf(CART_ITEM),
                requestedCartCounts = emptyMap(),
                searchResults = expectedSearchResults,
            )
        }
    }

    @Test
    fun load_WhenAlreadyLoaded_DoesNotReloadSearchResults() = runTest {
        viewModel.test(this, LOADED_STATE) {
            val newSearchResults = listOf(ProductInfo.fakeInfo(987))
            coEvery { searchApiDataSource.getSearchResults(VALID_SEARCH_STRING) } returns newSearchResults

            containerHost.load(VALID_SEARCH_STRING)

            expectNoItems()
        }
    }

    @Test
    fun load_WithSearchException_EmitsError() = runTest {
        viewModel.test(this) {
            containerHost.load(THROWING_SEARCH_STRING)

            awaitState() shouldBe SearchResultsScreenState.Error
        }
    }

    @Test
    fun addToCart_WhenNotLoaded_DoesNotSetRepositoryQuantity() = runTest {
        val expectedProduct = PRODUCT_IN_CART

        viewModel.test(this) {
            containerHost.addToCart(expectedProduct.id)

            coVerify(exactly = 0) { cartRepository.setQuantity(expectedProduct.id, 1) }
        }
    }

    @Test
    fun addToCart_MultipleTimesWithProductNotAlreadyInCart_SetsRepositoryQuantity() = runTest {
        val expectedProduct = PRODUCT_NOT_IN_CART

        viewModel.test(this, LOADED_STATE) {
            containerHost.addToCart(expectedProduct.id)
            containerHost.addToCart(expectedProduct.id)

            awaitState()
            coVerify { cartRepository.setQuantity(expectedProduct.id, 1) }
            awaitState()
            coVerify { cartRepository.setQuantity(expectedProduct.id, 2) }
        }
    }

    @Test
    fun addToCart_MultipleTimesWithProductAlreadyInCart_SetsRepositoryQuantity() = runTest {
        val expectedProduct = PRODUCT_IN_CART
        val cartItem = CART_ITEM

        viewModel.test(this, LOADED_STATE) {
            containerHost.addToCart(expectedProduct.id)
            containerHost.addToCart(expectedProduct.id)

            awaitState()
            coVerify { cartRepository.setQuantity(expectedProduct.id, cartItem.quantity + 1) }
            awaitState()
            coVerify { cartRepository.setQuantity(expectedProduct.id, cartItem.quantity + 2) }
        }
    }

    @Test
    fun addToCart_UpdatesCartCountsOptimisticallyForRequestedProductIdsOnly() =
        runTest {
            val product1 = ProductInfo.fakeInfo(11)
            val product2 = ProductInfo.fakeInfo(23)
            val cartItem1 = CartItem.fakeItem(product1.id)
            val cartItem2 = CartItem.fakeItem(product2.id)
            val cartItem3 = CartItem.fakeItem(product1.id + product2.id)
            val loadedMultiCartState = SearchResultsScreenState.Loaded(
                cartItems = listOf(cartItem1, cartItem2, cartItem3),
                requestedCartCounts = emptyMap(),
                searchResults = listOf(product1, product2)
            )

            viewModel.test(this, loadedMultiCartState) {
                containerHost.addToCart(product1.id)
                awaitState().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                    it.requestedCartCounts shouldBe mapOf(cartItem1.id to cartItem1.quantity + 1)
                }

                containerHost.addToCart(product2.id)
                awaitState().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                    it.requestedCartCounts shouldBe mapOf(
                        cartItem1.id to cartItem1.quantity + 1,
                        cartItem2.id to cartItem2.quantity + 1,
                    )
                }
            }
        }

    @Test
    fun addToCart_WithProductNotAlreadyInCart_UpdatesCartCountOptimistically() = runTest {
        val productNotAlreadyInCart = PRODUCT_NOT_IN_CART

        viewModel.test(this, LOADED_STATE) {
            containerHost.addToCart(productNotAlreadyInCart.id)
            containerHost.addToCart(productNotAlreadyInCart.id)

            awaitState().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldBe mapOf(productNotAlreadyInCart.id to 1)
            }
            awaitState().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldBe mapOf(productNotAlreadyInCart.id to 2)
            }
        }
    }

    @Test
    fun decrementFromCart_MultipleTimesWithProductAlreadyInCart_SetsRepositoryQuantity() = runTest {
        val expectedProduct = PRODUCT_IN_CART
        val cartItem = CART_ITEM

        viewModel.test(this, LOADED_STATE) {
            containerHost.decrementFromCart(expectedProduct.id)
            containerHost.decrementFromCart(expectedProduct.id)

            awaitState()
            awaitState()
            coVerify { cartRepository.setQuantity(expectedProduct.id, cartItem.quantity - 1) }
            coVerify { cartRepository.setQuantity(expectedProduct.id, cartItem.quantity - 2) }
        }
    }

    @Test
    fun decrementFromCart_MultipleTimesWithProductNotAlreadyInCart_SetsRepositoryQuantityToZero() =
        runTest {
            val expectedProduct = PRODUCT_NOT_IN_CART

            viewModel.test(this, LOADED_STATE) {
                containerHost.decrementFromCart(expectedProduct.id)
                containerHost.decrementFromCart(expectedProduct.id)

                awaitState() shouldBe instanceOf<SearchResultsScreenState.Loaded>()
                expectNoItems() // Second decrement = no state change
                coVerify(exactly = 2) { cartRepository.setQuantity(expectedProduct.id, 0) }
            }
        }

    @Test
    fun decrementFromCart_WithProductAlreadyInCart_DecrementsFromCartOptimistically() = runTest {
        val expectedProduct = PRODUCT_IN_CART
        val cartItem = CART_ITEM

        viewModel.test(this, LOADED_STATE) {
            containerHost.decrementFromCart(expectedProduct.id)
            awaitState().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldContainExactly mapOf(expectedProduct.id to cartItem.quantity - 1)
            }

            containerHost.decrementFromCart(expectedProduct.id)
            awaitState().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldContainExactly mapOf(expectedProduct.id to cartItem.quantity - 2)
            }
        }
    }

    @Test
    fun decrementFromCart_WithProductNotAlreadyInCart_SetsCountToZeroOptimistically() = runTest {
        viewModel.test(this, LOADED_STATE) {
            containerHost.decrementFromCart(PRODUCT_NOT_IN_CART.id)

            awaitState().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldBe mapOf(PRODUCT_NOT_IN_CART.id to 0)
            }
        }
    }

    @Test
    fun decrementFromCart_WhenQuantityIsAlreadyZero_ReturnsZeroInsteadOfRemoving() = runTest {
        val expectedProduct = PRODUCT_NOT_IN_CART

        viewModel.test(this, LOADED_STATE) {
            containerHost.addToCart(expectedProduct.id)
            containerHost.decrementFromCart(expectedProduct.id)
            containerHost.decrementFromCart(expectedProduct.id)

            awaitState().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldBe mapOf(expectedProduct.id to 1)
            }
            awaitState().shouldBeInstanceOf<SearchResultsScreenState.Loaded> {
                it.requestedCartCounts shouldBe mapOf(expectedProduct.id to 0)
            }
            expectNoItems() // Last decrement = no state change
        }
    }

}
