package com.zackjp.mockamazon.shared.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color


data class HeroCarouselCardResponse(
    val title: String,
    val background: Color,
    val carouselItemResponses: List<CarouselItemResponse>
) {
    companion object // for utility functions
}

data class CategoryCarouselResponse(
    val title: String,
    val carouselCardResponse: List<CarouselCardResponse>,
) {
    companion object // for utility functions
}

data class CarouselCardResponse(
    val title: String,
    val rec1: CarouselItemResponse,
    val rec2: CarouselItemResponse,
    val rec3: CarouselItemResponse,
    val rec4: CarouselItemResponse,
) {
    companion object // for utility functions
}

data class CarouselItemResponse(
    val id: Int,
    @param:DrawableRes val imageRes: Int,
    val discount: Float? = null,
) {
    companion object // for utility functions
}
