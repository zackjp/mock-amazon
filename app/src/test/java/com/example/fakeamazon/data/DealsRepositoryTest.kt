package com.example.fakeamazon.data

import com.example.fakeamazon.base.TestDispatcherProvider
import io.kotest.matchers.ints.shouldBeGreaterThan
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DealsRepositoryTest {

    val testDispatcher = StandardTestDispatcher()
    val testDispatcherProvider = TestDispatcherProvider(testDispatcher)

    @Test
    fun dealsRepository_Load_ReturnsMockData() = runTest(testDispatcher) {
        val repository = DealsRepository(testDispatcherProvider)

        repository.loadRecommendedDeals().size shouldBeGreaterThan 0
    }

}