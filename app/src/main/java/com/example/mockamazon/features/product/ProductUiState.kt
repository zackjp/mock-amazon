package com.example.mockamazon.features.product

import com.example.mockamazon.shared.model.ProductInfo

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Loaded(
        val productInfo: ProductInfo,
        val addToCartState: AddToCartState = AddToCartState.Inactive,
        val similarProducts: List<ProductInfo>,
    ) : ProductUiState()
    object Error : ProductUiState()
}

enum class AddToCartState {
    Added,
    Adding,
    Inactive,
}
