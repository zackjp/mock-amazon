package com.zackjp.mockamazon.data

import com.zackjp.mockamazon.data.model.toUiModel
import com.zackjp.mockamazon.data.remote.HomeFakeApiDataSource
import com.zackjp.mockamazon.model.CategoryCarousel
import com.zackjp.mockamazon.model.HeroCarouselCard
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HomeRepositoryImpl @Inject constructor(
    private val homeFakeApiDataSource: HomeFakeApiDataSource,
) : HomeRepository {

    override suspend fun getHeroCarouselCards(): List<HeroCarouselCard> =
        homeFakeApiDataSource.fetchHeroCarouselCards().map { it.toUiModel() }

    override suspend fun getCategoryCarousels(): List<CategoryCarousel> =
        homeFakeApiDataSource.fetchCategoryCarousels().map { it.toUiModel() }

}
