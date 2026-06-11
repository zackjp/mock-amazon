package com.zackjp.mockamazon.core.data

import com.zackjp.mockamazon.core.data.model.toUiModel
import com.zackjp.mockamazon.core.data.remote.HomeFakeApiDataSource
import com.zackjp.mockamazon.shared.model.HeroCarouselCardResponse
import com.zackjp.mockamazon.shared.model.IntentCarouselResponse
import com.zackjp.mockamazon.shared.testutils.model.fake
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeRepositoryImplTest {

    private val homeFakeApiDataSource = mockk<HomeFakeApiDataSource>()

    private lateinit var repository: HomeRepository

    @BeforeEach
    fun setUp() {
        repository = HomeRepositoryImpl(
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
    fun getIntentCarousels_HavingResultsFromApi_ReturnsIntentCarouselsAsync() = runTest {
        val apiResponse1 = IntentCarouselResponse.fake(id = 5)
        val apiResponse2 = IntentCarouselResponse.fake(id = 11)
        coEvery { homeFakeApiDataSource.fetchIntentCarousels() } returns listOf(
            apiResponse1,
            apiResponse2,
        )

        val expectedModel1 = apiResponse1.toUiModel()
        val expectedModel2 = apiResponse2.toUiModel()
        repository.getIntentCarousels() shouldBe listOf(expectedModel1, expectedModel2)
    }

}