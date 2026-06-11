package com.zackjp.mockamazon.core.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color


data class HeroCarouselCard(
    val title: String,
    val background: Color,
    val productTiles: List<ProductTile>
)

data class IntentCarousel(
    val title: String,
    val contextCards: List<ContextCard>,
)

data class ContextCard(
    val title: String,
    val rec1: ProductTile,
    val rec2: ProductTile,
    val rec3: ProductTile,
    val rec4: ProductTile,
)

data class ProductTile(
    val id: Int,
    @param:DrawableRes val imageRes: Int,
    val discount: Float? = null,
)
