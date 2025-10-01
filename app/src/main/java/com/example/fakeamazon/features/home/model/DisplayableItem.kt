package com.example.fakeamazon.features.home.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.fakeamazon.model.Item

data class DisplayableItem(
    @DrawableRes val imageId: Int,
    val discount: Float? = null,
)

data class TopHomeGroup(
    val title: String,
    val background: Color,
    val items: List<DisplayableItem>
)

fun Item.toDisplayableItem() = DisplayableItem(imageId = imageRes, discount = discount)