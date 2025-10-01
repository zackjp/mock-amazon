package com.example.fakeamazon.model

import androidx.annotation.DrawableRes

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
    @DrawableRes val imageRes: Int,
    val discount: Float,
)
