package com.zackjp.mockamazon.shared.data

import com.zackjp.mockamazon.shared.model.CategoryCarouselResponse
import com.zackjp.mockamazon.shared.model.HeroCarouselCardResponse
import com.zackjp.mockamazon.shared.testutils.model.fake
import com.zackjp.mockamazon.shared.ui.model.toUiModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeRepositoryTest {

    private val homeFakeApiDataSource = mockk<HomeFakeApiDataSource>()

    private lateinit var repository: HomeRepository

    @BeforeEach
    fun setUp() {
        repository = HomeRepository(
            homeFakeApiDataSource = homeFakeApiDataSource,
        )
    }

    @Test
    fun getHeroCarouselCards_HavingResultsFromApi_ReturnsHeroCarouselCardsAsync() = runTest {
        val apiResponse1 = HeroCarouselCardResponse.fake(id = 5)
        val apiResponse2 = HeroCarouselCardResponse.fake(id = 11)
        coEvery { homeFakeApiDataSource.fetchHeroCarouselCards() } returns listOf(
            apiResponse1,
            apiResponse2,
        )

        val expectedModel1 = apiResponse1.toUiModel()
        val expectedModel2 = apiResponse2.toUiModel()
        repository.getHeroCarouselCards() shouldBe listOf(expectedModel1, expectedModel2)
    }

    @Test
    fun getCategoryCarousels_HavingResultsFromApi_ReturnsCategoryCarouselsAsync() = runTest {
        val apiResponse1 = CategoryCarouselResponse.fake(id = 5)
        val apiResponse2 = CategoryCarouselResponse.fake(id = 11)
        coEvery { homeFakeApiDataSource.fetchCategoryCarousels() } returns listOf(
            apiResponse1,
            apiResponse2,
        )

        val expectedModel1 = apiResponse1.toUiModel()
        val expectedModel2 = apiResponse2.toUiModel()
        repository.getCategoryCarousels() shouldBe listOf(expectedModel1, expectedModel2)
    }

}