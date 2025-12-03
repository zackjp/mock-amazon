package com.example.mockamazon.features.search

import com.example.mockamazon.shared.model.ProductInfo

sealed class SearchResultsScreenState {
    data object Loading : SearchResultsScreenState()
    data object Error : SearchResultsScreenState()
    data class Loaded(
        val requestedCartCounts: Map<Int, Int>,
        val searchResults: List<ProductInfo>,
    ) : SearchResultsScreenState()
}
