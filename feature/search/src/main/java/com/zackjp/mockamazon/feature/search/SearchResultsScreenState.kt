package com.zackjp.mockamazon.feature.search

import com.zackjp.mockamazon.core.model.ProductInfo
import com.zackjp.mockamazon.core.model.CartItem

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
