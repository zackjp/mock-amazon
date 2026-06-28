package com.zackjp.mockamazon.shared.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color


data class HeroCarouselCardResponse(
    val heroId: String,
    val title: String,
    val background: Color,
    val backgroundImageId: Int? = null,
    val productGridHeightFraction: Float,
    val productTileResponse: List<ProductTileResponse>
) {
    companion object // for utility functions
}

data class IntentCarouselResponse(
    val intentId: String,
    val title: String,
    val contextCardResponse: List<ContextCardResponse>,
) {
    companion object // for utility functions
}

data class ContextCardResponse(
    val contextId: String,
    val title: String,
    val rec1: ProductTileResponse,
    val rec2: ProductTileResponse,
    val rec3: ProductTileResponse,
    val rec4: ProductTileResponse,
) {
    companion object // for utility functions
}

data class ProductTileResponse(
    val productId: Int,
    @param:DrawableRes val imageRes: Int,
    val discount: Float? = null,
) {
    companion object // for utility functions
}
