package com.example.fakeamazon.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.data.SearchApiDataSource
import com.example.fakeamazon.shared.model.CartItem
import com.example.fakeamazon.shared.runIf
import com.example.fakeamazon.shared.updateIf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val searchApiDataSource: SearchApiDataSource,
) : ViewModel() {

    private val _screenState =
        MutableStateFlow<SearchResultsScreenState>(SearchResultsScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private var _cartItems = listOf<CartItem>()

    fun load(searchString: String) {
        viewModelScope.launch {
            if (screenState.value is SearchResultsScreenState.Loaded) {
                return@launch
            }

            try {
                val searchResults = searchApiDataSource.getSearchResults(searchString)
                _cartItems = cartRepository.getCartItems()
                _screenState.value = SearchResultsScreenState.Loaded(
                    requestedCartCounts = emptyMap(),
                    searchResults = searchResults,
                )
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _screenState.value = SearchResultsScreenState.Error
            }
        }
    }

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            optimisticCartCountUpdate(productId, 1)

            _screenState.value.runIf<SearchResultsScreenState.Loaded> {
                val currentLocalQuantity = requestedCartCounts[productId]
                cartRepository.setQuantity(productId, currentLocalQuantity ?: 1)
            }
        }
    }

    fun decrementFromCart(productId: Int) {
        viewModelScope.launch {
            optimisticCartCountUpdate(productId, -1)

            _screenState.value.runIf<SearchResultsScreenState.Loaded> {
                val currentLocalQuantity = requestedCartCounts[productId]
                cartRepository.setQuantity(productId, currentLocalQuantity ?: 0)
            }
        }
    }

    private fun optimisticCartCountUpdate(productId: Int, quantityChange: Int) {
        _screenState.updateIf<SearchResultsScreenState.Loaded> { current ->
            current.copy(
                requestedCartCounts = current.requestedCartCounts
                    .toMutableMap()
                    .apply {
                        compute(productId) { _, value ->
                            val cartCount = when (value) {
                                null -> {
                                    val cartItem = _cartItems.find { it.id == productId }
                                    val baseCartCount = cartItem?.quantity
                                    baseCartCount ?: 0
                                }

                                else -> value
                            }
                            return@compute maxOf(0, cartCount + quantityChange)
                        }
                    }
            )
        }
    }

}
