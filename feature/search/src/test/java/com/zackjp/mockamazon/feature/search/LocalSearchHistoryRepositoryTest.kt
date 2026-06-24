package com.zackjp.mockamazon.feature.search

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import app.cash.turbine.test
import io.kotest.matchers.collections.shouldNotContainAnyOf
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.io.IOException

class LocalSearchHistoryRepositoryTest {

    private val fakeDataStore = FakePreferencesDataStore()
    private val repository = LocalSearchHistoryRepository(fakeDataStore)

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
            history shouldNotContainAnyOf listOf("query 0", "query 1", "query 2")
            history.first() shouldBe "query 12"
            history.last() shouldBe "query 3"
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun observeHistory_EmitsEmptyList_WhenStoredJsonIsCorrupt() = runTest {
        fakeDataStore.edit { it[stringPreferencesKey("search_history")] = "not valid json {" }

        repository.observeHistory().test {
            awaitItem() shouldBe emptyList()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveQuery_TreatsCorruptStorageAsEmpty_SavesNewQuery() = runTest {
        fakeDataStore.edit { it[stringPreferencesKey("search_history")] = "not valid json {" }
        repository.saveQuery("query1")

        repository.observeHistory().test {
            awaitItem() shouldBe listOf("query1")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun removeQuery_RemovesExistingQuery() = runTest {
        repository.saveQuery("query1")
        repository.saveQuery("query2")
        repository.saveQuery("query3")
        repository.removeQuery("query2")

        repository.observeHistory().test {
            awaitItem() shouldBe listOf("query3", "query1")
        }
    }

    @Test
    fun removeQuery_NonExistingQuery_DoesNothing() = runTest {
        repository.saveQuery("query1")
        repository.saveQuery("query2")
        repository.saveQuery("query3")
        repository.removeQuery("nonExistingQuery")

        repository.observeHistory().test {
            awaitItem() shouldBe listOf("query3", "query2", "query1")
        }
    }

    @Test
    fun observeHistory_EmitsEmptyList_WhenDataStoreThrowsIOException() = runTest {
        val failingDataStore = object : DataStore<Preferences> {
            override val data: Flow<Preferences> = flow { throw IOException("disk read failed") }
            override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences =
                throw IOException("disk read failed")
        }
        val repositoryWithFailingDataStore = LocalSearchHistoryRepository(failingDataStore)

        repositoryWithFailingDataStore.observeHistory().test {
            awaitItem() shouldBe emptyList()
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private class FakePreferencesDataStore : DataStore<Preferences> {
    private val _data = MutableStateFlow(emptyPreferences())
    override val data: Flow<Preferences> = _data

    override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
        val updated = transform(_data.value)
        _data.value = updated
        return updated
    }
}
