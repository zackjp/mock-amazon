package com.zackjp.mockamazon.feature.home

import com.zackjp.mockamazon.shared.model.ItemSection
import com.zackjp.mockamazon.shared.model.TopHomeGroup

sealed class HomeScreenState() {
    data object Error : HomeScreenState()

    data class Loaded(
        val homeSections: List<ItemSection>,
        val topHomeGroups: List<TopHomeGroup>,
    ) : HomeScreenState()

    data object Loading : HomeScreenState()
}
