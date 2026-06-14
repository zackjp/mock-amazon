package com.zackjp.mockamazon.core.data.remote

import com.zackjp.mockamazon.shared.data.ProductInMemoryDb
import com.zackjp.mockamazon.shared.testutils.TestDispatcherProvider
import io.kotest.matchers.comparables.shouldBeGreaterThan
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeFakeApiDataSourceTest {

    private val testDispatcherProvider = TestDispatcherProvider()
    private val testDispatcher = testDispatcherProvider.io

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
    fun fetchIntentCarousels_ReturnsMockData() = runTest(testDispatcher) {
        homeFakeApiDataSource.fetchIntentCarousels().size shouldBeGreaterThan 0
    }

}