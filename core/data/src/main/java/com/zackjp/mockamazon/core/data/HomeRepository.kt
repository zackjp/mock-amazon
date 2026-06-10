package com.zackjp.mockamazon.core.data

import com.zackjp.mockamazon.core.model.CategoryCarousel
import com.zackjp.mockamazon.core.model.HeroCarouselCard

interface HomeRepository {

    suspend fun getHeroCarouselCards(): List<HeroCarouselCard>

    suspend fun getCategoryCarousels(): List<CategoryCarousel>

}
