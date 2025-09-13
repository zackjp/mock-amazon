package com.example.fakeamazon.features.home

import com.example.fakeamazon.base.TestDispatcherProvider
import com.example.fakeamazon.data.DealsRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    val testDispatcher = StandardTestDispatcher()
    val testDispatcherProvider: TestDispatcherProvider = TestDispatcherProvider(testDispatcher)

    lateinit var viewModel: HomeViewModel

    @Before
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

        assert(viewModel.recommendationGroups.value.isEmpty())
        testDispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.recommendationGroups.value.containsAll(DealsRepository.RECOMMENDED_DEALS))
    }

}
