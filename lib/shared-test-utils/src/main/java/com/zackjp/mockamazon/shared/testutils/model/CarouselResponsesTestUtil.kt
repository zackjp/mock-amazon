package com.zackjp.mockamazon.shared.testutils.model

import androidx.compose.ui.graphics.Color
import com.zackjp.mockamazon.shared.R
import com.zackjp.mockamazon.shared.model.ContextCardResponse
import com.zackjp.mockamazon.shared.model.HeroCarouselCardResponse
import com.zackjp.mockamazon.shared.model.IntentCarouselResponse
import com.zackjp.mockamazon.shared.model.ProductTileResponse


fun HeroCarouselCardResponse.Companion.fake(
    id: Long,
    responseCount: Int = 2,
    background: Color = Color.Black,
    backgroundImageId: Int? = id.toInt() % 10_000,
) =
    HeroCarouselCardResponse(
        heroId = "card_id_$id",
        title = "Title $id",
        background = background,
        backgroundImageId = backgroundImageId,
        productGridHeightFraction = 1f / id,
        productTileResponse = (0..<responseCount).map { count ->
            ProductTileResponse.fake(count)
        },
    )

fun IntentCarouselResponse.Companion.fake(
    id: Long,
    cardCount: Int = 2,
) =
    IntentCarouselResponse(
        intentId = "intent_id_$id",
        title = "Title $id",
        contextCardResponse = (0..<cardCount).map { count ->
            ContextCardResponse.fake(count)
        },
    )

fun ContextCardResponse.Companion.fake(
    id: Int,
) =
    ContextCardResponse(
        contextId = "context_id_$id",
        title = "Title $id",
        rec1 = ProductTileResponse.fake(id + 1),
        rec2 = ProductTileResponse.fake(id + 2),
        rec3 = ProductTileResponse.fake(id + 3),
        rec4 = ProductTileResponse.fake(id + 4),
    )

fun ProductTileResponse.Companion.fake(
    id: Int,
): ProductTileResponse =
    ProductTileResponse(
        productId = id,
        imageRes = R.drawable.item_soda,
        discount = id * .1f,
    )
