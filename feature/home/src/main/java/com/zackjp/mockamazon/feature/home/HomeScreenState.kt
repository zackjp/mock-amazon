package com.zackjp.mockamazon.feature.home

import com.zackjp.mockamazon.shared.ui.model.CategoryCarousel
import com.zackjp.mockamazon.shared.ui.model.HeroCarouselCard

sealed class HomeScreenState() {
    data object Error : HomeScreenState()

    data class Loaded(
        val categoryCarousels: List<CategoryCarousel>,
        val heroCarouselCards: List<HeroCarouselCard>,
    ) : HomeScreenState()

    data object Loading : HomeScreenState()
}
