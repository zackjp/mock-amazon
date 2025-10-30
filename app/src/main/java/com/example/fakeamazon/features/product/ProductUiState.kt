package com.example.fakeamazon.features.product

import com.example.fakeamazon.shared.model.ProductInfo

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Loaded(val productInfo: ProductInfo) : ProductUiState()
    object Error : ProductUiState()
}
