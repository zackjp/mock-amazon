package com.zackjp.mockamazon.shared.data

import com.zackjp.mockamazon.shared.ui.model.CategoryCarousel
import com.zackjp.mockamazon.shared.ui.model.HeroCarouselCard
import com.zackjp.mockamazon.shared.ui.model.toUiModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val homeFakeApiDataSource: HomeFakeApiDataSource,
) {

    suspend fun getHeroCarouselCards(): List<HeroCarouselCard> =
        homeFakeApiDataSource.fetchHeroCarouselCards().map { it.toUiModel() }

    suspend fun getCategoryCarousels(): List<CategoryCarousel> =
        homeFakeApiDataSource.fetchCategoryCarousels().map { it.toUiModel() }

}
