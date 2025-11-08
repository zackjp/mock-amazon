package com.example.fakeamazon.features.home

import com.example.fakeamazon.shared.model.ItemSection
import com.example.fakeamazon.shared.model.TopHomeGroup

sealed class HomeScreenState() {
    object Error : HomeScreenState()

    data class Loaded(
        val homeSections: List<ItemSection>,
        val topHomeGroups: List<TopHomeGroup>,
    ) : HomeScreenState()

    object Loading : HomeScreenState()
}
