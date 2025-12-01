package com.example.mockamazon.features.home.model

import androidx.annotation.DrawableRes
import com.example.mockamazon.shared.model.Item

data class DisplayableItem(
    @DrawableRes val imageId: Int,
    val discount: Float? = null,
)

fun Item.toDisplayableItem() = DisplayableItem(imageId = imageRes, discount = discount)