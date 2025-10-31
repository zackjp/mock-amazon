package com.example.fakeamazon.features.product

import com.example.fakeamazon.shared.model.ProductInfo

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Loaded(
        val productInfo: ProductInfo,
        val addToCartState: AddToCartState = AddToCartState.Inactive,
    ) : ProductUiState()
    object Error : ProductUiState()
}

enum class AddToCartState {
    Added,
    Adding,
    Inactive,
}
