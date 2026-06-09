package com.zackjp.mockamazon.shared.ui.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color


data class HeroCarouselCard(
    val title: String,
    val background: Color,
    val carouselItems: List<CarouselItem>
)

data class CategoryCarousel(
    val title: String,
    val carouselCards: List<CarouselCard>,
)

data class CarouselCard(
    val title: String,
    val rec1: CarouselItem,
    val rec2: CarouselItem,
    val rec3: CarouselItem,
    val rec4: CarouselItem,
)

data class CarouselItem(
    val id: Int,
    @param:DrawableRes val imageRes: Int,
    val discount: Float? = null,
)
