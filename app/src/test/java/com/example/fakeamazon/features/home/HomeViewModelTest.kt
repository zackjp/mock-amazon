package com.example.fakeamazon.features.home

import com.example.fakeamazon.base.TestDispatcherProvider
import com.example.fakeamazon.data.DealsRepository
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeViewModelTest {

    val testDispatcher = StandardTestDispatcher()
    val testDispatcherProvider: TestDispatcherProvider = TestDispatcherProvider(testDispatcher)

    lateinit var viewModel: HomeViewModel

    @BeforeEach
    fun setUp() {
        viewModel = HomeViewModel(testDispatcherProvider)
    }

    @Test
    fun viewModel_Init_StartsWithEmptyRecommendedDeals() {
        assert(viewModel.recommendationGroups.value.isEmpty())
    }

    @Test
    fun viewModel_Load_LoadsRecommendedDealsAsync() {
        viewModel.load()

        viewModel.recommendationGroups.value shouldBe emptyList()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.recommendationGroups.value shouldBe DealsRepository.RECOMMENDED_DEALS
    }

}
