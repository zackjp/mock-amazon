package com.example.fakeamazon.features.home.model

import androidx.annotation.DrawableRes
import com.example.fakeamazon.model.Recommendation

data class DisplayableItem(
    @DrawableRes val imageId: Int,
    val discount: Float? = null,
)

data class TopHomeGroup(
    val title: String,
    val items: List<DisplayableItem>
)

fun Recommendation.toDisplayableItem() = DisplayableItem(imageId = imageRes, discount = discount)