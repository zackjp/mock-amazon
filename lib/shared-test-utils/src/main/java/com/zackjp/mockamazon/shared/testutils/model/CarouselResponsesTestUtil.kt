package com.zackjp.mockamazon.shared.testutils.model

import androidx.compose.ui.graphics.Color
import com.zackjp.mockamazon.shared.R
import com.zackjp.mockamazon.shared.model.ContextCardResponse
import com.zackjp.mockamazon.shared.model.ProductTileResponse
import com.zackjp.mockamazon.shared.model.IntentCarouselResponse
import com.zackjp.mockamazon.shared.model.HeroCarouselCardResponse


fun HeroCarouselCardResponse.Companion.fake(
    id: Long,
    responseCount: Int = 2,
    background: Color = Color.Black,
) =
    HeroCarouselCardResponse(
        title = "Title $id",
        background = background,
        productTileRespons = (0..<responseCount).map { count ->
            ProductTileResponse.fake(count)
        },
    )

fun IntentCarouselResponse.Companion.fake(
    id: Long,
    cardCount: Int = 2,
) =
    IntentCarouselResponse(
        title = "Title $id",
        contextCardResponse = (0..<cardCount).map { count ->
            ContextCardResponse.fake(count)
        },
    )

fun ContextCardResponse.Companion.fake(
    id: Int,
) =
    ContextCardResponse(
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
        id = id,
        imageRes = R.drawable.item_soda,
        discount = id * .1f,
    )
