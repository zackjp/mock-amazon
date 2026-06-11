package com.zackjp.mockamazon.core.data

import com.zackjp.mockamazon.core.data.model.toUiModel
import com.zackjp.mockamazon.core.data.remote.HomeFakeApiDataSource
import com.zackjp.mockamazon.core.model.IntentCarousel
import com.zackjp.mockamazon.core.model.HeroCarouselCard
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HomeRepositoryImpl @Inject constructor(
    private val homeFakeApiDataSource: HomeFakeApiDataSource,
) : HomeRepository {

    override suspend fun getHeroCarouselCards(): List<HeroCarouselCard> =
        homeFakeApiDataSource.fetchHeroCarouselCards().map { it.toUiModel() }

    override suspend fun getIntentCarousels(): List<IntentCarousel> =
        homeFakeApiDataSource.fetchIntentCarousels().map { it.toUiModel() }

}
