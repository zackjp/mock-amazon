package com.example.fakeamazon.features.home

import com.example.fakeamazon.data.DealsRepository
import com.example.fakeamazon.model.Recommendation
import com.example.fakeamazon.model.RecommendationGroup
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    val mockDealsRepository = mockk<DealsRepository>()
    val mockDeals = listOf(
        RecommendationGroup(
            "Deals for you",
            Recommendation(123, 0.1f),
            Recommendation(234, 0.2f),
            Recommendation(345, 0.3f),
            Recommendation(456, 0.4f),
        ),
        RecommendationGroup(
            "Inspired by your recent history",
            Recommendation(567, 0.5f),
            Recommendation(678, 0.6f),
            Recommendation(789, 0.7f),
            Recommendation(890, 0.8f),
        )
    )

    lateinit var viewModel: HomeViewModel

    @BeforeEach
    fun setUp() {
        coEvery { mockDealsRepository.loadRecommendedDeals() } returns mockDeals

        viewModel = HomeViewModel(mockDealsRepository)
    }

    @Test
    fun viewModel_Init_StartsWithEmptyRecommendedDeals() = runTest {
        advanceUntilIdle()

        viewModel.recommendationGroups.value shouldBe emptyList()
    }

    @Test
    fun viewModel_Load_LoadsRecommendedDealsAsync() = runTest {
        viewModel.load()

        viewModel.recommendationGroups.value shouldBe emptyList()
        advanceUntilIdle()
        viewModel.recommendationGroups.value shouldBe mockDeals
    }

}
