package com.example.fakeamazon.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeamazon.data.CartRepository
import com.example.fakeamazon.data.SearchApiDataSource
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

    fun load(searchString: String) {
        viewModelScope.launch {
            try {
                val searchResults = searchApiDataSource.getSearchResults(searchString)
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
            cartRepository.addToCart(productId)

            val cartItems = cartRepository.getCartItems()

            _screenState.updateIf<SearchResultsScreenState.Loaded> { original ->
                val updatedRequestedCartCounts = original.requestedCartCounts.toMutableMap().apply {
                    // add the product id as a requested item to our local map, then update
                    // all previous requested items
                    put(productId, 0)
                    cartItems.forEach { cartItem ->
                        if (containsKey(cartItem.id)) {
                            put(cartItem.id, cartItem.quantity)
                        }
                    }
                }

                original.copy(
                    requestedCartCounts = updatedRequestedCartCounts,
                )
            }
        }
    }

}
