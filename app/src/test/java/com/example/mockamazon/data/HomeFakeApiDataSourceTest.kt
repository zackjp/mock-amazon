package com.example.mockamazon.data

import com.example.mockamazon.shared.testutils.TestDispatcherProvider
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
    fun fetchTopHomeGroups_ReturnsMockData() = runTest(testDispatcher) {
        homeFakeApiDataSource.fetchTopHomeGroups().size shouldBeGreaterThan 0
    }

    @Test
    fun fetchHomeSections_ReturnsMockData() = runTest(testDispatcher) {
        homeFakeApiDataSource.fetchHomeSections().size shouldBeGreaterThan 0
    }

}
