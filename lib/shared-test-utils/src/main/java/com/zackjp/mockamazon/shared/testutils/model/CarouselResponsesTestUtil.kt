package com.zackjp.mockamazon.shared.testutils.model

import androidx.compose.ui.graphics.Color
import com.zackjp.mockamazon.shared.R
import com.zackjp.mockamazon.shared.model.CarouselCardResponse
import com.zackjp.mockamazon.shared.model.CarouselItemResponse
import com.zackjp.mockamazon.shared.model.CategoryCarouselResponse
import com.zackjp.mockamazon.shared.model.HeroCarouselCardResponse


fun HeroCarouselCardResponse.Companion.fake(
    id: Long,
    responseCount: Int = 2,
    background: Color = Color.Black,
) =
    HeroCarouselCardResponse(
        title = "Title $id",
        background = background,
        carouselItemResponses = (0..<responseCount).map { count ->
            CarouselItemResponse.fake(count)
        },
    )

fun CategoryCarouselResponse.Companion.fake(
    id: Long,
    cardCount: Int = 2,
) =
    CategoryCarouselResponse(
        title = "Title $id",
        carouselCardResponse = (0..<cardCount).map { count ->
            CarouselCardResponse.fake(count)
        },
    )

fun CarouselCardResponse.Companion.fake(
    id: Int,
) =
    CarouselCardResponse(
        title = "Title $id",
        rec1 = CarouselItemResponse.fake(id + 1),
        rec2 = CarouselItemResponse.fake(id + 2),
        rec3 = CarouselItemResponse.fake(id + 3),
        rec4 = CarouselItemResponse.fake(id + 4),
    )

fun CarouselItemResponse.Companion.fake(
    id: Int,
): CarouselItemResponse =
    CarouselItemResponse(
        id = id,
        imageRes = R.drawable.item_soda,
        discount = id * .1f,
    )
