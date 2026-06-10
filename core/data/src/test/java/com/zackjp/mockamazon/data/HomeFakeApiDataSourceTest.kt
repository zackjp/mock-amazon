package com.zackjp.mockamazon.data

import com.zackjp.mockamazon.data.HomeFakeApiDataSource
import com.zackjp.mockamazon.shared.data.ProductInMemoryDb
import com.zackjp.mockamazon.shared.testutils.TestDispatcherProvider
import io.kotest.matchers.comparables.shouldBeGreaterThan
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeFakeApiDataSourceTest {

    private val testDispatcherProvider = TestDispatcherProvider()
    private val testDispatcher = testDispatcherProvider.default

    private lateinit var homeFakeApiDataSource: HomeFakeApiDataSource

    @BeforeEach
    fun setUp() {
        homeFakeApiDataSource = HomeFakeApiDataSource(
            dispatcherProvider = testDispatcherProvider,
            productInMemoryDb = ProductInMemoryDb(),
        )
    }

    @Test
    fun fetchHeroCarouselCards_ReturnsMockData() = runTest(testDispatcher) {
        homeFakeApiDataSource.fetchHeroCarouselCards().size shouldBeGreaterThan 0
    }

    @Test
    fun fetchCategoryCarousels_ReturnsMockData() = runTest(testDispatcher) {
        homeFakeApiDataSource.fetchCategoryCarousels().size shouldBeGreaterThan 0
    }

}
