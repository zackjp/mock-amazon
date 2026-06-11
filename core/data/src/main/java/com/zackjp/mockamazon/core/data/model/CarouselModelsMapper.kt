package com.zackjp.mockamazon.core.data.model

import com.zackjp.mockamazon.core.model.ContextCard
import com.zackjp.mockamazon.core.model.ProductTile
import com.zackjp.mockamazon.core.model.IntentCarousel
import com.zackjp.mockamazon.core.model.HeroCarouselCard
import com.zackjp.mockamazon.shared.model.ContextCardResponse
import com.zackjp.mockamazon.shared.model.ProductTileResponse
import com.zackjp.mockamazon.shared.model.IntentCarouselResponse
import com.zackjp.mockamazon.shared.model.HeroCarouselCardResponse


internal fun HeroCarouselCardResponse.toUiModel(): HeroCarouselCard =
    HeroCarouselCard(
        title = this.title,
        background = this.background,
        productTiles = this.productTileRespons.map { it.toUiModel() },
    )

internal fun IntentCarouselResponse.toUiModel(): IntentCarousel =
    IntentCarousel(
        title = this.title,
        contextCards = this.contextCardResponse.map { it.toUiModel() },
    )

internal fun ContextCardResponse.toUiModel(): ContextCard =
    ContextCard(
        title = this.title,
        rec1 = this.rec1.toUiModel(),
        rec2 = this.rec2.toUiModel(),
        rec3 = this.rec3.toUiModel(),
        rec4 = this.rec4.toUiModel(),
    )

internal fun ProductTileResponse.toUiModel(): ProductTile =
    ProductTile(
        id = this.id,
        imageRes = this.imageRes,
        discount = this.discount,
    )
