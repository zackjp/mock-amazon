package com.zackjp.mockamazon.core.data

import com.zackjp.mockamazon.core.model.HeroCarouselCard
import com.zackjp.mockamazon.core.model.IntentCarousel

interface HomeRepository {

    suspend fun getHeroCarouselCards(): List<HeroCarouselCard>

    suspend fun getIntentCarousels(): List<IntentCarousel>

}
