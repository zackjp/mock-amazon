package com.example.mockamazon.features.home

import com.example.mockamazon.shared.model.ItemSection
import com.example.mockamazon.shared.model.TopHomeGroup

sealed class HomeScreenState() {
    object Error : HomeScreenState()

    data class Loaded(
        val homeSections: List<ItemSection>,
        val topHomeGroups: List<TopHomeGroup>,
    ) : HomeScreenState()

    object Loading : HomeScreenState()
}
