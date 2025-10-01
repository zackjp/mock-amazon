package com.example.fakeamazon.data

import com.example.fakeamazon.base.TestDispatcherProvider
import io.kotest.matchers.ints.shouldBeGreaterThan
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class HomeRepositoryTest {

    val testDispatcher = StandardTestDispatcher()
    val testDispatcherProvider = TestDispatcherProvider(testDispatcher)

    @Test
    fun homeRepository_Load_ReturnsMockData() = runTest(testDispatcher) {
        val repository = HomeRepository(testDispatcherProvider)

        repository.loadSections().size shouldBeGreaterThan 0
    }

}