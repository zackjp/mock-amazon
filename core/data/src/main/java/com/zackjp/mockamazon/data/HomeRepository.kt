package com.zackjp.mockamazon.data

import com.zackjp.mockamazon.data.model.toUiModel
import com.zackjp.mockamazon.data.HomeFakeApiDataSource
import com.zackjp.mockamazon.model.CategoryCarousel
import com.zackjp.mockamazon.model.HeroCarouselCard
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
