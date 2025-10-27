package com.example.fakeamazon.features.home.model

import androidx.annotation.DrawableRes
import com.example.fakeamazon.shared.model.Item

data class DisplayableItem(
    @DrawableRes val imageId: Int,
    val discount: Float? = null,
)

fun Item.toDisplayableItem() = DisplayableItem(imageId = imageRes, discount = discount)