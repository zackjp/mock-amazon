package com.zackjp.mockamazon.feature.search

import app.cash.turbine.test
import io.kotest.matchers.collections.shouldNotContainAll
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class FakeSearchHistoryRepositoryTest {

    private val repository = FakeSearchHistoryRepository()

    @Test
    fun observeHistory_InitiallyEmpty() = runTest {
        repository.observeHistory().test {
            awaitItem() shouldBe emptyList()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveQuery_AddsQueryToTop() = runTest {
        repository.saveQuery("query1")
        repository.saveQuery("query2")

        repository.observeHistory().test {
            awaitItem() shouldBe listOf("query2", "query1")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveQuery_DeduplicatesExistingQuery_MovesToTop() = runTest {
        repository.saveQuery("query1")
        repository.saveQuery("query2")
        repository.saveQuery("query1")

        repository.observeHistory().test {
            awaitItem() shouldBe listOf("query1", "query2")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveQuery_CapsAtTenEntries_DropsOldest() = runTest {
        repeat(13) { i -> repository.saveQuery("query $i") }

        repository.observeHistory().test {
            val history = awaitItem()
            history.size shouldBe 10
            history shouldNotContainAll listOf("query 0", "query 1", "query 2")
            history.first() shouldBe "query 12"
            history.last() shouldBe "query 3"
            cancelAndIgnoreRemainingEvents()
        }
    }
}
