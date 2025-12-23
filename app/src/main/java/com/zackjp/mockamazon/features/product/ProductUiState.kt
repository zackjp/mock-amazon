package com.zackjp.mockamazon.features.product

import com.zackjp.mockamazon.shared.model.ProductInfo

sealed class ProductUiState {
    data object Loading : ProductUiState()
    data class Loaded(
        val productInfo: ProductInfo,
        val addToCartState: AddToCartState = AddToCartState.Inactive,
        val similarProducts: List<ProductInfo>,
    ) : ProductUiState()
    data object Error : ProductUiState()
}

enum class AddToCartState {
    Added,
    Adding,
    Inactive,
}
