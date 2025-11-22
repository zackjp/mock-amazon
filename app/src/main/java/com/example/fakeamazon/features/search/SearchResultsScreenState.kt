package com.example.fakeamazon.features.search

import com.example.fakeamazon.shared.model.ProductInfo

sealed class SearchResultsScreenState {
    object Loading : SearchResultsScreenState()
    object Error : SearchResultsScreenState()
    data class Loaded(
        val requestedCartCounts: Map<Int, Int>,
        val searchResults: List<ProductInfo>,
    ) : SearchResultsScreenState()
}
