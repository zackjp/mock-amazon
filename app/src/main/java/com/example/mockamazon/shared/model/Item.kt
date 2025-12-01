package com.example.mockamazon.shared.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class TopHomeGroup(
    val title: String,
    val background: Color,
    val items: List<Item>
)

data class ItemSection(
    val title: String,
    val itemGroups: List<ItemGroup>,
)

data class ItemGroup(
    val title: String,
    val rec1: Item,
    val rec2: Item,
    val rec3: Item,
    val rec4: Item,
)

data class Item(
    val id: Int,
    @param:DrawableRes val imageRes: Int,
    val discount: Float? = null,
)
