package com.zackjp.mockamazon.feature.search

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeSearchHistoryRepository @Inject constructor() : SearchHistoryRepository {

    private val history = MutableStateFlow(DEFAULT_HISTORY)

    override fun observeHistory(): Flow<List<String>> = history

    companion object {
        val DEFAULT_HISTORY = listOf("mixed nuts", "popcorn", "snacks", "sneakers", "household")
    }
}
