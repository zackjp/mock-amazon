package com.zackjp.mockamazon.data.model

import com.zackjp.mockamazon.model.CarouselCard
import com.zackjp.mockamazon.model.CarouselItem
import com.zackjp.mockamazon.model.CategoryCarousel
import com.zackjp.mockamazon.model.HeroCarouselCard
import com.zackjp.mockamazon.shared.model.CarouselCardResponse
import com.zackjp.mockamazon.shared.model.CarouselItemResponse
import com.zackjp.mockamazon.shared.model.CategoryCarouselResponse
import com.zackjp.mockamazon.shared.model.HeroCarouselCardResponse


fun HeroCarouselCardResponse.toUiModel(): HeroCarouselCard =
    HeroCarouselCard(
        title = this.title,
        background = this.background,
        carouselItems = this.carouselItemResponses.map { it.toUiModel() },
    )

fun CategoryCarouselResponse.toUiModel(): CategoryCarousel =
    CategoryCarousel(
        title = this.title,
        carouselCards = this.carouselCardResponse.map { it.toUiModel() },
    )

fun CarouselCardResponse.toUiModel(): CarouselCard =
    CarouselCard(
        title = this.title,
        rec1 = this.rec1.toUiModel(),
        rec2 = this.rec2.toUiModel(),
        rec3 = this.rec3.toUiModel(),
        rec4 = this.rec4.toUiModel(),
    )

fun CarouselItemResponse.toUiModel(): CarouselItem =
    CarouselItem(
        id = this.id,
        imageRes = this.imageRes,
        discount = this.discount,
    )
