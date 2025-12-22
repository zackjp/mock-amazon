package com.example.mockamazon.features.search

import com.example.mockamazon.shared.model.CartItem
import com.example.mockamazon.shared.model.ProductInfo

sealed class SearchResultsScreenState {
    data object Loading : SearchResultsScreenState()
    data object Error : SearchResultsScreenState()
    data class Loaded(
        val cartItems: List<CartItem>,
        val requestedCartCounts: Map<Int, Int>,
        val searchResults: List<ProductInfo>,
    ) : SearchResultsScreenState()
}

sealed class SearchResultsEffect
