package com.zackjp.mockamazon.feature.search

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeSearchHistoryRepository @Inject constructor() : SearchHistoryRepository {

    private val history = MutableStateFlow(emptyList<String>())

    override fun observeHistory(): Flow<List<String>> = history

    override suspend fun saveQuery(query: String) {
        history.value = buildList {
            add(query)
            addAll(history.value.filterNot { it == query })
            if (size > MAX_HISTORY_SIZE) removeAt(MAX_HISTORY_SIZE)
        }
    }

    companion object {
        private const val MAX_HISTORY_SIZE = 10
    }
}
