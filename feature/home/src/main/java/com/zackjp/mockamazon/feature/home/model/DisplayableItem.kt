package com.zackjp.mockamazon.feature.home.model

import androidx.annotation.DrawableRes
import com.zackjp.mockamazon.core.model.ProductTile

data class DisplayableItem(
    @param:DrawableRes val imageId: Int,
    val discount: Float? = null,
)

fun ProductTile.toDisplayableItem() = DisplayableItem(imageId = imageRes, discount = discount)