package com.zackjp.mockamazon.core.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color


data class HeroCarouselCard(
    val heroId: String,
    val preTitle: String? = null,
    val title: String,
    val background: Color,
    val backgroundImageId: Int?,
    val productGridHeightFraction: Float,
    val productTiles: List<ProductTile>
)

data class IntentCarousel(
    val intentId: String,
    val title: String,
    val contextCards: List<ContextCard>,
)

data class ContextCard(
    val contextId: String,
    val title: String,
    val rec1: ProductTile,
    val rec2: ProductTile,
    val rec3: ProductTile,
    val rec4: ProductTile,
)

data class ProductTile(
    val productId: Int,
    @param:DrawableRes val imageRes: Int,
    val discount: Float? = null,
)
