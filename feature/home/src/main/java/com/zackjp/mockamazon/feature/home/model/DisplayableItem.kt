package com.zackjp.mockamazon.feature.home.model

import androidx.annotation.DrawableRes
import com.zackjp.mockamazon.shared.ui.model.CarouselItem

data class DisplayableItem(
    @DrawableRes val imageId: Int,
    val discount: Float? = null,
)

fun CarouselItem.toDisplayableItem() = DisplayableItem(imageId = imageRes, discount = discount)