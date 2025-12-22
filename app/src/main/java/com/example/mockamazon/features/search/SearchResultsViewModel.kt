package com.example.mockamazon.features.search

import androidx.lifecycle.ViewModel
import com.example.mockamazon.data.CartRepository
import com.example.mockamazon.data.SearchApiDataSource
import com.example.mockamazon.shared.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.Syntax
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val searchApiDataSource: SearchApiDataSource,
) : ContainerHost<SearchResultsScreenState, SearchResultsEffect>, ViewModel() {

    override val container = container<SearchResultsScreenState, SearchResultsEffect>(
        SearchResultsScreenState.Loading
    )

    fun load(searchString: String) = intent {
        if (state is SearchResultsScreenState.Loaded) {
            return@intent
        }

        try {
            val searchResults = searchApiDataSource.getSearchResults(searchString)
            val cartItems = cartRepository.getCart().cartItems

            reduce {
                SearchResultsScreenState.Loaded(
                    cartItems = cartItems,
                    requestedCartCounts = emptyMap(),
                    searchResults = searchResults,
                )
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            reduce { SearchResultsScreenState.Error }
        }
    }

    fun addToCart(productId: Int) = intent {
        val intentState = state
        if (intentState !is SearchResultsScreenState.Loaded) {
            return@intent
        }

        val newQuantity = determineCartCount(intentState, productId, 1)
        updateCartCountOptimistically(productId, newQuantity)

        cartRepository.setQuantity(productId, newQuantity)
    }

    fun decrementFromCart(productId: Int) = intent {
        val intentState = state
        if (intentState !is SearchResultsScreenState.Loaded) {
            return@intent
        }

        val newQuantity = determineCartCount(intentState, productId, -1)
        updateCartCountOptimistically(productId, newQuantity)

        cartRepository.setQuantity(productId, newQuantity)
    }

    private fun determineCartCount(
        loadedState: SearchResultsScreenState.Loaded,
        productId: Int,
        quantityChange: Int,
    ): Int {
        val storedValue = loadedState.requestedCartCounts[productId]
        val baseCount = when (storedValue) {
            null -> loadedState.cartItems.find { it.id == productId }?.quantity ?: 0
            else -> storedValue
        }
        return maxOf(0, baseCount + quantityChange)
    }

    private suspend fun Syntax<SearchResultsScreenState, SearchResultsEffect>.updateCartCountOptimistically(
        productId: Int,
        newQuantity: Int,
    ) {
        reduce {
            val preReduceState = state
            if (preReduceState !is SearchResultsScreenState.Loaded) {
                return@reduce preReduceState
            }

            preReduceState.copy(
                requestedCartCounts = preReduceState.requestedCartCounts
                    .toMutableMap()
                    .apply {
                        put(productId, newQuantity)
                    }
            )
        }
    }

}
