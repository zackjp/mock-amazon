package com.zackjp.mockamazon.feature.home

import com.zackjp.mockamazon.core.model.HeroCarouselCard
import com.zackjp.mockamazon.core.model.IntentCarousel

sealed class HomeScreenState() {
    data object Error : HomeScreenState()

    data class Loaded(
        val heroCarouselCards: List<HeroCarouselCard>,
        val intentCarousels: List<IntentCarousel>,
    ) : HomeScreenState()

    data object Loading : HomeScreenState()
}
